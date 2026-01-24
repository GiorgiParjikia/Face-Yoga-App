package ru.netology.faceyoga.ui.common

import android.content.Context
import ru.netology.faceyoga.R
import java.util.Locale
import java.util.concurrent.ConcurrentHashMap

private val titleCache = ConcurrentHashMap<String, Int>()

/**
 * Локализация названий упражнений по ключу (ex_*)
 */
fun Context.localizedExerciseTitle(idEn: String): String {
    val resName = "ex_" + idEn
        .trim()
        .lowercase(Locale.US)
        .replace("&", " and ")
        .replace(Regex("[^a-z0-9]+"), "_")
        .trim('_')

    val resId = titleCache.getOrPut(resName) {
        resources.getIdentifier(resName, "string", packageName)
    }

    return if (resId != 0) getString(resId) else idEn
}

/**
 * Локализация предметов, необходимых для упражнений
 * (карандаш, мячик и т.д.)
 */
fun Context.localizedItemName(key: String?): String {
    return when (key) {
        "pencil" -> getString(R.string.item_pencil)
        else -> ""
    }
}