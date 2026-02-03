package ru.netology.faceyoga.ui.day

import android.content.res.Resources
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.core.view.updateLayoutParams
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import ru.netology.faceyoga.data.media.VideoUrlResolver
import ru.netology.faceyoga.databinding.ViewVideoPreviewOverlayBinding

class VideoPreviewController(
    private val parent: ViewGroup,
    private val scope: LifecycleCoroutineScope,
    private val videoUrlResolver: VideoUrlResolver
) {

    private var _binding: ViewVideoPreviewOverlayBinding? = null
    private val binding get() = _binding!!

    private var player: ExoPlayer? = null
    private var resolveJob: Job? = null
    private var isShown = false
    private var lastVideoUri: String? = null

    // -------- public API --------

    fun show(videoUri: String) {
        if (isShown && lastVideoUri == videoUri) return

        ensureOverlay()
        isShown = true
        lastVideoUri = videoUri

        // 👉 ОПУСКАЕМ КАРТОЧКУ НИЖЕ
        binding.videoCard.updateLayoutParams<ViewGroup.MarginLayoutParams> {
            topMargin = 56.dp
        }

        binding.previewOverlayRoot.isVisible = true
        binding.loading.isVisible = true

        preparePlayer(videoUri)
    }

    fun hide() {
        if (!isShown) return

        isShown = false
        lastVideoUri = null

        resolveJob?.cancel()
        resolveJob = null

        binding.previewOverlayRoot.isVisible = false
        releasePlayer()
    }

    fun release() {
        hide()
        _binding = null
    }

    // -------- internals --------

    private fun ensureOverlay() {
        if (_binding != null) return

        val inflater = LayoutInflater.from(parent.context)
        _binding = ViewVideoPreviewOverlayBinding.inflate(inflater, parent, false)
        parent.addView(binding.root)

        binding.previewOverlayRoot.isVisible = false

        // перехватываем тачи, чтобы не протекали вниз
        binding.previewOverlayRoot.setOnTouchListener { _, _ -> true }
        binding.dim.setOnTouchListener { _, _ -> true }
    }

    private fun preparePlayer(gsUri: String) {
        releasePlayer()

        player = ExoPlayer.Builder(parent.context).build().also { exo ->
            exo.repeatMode = Player.REPEAT_MODE_ONE
            binding.playerView.player = exo

            exo.addListener(object : Player.Listener {
                override fun onPlaybackStateChanged(state: Int) {
                    when (state) {
                        Player.STATE_BUFFERING ->
                            binding.loading.isVisible = true
                        Player.STATE_READY ->
                            binding.loading.isVisible = false
                    }
                }
            })
        }

        resolveJob = scope.launch(Dispatchers.IO) {
            runCatching {
                videoUrlResolver.resolve(gsUri)
            }.onSuccess { https ->
                launch(Dispatchers.Main) {
                    if (!isShown) return@launch

                    player?.apply {
                        setMediaItem(MediaItem.fromUri(https))
                        prepare()
                        playWhenReady = true
                    }
                }
            }.onFailure {
                launch(Dispatchers.Main) {
                    binding.loading.isVisible = false
                }
            }
        }
    }

    private fun releasePlayer() {
        binding.playerView.player = null
        player?.release()
        player = null
    }
}

/* ---------- helpers ---------- */

private val Int.dp: Int
    get() = (this * Resources.getSystem().displayMetrics.density).toInt()
