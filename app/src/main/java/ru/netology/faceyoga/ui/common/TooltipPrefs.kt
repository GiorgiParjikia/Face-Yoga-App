package ru.netology.faceyoga.ui.common

import android.content.Context

object TooltipPrefs {

    private const val PREFS_NAME = "tooltip_prefs"
    private const val KEY_HOLD_PREVIEW_SHOWN = "hold_preview_shown"

    fun isHoldPreviewShown(context: Context): Boolean {
        return context
            .getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .getBoolean(KEY_HOLD_PREVIEW_SHOWN, false)
    }

    fun markHoldPreviewShown(context: Context) {
        context
            .getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .edit()
            .putBoolean(KEY_HOLD_PREVIEW_SHOWN, true)
            .apply()
    }
}
