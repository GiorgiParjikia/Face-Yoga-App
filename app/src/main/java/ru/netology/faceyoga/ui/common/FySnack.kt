package ru.netology.faceyoga.ui.common

import android.view.View
import com.google.android.material.snackbar.Snackbar
import ru.netology.faceyoga.R

object FySnack {

    /**
     * Единый snackbar в стиле приложения.
     * anchor — чтобы уведомление было над bottom nav.
     */
    fun show(
        rootView: View,
        message: String,
        anchor: View? = null,
        duration: Int = Snackbar.LENGTH_SHORT
    ) {
        val sb = Snackbar.make(rootView, message, duration)

        if (anchor != null) sb.anchorView = anchor

        // Стиль как на твоих "черных" уведомлениях
        sb.setBackgroundTint(rootView.context.getColor(R.color.snackbar_bg))
        sb.setTextColor(rootView.context.getColor(R.color.snackbar_text))

        sb.show()
    }
}
