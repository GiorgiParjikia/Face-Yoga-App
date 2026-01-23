package ru.netology.faceyoga.ui.player

import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.netology.faceyoga.R
import ru.netology.faceyoga.data.media.VideoUrlResolver
import ru.netology.faceyoga.databinding.FragmentVideoPlayerBinding
import ru.netology.faceyoga.ui.common.localizedExerciseTitle
import ru.netology.faceyoga.ui.day.DayExerciseUi
import ru.netology.faceyoga.ui.day.DayExercisesViewModel
import javax.inject.Inject
import kotlin.math.max

@AndroidEntryPoint
class VideoPlayerFragment : Fragment(R.layout.fragment_video_player) {

    private var _binding: FragmentVideoPlayerBinding? = null
    private val binding get() = _binding!!

    private val playerViewModel: PlayerViewModel by viewModels()
    private val dayViewModel: DayExercisesViewModel by viewModels()

    @Inject lateinit var videoUrlResolver: VideoUrlResolver

    private var player: ExoPlayer? = null

    // --- TIMER ---
    private var timer: CountDownTimer? = null
    private var lastTimerKey: String? = null

    private fun formatMmSs(totalSeconds: Int): String {
        val mm = totalSeconds / 60
        val ss = totalSeconds % 60
        return "%02d:%02d".format(mm, ss)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        _binding = FragmentVideoPlayerBinding.bind(view)

        // 🎥 Player (LOOP)
        player = ExoPlayer.Builder(requireContext()).build().also { exo ->
            exo.repeatMode = Player.REPEAT_MODE_ONE
            binding.playerView.player = exo
        }

        // 👉 Next
        binding.btnNext.setOnClickListener {
            playerViewModel.next()
        }

        // 1) получаем упражнения дня
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                dayViewModel.exercises.collect { list ->
                    val withVideo = list.filter { !it.videoUri.isNullOrBlank() }
                    if (withVideo.isNotEmpty()) {
                        playerViewModel.setQueue(withVideo)
                    } else {
                        Toast.makeText(requireContext(), "Для этого дня нет видео", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        // 2) на смену current — обновляем UI + играем видео
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                playerViewModel.queue.collect { state ->
                    updateOverlay(state)
                    playCurrent()
                }
            }
        }
    }

    private fun updateOverlay(state: PlayerQueueState) {
        val ctx = requireContext()
        val current = state.current ?: return

        // 1 / N
        binding.tvCounter.text = "${state.index + 1} / ${max(1, state.list.size)}"

        // Current title (localized)
        binding.tvTitle.text = ctx.localizedExerciseTitle(current.title)

        // TIMER vs REPS
        if (isTimer(current)) {
            // показываем прогрессбар
            binding.progressLine.visibility = View.VISIBLE

            val seconds = secondsFromRightInfo(current).coerceAtLeast(1)

            // чтобы таймер не перезапускался при каждом повторном эмите state
            val key = "${current.title}|${current.rightInfo}|${state.index}"
            if (lastTimerKey != key) {
                lastTimerKey = key
                startRestTimer(seconds)
            }
        } else {
            // скрываем прогрессбар
            binding.progressLine.visibility = View.GONE

            // останавливаем таймер, сбрасываем ключ
            stopTimerAndResetProgress()

            // для повторений показываем статичную инфу
            binding.tvMainInfo.text = mainInfoText(current)
        }

        // Next line + button enabled
        val next = state.list.getOrNull(state.index + 1)
        if (next != null) {
            val nextTitle = ctx.localizedExerciseTitle(next.title)
            val nextInfo = mainInfoText(next)
            binding.tvNext.text = ctx.getString(R.string.next_prefix, "$nextTitle — $nextInfo")
            binding.btnNext.isEnabled = true
        } else {
            binding.tvNext.text = ""
            binding.btnNext.isEnabled = false
        }
    }

    private fun startRestTimer(totalSeconds: Int) {
        timer?.cancel()

        // +1 секунда, чтобы старт был с 01:00, а не 00:59
        val duration = totalSeconds + 1

        // инверт: стартуем с полного и уменьшаем до 0
        binding.progressLine.max = totalSeconds
        binding.progressLine.progress = totalSeconds

        // стартовое значение текста
        binding.tvMainInfo.text = formatMmSs(totalSeconds)

        timer = object : CountDownTimer(duration * 1000L, 1000L) {
            override fun onTick(millisUntilFinished: Long) {
                val left = (millisUntilFinished / 1000L).toInt().coerceAtMost(totalSeconds)

                // 🔁 инвертированный прогресс (убывает)
                binding.progressLine.progress = left

                // ⏱ тикающее время
                binding.tvMainInfo.text = formatMmSs(left)
            }

            override fun onFinish() {
                binding.progressLine.progress = 0
                binding.tvMainInfo.text = "00:00"
                // автопереход после отдыха:
                playerViewModel.next()
            }
        }.start()
    }

    private fun stopTimerAndResetProgress() {
        timer?.cancel()
        timer = null
        lastTimerKey = null

        // max сбрасывать не обязательно
        binding.progressLine.progress = 0
    }

    private fun playCurrent() {
        val item = playerViewModel.current() ?: return
        val gs = item.videoUri ?: return

        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO) {
            runCatching {
                val https = videoUrlResolver.resolve(gs)
                launch(Dispatchers.Main) {
                    player?.apply {
                        setMediaItem(MediaItem.fromUri(https))
                        prepare()
                        playWhenReady = true
                    }
                }
            }.onFailure {
                launch(Dispatchers.Main) {
                    Toast.makeText(requireContext(), "Не удалось открыть видео", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    // -------- helpers under your DayExerciseUi --------

    private fun isTimer(item: DayExerciseUi): Boolean =
        item.rightInfo.contains(":")

    private fun secondsFromRightInfo(item: DayExerciseUi): Int {
        // ожидаем "mm:ss"
        val parts = item.rightInfo.split(":")
        if (parts.size != 2) return 0
        val mm = parts[0].toIntOrNull() ?: return 0
        val ss = parts[1].toIntOrNull() ?: return 0
        return mm * 60 + ss
    }

    private fun repsFromRightInfo(item: DayExerciseUi): Int {
        // rightInfo like "x10" -> 10
        return item.rightInfo.filter { it.isDigit() }.toIntOrNull() ?: 0
    }

    private fun mainInfoText(item: DayExerciseUi): String {
        val ctx = requireContext()
        return if (isTimer(item)) {
            item.rightInfo // "01:00"
        } else {
            val reps = repsFromRightInfo(item).takeIf { it > 0 } ?: 10
            ctx.getString(R.string.reps_format, reps) // "10 повторений"
        }
    }

    override fun onStop() {
        super.onStop()

        // stop timer
        timer?.cancel()
        timer = null
        lastTimerKey = null

        // release player
        player?.release()
        player = null
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}