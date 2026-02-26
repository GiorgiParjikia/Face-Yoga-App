package ru.netology.faceyoga.ui.player

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
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
import androidx.media3.common.PlaybackException
import androidx.media3.exoplayer.ExoPlayer
import androidx.navigation.fragment.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.crashlytics.FirebaseCrashlytics
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ru.netology.faceyoga.R
import ru.netology.faceyoga.data.media.VideoUrlResolver
import ru.netology.faceyoga.databinding.FragmentVideoPlayerBinding
import ru.netology.faceyoga.ui.common.StateKeys
import ru.netology.faceyoga.ui.common.dp
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

    // offline / retry
    private var lastResolvedHttps: String? = null

    // засчет упражнения только при реальном проигрывании
    private var completeJob: Job? = null

    // таймаут ожидания READY
    private var readyTimeoutJob: Job? = null

    // ✅ Crashlytics
    private val crash by lazy { FirebaseCrashlytics.getInstance() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        _binding = FragmentVideoPlayerBinding.bind(view)

        // ✅ Crashlytics: базовые keys + breadcrumb
        crash.setCustomKey("day_number", dayNumber)
        crash.setCustomKey("program_day_id", programDayId)
        crash.log("player_open day=$dayNumber programDayId=$programDayId")

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { _, insets ->
            val navBarBottom = insets.getInsets(WindowInsetsCompat.Type.navigationBars()).bottom
            binding.bottomContainer.setPadding(
                binding.bottomContainer.paddingLeft,
                binding.bottomContainer.paddingTop,
                binding.bottomContainer.paddingRight,
                navBarBottom + 8.dp(resources)
            )
            insets
        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            crash.log("player_back_pressed day=$dayNumber")
            showExitDialog()
        }
        binding.btnClose.setOnClickListener {
            crash.log("player_close_clicked day=$dayNumber")
            showExitDialog()
        }

        binding.btnRetry.setOnClickListener {
            crash.log("player_retry_clicked day=$dayNumber")
            binding.offlineOverlay.visibility = View.GONE
            playCurrent(forceResolve = true)
        }

        setupPlayer()
        setupButtons()
        observeDayExercises()
        observeQueue()
    }

    override fun onStart() {
        super.onStart()
        crash.log("player_onStart day=$dayNumber")
        player?.playWhenReady = true
        player?.play()
    }

    override fun onStop() {
        super.onStop()
        crash.log("player_onStop day=$dayNumber")

        pausedTimerSeconds =
            if (binding.progressLine.visibility == View.VISIBLE)
                binding.progressLine.progress.takeIf { it > 0 }
            else null

        timer?.cancel()
        timer = null

        completeJob?.cancel()
        completeJob = null

        readyTimeoutJob?.cancel()
        readyTimeoutJob = null

        player?.pause()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        crash.log("player_onDestroyView day=$dayNumber")

        readyTimeoutJob?.cancel()
        readyTimeoutJob = null

        completeJob?.cancel()
        completeJob = null

        _binding = null
        player?.release()
        player = null
    }

    private fun setupPlayer() {
        player = ExoPlayer.Builder(requireContext()).build().also { exo ->
            exo.repeatMode = Player.REPEAT_MODE_ONE
            binding.playerView.player = exo

            exo.addListener(object : Player.Listener {

                override fun onPlaybackStateChanged(state: Int) {
                    when (state) {
                        Player.STATE_BUFFERING -> {
                            crash.log("player_state=BUFFERING day=$dayNumber")
                            // BUFFERING может быть бесконечным при отсутствии сети → мы держим таймаут
                            binding.loadingOverlay.visibility = View.VISIBLE
                        }
                        Player.STATE_READY -> {
                            crash.log("player_state=READY day=$dayNumber")
                            // Видео реально подготовилось
                            binding.loadingOverlay.visibility = View.GONE
                            binding.offlineOverlay.visibility = View.GONE

                            readyTimeoutJob?.cancel()
                            readyTimeoutJob = null

                            maybeScheduleCompleteAfterRealPlayback()
                        }
                        Player.STATE_ENDED -> {
                            // не используем, repeat one
                        }
                        Player.STATE_IDLE -> {
                            // ничего
                        }
                    }
                }

                override fun onIsPlayingChanged(isPlaying: Boolean) {
                    crash.log("player_isPlaying=$isPlaying day=$dayNumber")
                    if (isPlaying) {
                        maybeScheduleCompleteAfterRealPlayback()
                    } else {
                        completeJob?.cancel()
                        completeJob = null
                    }
                }

                override fun onPlayerError(error: PlaybackException) {
                    crash.log("player_error day=$dayNumber code=${error.errorCodeName}")
                    crash.recordException(error)
                    showOffline()
                }
            })
        }
    }

    private fun setupButtons() {
        binding.btnNext.setOnClickListener {
            val state = lastQueueState
            val idx = state?.index ?: -1
            crash.setCustomKey("exercise_index", idx)
            crash.log("next_clicked day=$dayNumber index=$idx completed=${playerViewModel.isCurrentCompleted()}")

            if (!playerViewModel.isCurrentCompleted()) {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.watch_a_bit_to_continue),
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            val s = state ?: return@setOnClickListener
            val hasNext = s.index + 1 < s.list.size

            if (hasNext) {
                pausedTimerSeconds = null
                playerViewModel.next()
            } else {
                crash.log("finish_day_clicked day=$dayNumber")
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

    private fun observeDayExercises() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                dayViewModel.exercises.collect { list ->
                    if (list.isEmpty()) return@collect

                    val withVideo = list.filter { !it.videoUri.isNullOrBlank() }
                    if (withVideo.isNotEmpty() && !queueWasSet) {
                        crash.log("queue_set day=$dayNumber size=${withVideo.size}")
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

                    // ✅ Crashlytics: exercise keys + breadcrumb
                    val current = state.current
                    if (current != null) {
                        val title = runCatching { requireContext().localizedExerciseTitle(current.title) }
                            .getOrElse { current.title }
                        crash.setCustomKey("exercise_index", state.index)
                        crash.setCustomKey("exercise_title", title)
                        crash.log("exercise_change day=$dayNumber index=${state.index} title=$title")
                    } else {
                        crash.log("exercise_change day=$dayNumber index=${state.index} title=null")
                    }

                    // сменилось упражнение
                    lastResolvedHttps = null
                    completeJob?.cancel(); completeJob = null
                    readyTimeoutJob?.cancel(); readyTimeoutJob = null

                    updateOverlay(state)
                    setNextEnabled(playerViewModel.isCurrentCompleted())

                    playCurrent(forceResolve = false)
                }
            }
        }
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

    private fun setNextEnabled(enabled: Boolean) {
        binding.btnNext.isEnabled = enabled
        binding.btnNext.alpha = if (enabled) 1f else 0.55f
    }

    private fun playCurrent(forceResolve: Boolean) {
        val item = playerViewModel.current() ?: return
        val gs = item.videoUri ?: return

        crash.log("play_current day=$dayNumber forceResolve=$forceResolve")

        // ✅ 1) если нет сети — сразу оффлайн, без вечного лоадера
        if (!isOnline(requireContext())) {
            crash.log("offline_no_network day=$dayNumber")
            showOffline()
            return
        }

        binding.offlineOverlay.visibility = View.GONE
        binding.loadingOverlay.visibility = View.VISIBLE
        setNextEnabled(playerViewModel.isCurrentCompleted())

        // ✅ 2) таймаут: если READY не наступит — покажем оффлайн
        startReadyTimeout()

        val cached = lastResolvedHttps
        if (!forceResolve && !cached.isNullOrBlank()) {
            crash.log("play_cached_https day=$dayNumber")
            playHttps(cached)
            return
        }

        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO) {
            runCatching {
                videoUrlResolver.resolve(gs)
            }.onSuccess { https ->
                lastResolvedHttps = https
                launch(Dispatchers.Main) {
                    crash.log("resolve_ok day=$dayNumber")
                    playHttps(https)
                }
            }.onFailure { e ->
                launch(Dispatchers.Main) {
                    crash.log("resolve_fail day=$dayNumber")
                    crash.recordException(e)
                    showOffline()
                }
            }
        }
    }

    private fun playHttps(https: String) {
        player?.apply {
            setMediaItem(MediaItem.fromUri(https))
            prepare()
            playWhenReady = true
        }
    }

    private fun startReadyTimeout() {
        readyTimeoutJob?.cancel()
        readyTimeoutJob = viewLifecycleOwner.lifecycleScope.launch {
            delay(8000) // 8 сек хватает
            val p = player
            // если за 8 сек не вышли в READY — считаем, что сети/доступа нет
            if (p == null || p.playbackState != Player.STATE_READY) {
                crash.log("ready_timeout_show_offline day=$dayNumber")
                showOffline()
            }
        }
    }

    private fun showOffline() {
        readyTimeoutJob?.cancel()
        readyTimeoutJob = null

        binding.loadingOverlay.visibility = View.GONE
        binding.offlineOverlay.visibility = View.VISIBLE
        setNextEnabled(false)

        crash.log("offline_shown day=$dayNumber")

        // на всякий случай остановим попытки воспроизведения
        player?.pause()
    }

    private fun maybeScheduleCompleteAfterRealPlayback() {
        if (playerViewModel.isCurrentCompleted()) {
            setNextEnabled(true)
            return
        }

        if (binding.offlineOverlay.visibility == View.VISIBLE) {
            setNextEnabled(false)
            return
        }

        val exo = player ?: return
        if (!exo.isPlaying) {
            setNextEnabled(false)
            return
        }

        if (completeJob?.isActive == true) return

        completeJob = viewLifecycleOwner.lifecycleScope.launch {
            delay(1500)

            val p = player ?: return@launch
            val reallyPlaying =
                p.isPlaying &&
                        p.currentPosition > 0L &&
                        p.playbackState == Player.STATE_READY

            if (reallyPlaying) {
                crash.log("mark_completed day=$dayNumber")
                playerViewModel.markCurrentCompleted()
                setNextEnabled(true)
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

        crash.log("timer_start day=$dayNumber seconds=$totalSeconds")

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
                crash.log("timer_finish_auto_next day=$dayNumber")
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
                crash.log("exit_confirmed day=$dayNumber")
                findNavController().popBackStack(R.id.dayExercisesFragment, false)
            }
            .show()
    }

    private fun isOnline(context: Context): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = cm.activeNetwork ?: return false
        val caps = cm.getNetworkCapabilities(network) ?: return false
        // достаточно, чтобы была возможность в интернет (WIFI/CELL)
        return caps.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }
}