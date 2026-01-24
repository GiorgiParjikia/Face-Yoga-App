package ru.netology.faceyoga.ui.common

import android.content.Context
import ru.netology.faceyoga.R

fun Context.localizedDayLevelTitle(dayNumber: Int): String =
    when (dayNumber) {
        in 1..10 -> getString(R.string.level_easy)
        in 11..20 -> getString(R.string.level_medium)
        else -> getString(R.string.level_hard)
    }
