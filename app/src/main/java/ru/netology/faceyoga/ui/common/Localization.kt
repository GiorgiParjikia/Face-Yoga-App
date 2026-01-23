package ru.netology.faceyoga.ui.common

import android.content.Context
import java.util.Locale
import java.util.concurrent.ConcurrentHashMap

private val titleCache = ConcurrentHashMap<String, Int>()

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
