package ru.netology.faceyoga.ui.player

import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.Toast
import androidx.activity.addCallback
import androidx.core.os.bundleOf
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import androidx.navigation.fragment.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
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
    private var pausedTimerSeconds: Int? = null   // сколько осталось при сворачивании

    // очередь
    private var queueWasSet = false
    private var lastQueueState: PlayerQueueState? = null

    // аргументы
    private val programDayId: Long by lazy {
        requireArguments().getLong("programDayId")
    }

    private val dayNumber: Int by lazy {
        requireArguments().getInt("dayNumber", 1)
    }

    private fun formatMmSs(totalSeconds: Int): String {
        val mm = totalSeconds / 60
        val ss = totalSeconds % 60
        return "%02d:%02d".format(mm, ss)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        _binding = FragmentVideoPlayerBinding.bind(view)

        // ✅ Поднимаем нижнюю панель над системной навигацией (кнопки/жесты)
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { _, insets ->
            val navBarBottom = insets.getInsets(WindowInsetsCompat.Type.navigationBars()).bottom

            // bottomContainer должен быть в xml: android:id="@+id/bottomContainer"
            binding.bottomContainer.setPadding(
                binding.bottomContainer.paddingLeft,
                binding.bottomContainer.paddingTop,
                binding.bottomContainer.paddingRight,
                navBarBottom + dp(8)
            )
            insets
        }

        // ✅ Перехват "Назад" с подтверждением выхода
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            showExitDialog()
        }

        // ✅ Клик по крестику (Close)
        binding.btnClose.setOnClickListener {
            showExitDialog()
        }

        // 🎥 Player
        player = ExoPlayer.Builder(requireContext()).build().also { exo ->
            exo.repeatMode = Player.REPEAT_MODE_ONE
            binding.playerView.player = exo

            exo.addListener(object : Player.Listener {
                override fun onPlaybackStateChanged(state: Int) {
                    val loading = state == Player.STATE_BUFFERING || state == Player.STATE_IDLE
                    binding.loadingOverlay.visibility = if (loading) View.VISIBLE else View.GONE
                }

                override fun onIsPlayingChanged(isPlaying: Boolean) {
                    if (isPlaying) binding.loadingOverlay.visibility = View.GONE
                }
            })
        }

        // 👉 Next / Finish
        binding.btnNext.setOnClickListener {
            val state = lastQueueState ?: return@setOnClickListener

            val hasNext = state.index + 1 < state.list.size
            if (hasNext) {
                pausedTimerSeconds = null
                playerViewModel.next()
            } else {
                findNavController().navigate(
                    R.id.action_videoPlayerFragment_to_congratsFragment,
                    bundleOf(
                        "programDayId" to programDayId,
                        "dayNumber" to dayNumber
                    )
                )
            }
        }

        // 1️⃣ упражнения дня
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                dayViewModel.exercises.collect { list ->
                    if (list.isEmpty()) return@collect

                    val withVideo = list.filter { !it.videoUri.isNullOrBlank() }

                    if (withVideo.isNotEmpty()) {
                        if (!queueWasSet) {
                            queueWasSet = true
                            playerViewModel.setQueue(withVideo)
                        }
                    } else {
                        Toast.makeText(requireContext(), "Для этого дня нет видео", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        // 2️⃣ смена текущего упражнения
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                playerViewModel.queue.collect { state ->
                    lastQueueState = state
                    updateOverlay(state)
                    playCurrent()
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()

        if (player != null && playerViewModel.current() != null) {
            player?.playWhenReady = true
            player?.play()
            binding.loadingOverlay.visibility = View.GONE
        }
        // ❗ Таймер НЕ запускаем тут — он возобновится внутри updateOverlay()
    }

    private fun updateOverlay(state: PlayerQueueState) {
        val ctx = requireContext()
        val current = state.current ?: return

        binding.tvCounter.text = "${state.index + 1} / ${max(1, state.list.size)}"
        binding.tvTitle.text = ctx.localizedExerciseTitle(current.title)

        if (isTimer(current)) {
            binding.progressLine.visibility = View.VISIBLE
            val seconds = secondsFromRightInfo(current).coerceAtLeast(1)
            val key = "${current.title}|${current.rightInfo}|${state.index}"

            val resume = pausedTimerSeconds
            if (resume != null && resume > 0 && lastTimerKey == key && timer == null) {
                pausedTimerSeconds = null
                startRestTimer(resume)
            } else {
                if (lastTimerKey != key) {
                    lastTimerKey = key
                    pausedTimerSeconds = null
                    startRestTimer(seconds)
                }
            }

        } else {
            binding.progressLine.visibility = View.GONE
            stopTimerAndResetProgress()
            binding.tvMainInfo.text = mainInfoText(current)
        }

        val next = state.list.getOrNull(state.index + 1)
        if (next != null) {
            val nextTitle = ctx.localizedExerciseTitle(next.title)
            val nextInfo = mainInfoText(next)
            binding.tvNext.text = ctx.getString(R.string.next_prefix, "$nextTitle — $nextInfo")

            binding.btnNext.isEnabled = true
            binding.btnNext.text = ctx.getString(R.string.action_next)
        } else {
            binding.tvNext.text = ""
            binding.btnNext.isEnabled = true
            binding.btnNext.text = ctx.getString(R.string.finish_day_button)
        }
    }

    private fun startRestTimer(totalSeconds: Int) {
        timer?.cancel()

        val duration = totalSeconds + 1

        binding.progressLine.max = totalSeconds
        binding.progressLine.progress = totalSeconds
        binding.tvMainInfo.text = formatMmSs(totalSeconds)

        timer = object : CountDownTimer(duration * 1000L, 1000L) {
            override fun onTick(millisUntilFinished: Long) {
                val left = (millisUntilFinished / 1000L).toInt().coerceAtMost(totalSeconds)
                pausedTimerSeconds = left
                binding.progressLine.progress = left
                binding.tvMainInfo.text = formatMmSs(left)
            }

            override fun onFinish() {
                pausedTimerSeconds = null
                binding.progressLine.progress = 0
                binding.tvMainInfo.text = "00:00"
                playerViewModel.next()
            }
        }.start()
    }

    private fun stopTimerAndResetProgress() {
        timer?.cancel()
        timer = null
        pausedTimerSeconds = null
        binding.progressLine.progress = 0
    }

    private fun playCurrent() {
        val item = playerViewModel.current() ?: return
        val gs = item.videoUri ?: return

        binding.loadingOverlay.visibility = View.VISIBLE

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
                    binding.loadingOverlay.visibility = View.GONE
                    Toast.makeText(requireContext(), "Не удалось открыть видео", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun isTimer(item: DayExerciseUi): Boolean =
        item.rightInfo.contains(":")

    private fun secondsFromRightInfo(item: DayExerciseUi): Int {
        val parts = item.rightInfo.split(":")
        if (parts.size != 2) return 0
        val mm = parts[0].toIntOrNull() ?: return 0
        val ss = parts[1].toIntOrNull() ?: return 0
        return mm * 60 + ss
    }

    private fun repsFromRightInfo(item: DayExerciseUi): Int =
        item.rightInfo.filter { it.isDigit() }.toIntOrNull() ?: 0

    private fun mainInfoText(item: DayExerciseUi): String {
        val ctx = requireContext()
        return if (isTimer(item)) {
            item.rightInfo
        } else {
            val reps = repsFromRightInfo(item).takeIf { it > 0 } ?: 10
            ctx.getString(R.string.reps_format, reps)
        }
    }

    override fun onStop() {
        super.onStop()

        pausedTimerSeconds =
            if (binding.progressLine.visibility == View.VISIBLE)
                binding.progressLine.progress.takeIf { it > 0 }
            else null

        timer?.cancel()
        timer = null

        player?.playWhenReady = false
        player?.pause()
        player?.stop()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        player?.release()
        player = null
    }

    // ✅ подтверждение выхода из тренировки
    private fun showExitDialog() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(getString(R.string.exit_workout_title))
            .setMessage(getString(R.string.exit_workout_message))
            .setNegativeButton(getString(R.string.stay)) { dialog, _ ->
                dialog.dismiss()
            }
            .setPositiveButton(getString(R.string.exit)) { _, _ ->
                findNavController().popBackStack(R.id.dayExercisesFragment, false)
            }
            .show()
    }

    private fun dp(value: Int): Int =
        (value * resources.displayMetrics.density).toInt()
}