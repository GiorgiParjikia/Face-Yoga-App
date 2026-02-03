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
import ru.netology.faceyoga.ui.common.StateKeys
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

    private var timer: CountDownTimer? = null
    private var lastTimerKey: String? = null
    private var pausedTimerSeconds: Int? = null

    private var queueWasSet = false
    private var lastQueueState: PlayerQueueState? = null

    private val programDayId: Long by lazy {
        requireArguments().getLong(StateKeys.PROGRAM_DAY_ID)
    }

    private val dayNumber: Int by lazy {
        requireArguments().getInt(StateKeys.DAY_NUMBER, 1)
    }

    // ---------------- lifecycle ----------------

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        _binding = FragmentVideoPlayerBinding.bind(view)

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { _, insets ->
            val navBarBottom = insets.getInsets(WindowInsetsCompat.Type.navigationBars()).bottom
            binding.bottomContainer.setPadding(
                binding.bottomContainer.paddingLeft,
                binding.bottomContainer.paddingTop,
                binding.bottomContainer.paddingRight,
                navBarBottom + dp(8)
            )
            insets
        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            showExitDialog()
        }
        binding.btnClose.setOnClickListener { showExitDialog() }

        setupPlayer()
        setupButtons()
        observeDayExercises()
        observeQueue()
    }

    override fun onStart() {
        super.onStart()
        player?.playWhenReady = true
        player?.play()
    }

    override fun onStop() {
        super.onStop()

        pausedTimerSeconds =
            if (binding.progressLine.visibility == View.VISIBLE)
                binding.progressLine.progress.takeIf { it > 0 }
            else null

        timer?.cancel()
        timer = null

        player?.pause()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        player?.release()
        player = null
    }

    // ---------------- setup ----------------

    private fun setupPlayer() {
        player = ExoPlayer.Builder(requireContext()).build().also { exo ->
            exo.repeatMode = Player.REPEAT_MODE_ONE
            binding.playerView.player = exo

            exo.addListener(object : Player.Listener {
                override fun onPlaybackStateChanged(state: Int) {
                    when (state) {
                        Player.STATE_BUFFERING ->
                            binding.loadingOverlay.visibility = View.VISIBLE
                        Player.STATE_READY ->
                            binding.loadingOverlay.visibility = View.GONE
                    }
                }
            })
        }
    }

    private fun setupButtons() {
        binding.btnNext.setOnClickListener {
            val state = lastQueueState ?: return@setOnClickListener
            val hasNext = state.index + 1 < state.list.size

            if (hasNext) {
                pausedTimerSeconds = null
                playerViewModel.next()
            } else {
                playerViewModel.finishDay()
                findNavController().navigate(
                    R.id.action_videoPlayerFragment_to_congratsFragment,
                    bundleOf(
                        StateKeys.PROGRAM_DAY_ID to programDayId,
                        StateKeys.DAY_NUMBER to dayNumber
                    )
                )
            }
        }
    }

    // ---------------- observers ----------------

    private fun observeDayExercises() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                dayViewModel.exercises.collect { list ->
                    if (list.isEmpty()) return@collect

                    val withVideo = list.filter { !it.videoUri.isNullOrBlank() }
                    if (withVideo.isNotEmpty() && !queueWasSet) {
                        queueWasSet = true
                        playerViewModel.setQueue(withVideo)
                    }
                }
            }
        }
    }

    private fun observeQueue() {
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

    // ---------------- UI ----------------

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
            } else if (lastTimerKey != key) {
                lastTimerKey = key
                pausedTimerSeconds = null
                startRestTimer(seconds)
            }
        } else {
            binding.progressLine.visibility = View.GONE
            stopTimerAndResetProgress()
            binding.tvMainInfo.text = repsFullText(current)
        }

        val next = state.list.getOrNull(state.index + 1)
        if (next != null) {
            binding.tvNext.text = ctx.getString(
                R.string.next_prefix,
                "${ctx.localizedExerciseTitle(next.title)} — " +
                        (if (isTimer(next)) next.rightInfo else repsFullText(next))
            )
            binding.btnNext.text = ctx.getString(R.string.action_next)
        } else {
            binding.tvNext.text = ""
            binding.btnNext.text = ctx.getString(R.string.finish_day_button)
        }
    }

    // ---------------- video ----------------

    private fun playCurrent() {
        val item = playerViewModel.current() ?: return
        val gs = item.videoUri ?: return

        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO) {
            runCatching {
                videoUrlResolver.resolve(gs)
            }.onSuccess { https ->
                launch(Dispatchers.Main) {
                    player?.apply {
                        setMediaItem(MediaItem.fromUri(https))
                        prepare()
                        playWhenReady = true
                    }
                }
            }.onFailure {
                launch(Dispatchers.Main) {
                    Toast.makeText(
                        requireContext(),
                        "Не удалось открыть видео",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    // ---------------- helpers ----------------

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

    private fun repsFullText(item: DayExerciseUi): String {
        val reps = repsFromRightInfo(item)
        return requireContext().resources.getQuantityString(
            R.plurals.repetitions,
            reps,
            reps
        )
    }

    private fun startRestTimer(totalSeconds: Int) {
        timer?.cancel()

        binding.progressLine.max = totalSeconds
        binding.progressLine.progress = totalSeconds
        binding.tvMainInfo.text = formatMmSs(totalSeconds)

        timer = object : CountDownTimer((totalSeconds + 1) * 1000L, 1000L) {
            override fun onTick(millisUntilFinished: Long) {
                val left = (millisUntilFinished / 1000L).toInt().coerceAtLeast(0)
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

    private fun formatMmSs(totalSeconds: Int): String =
        "%02d:%02d".format(totalSeconds / 60, totalSeconds % 60)

    private fun showExitDialog() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(getString(R.string.exit_workout_title))
            .setMessage(getString(R.string.exit_workout_message))
            .setNegativeButton(getString(R.string.stay)) { d, _ -> d.dismiss() }
            .setPositiveButton(getString(R.string.exit)) { _, _ ->
                findNavController().popBackStack(R.id.dayExercisesFragment, false)
            }
            .show()
    }

    private fun dp(value: Int): Int =
        (value * resources.displayMetrics.density).toInt()
}
