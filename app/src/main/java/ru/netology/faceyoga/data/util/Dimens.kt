package ru.netology.faceyoga.ui.common

import android.content.res.Resources
import android.util.TypedValue

/**
 * Перевод dp -> px.
 * Используй: 8.dp(resources) или 8f.dp(resources)
 */
fun Int.dp(res: Resources): Int =
    TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        this.toFloat(),
        res.displayMetrics
    ).toInt()

fun Float.dp(res: Resources): Float =
    TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        this,
        res.displayMetrics
    )