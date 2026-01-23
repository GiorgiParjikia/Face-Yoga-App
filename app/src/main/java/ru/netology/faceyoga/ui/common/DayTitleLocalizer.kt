package ru.netology.faceyoga.ui.common

import android.content.Context
import ru.netology.faceyoga.R

fun Context.localizedDayLevelTitle(dayNumber: Int): String {
    val (levelRes, insideLevelDay) = when (dayNumber) {
        in 1..10 -> R.string.level_easy to dayNumber
        in 11..20 -> R.string.level_medium to (dayNumber - 10)
        else -> R.string.level_hard to (dayNumber - 20)
    }
    val level = getString(levelRes)
    return getString(R.string.day_level_title, level, insideLevelDay)
}
