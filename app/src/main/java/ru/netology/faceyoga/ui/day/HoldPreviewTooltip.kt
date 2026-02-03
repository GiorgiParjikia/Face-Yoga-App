package ru.netology.faceyoga.ui.day

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.lifecycle.LifecycleCoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ru.netology.faceyoga.databinding.ViewHoldPreviewTooltipBinding
import ru.netology.faceyoga.ui.common.TooltipPrefs

class HoldPreviewTooltip(
    private val parent: ViewGroup,
    private val anchor: View, // 👈 toolbar или любой якорь
    private val scope: LifecycleCoroutineScope
) {

    private var binding: ViewHoldPreviewTooltipBinding? = null

    fun showIfNeeded() {
        val context = parent.context
        if (TooltipPrefs.isHoldPreviewShown(context)) return

        show()
        TooltipPrefs.markHoldPreviewShown(context)
    }

    private fun show() {
        if (binding != null) return

        val inflater = LayoutInflater.from(parent.context)
        binding = ViewHoldPreviewTooltipBinding.inflate(inflater, parent, false)

        parent.addView(binding!!.root)

        binding!!.root.apply {
            isVisible = true
            alpha = 0f
        }

        // ⚠️ Ждём, пока layout посчитается
        binding!!.root.post {
            val y = anchor.bottom + dp(8)
            binding!!.root.translationY = y.toFloat()

            binding!!.root.animate()
                .alpha(1f)
                .setDuration(250)
                .start()
        }

        scope.launch {
            delay(8_000)
            hide()
        }
    }

    private fun hide() {
        binding?.let {
            parent.removeView(it.root)
        }
        binding = null
    }

    private fun dp(value: Int): Int =
        (value * parent.resources.displayMetrics.density).toInt()
}
