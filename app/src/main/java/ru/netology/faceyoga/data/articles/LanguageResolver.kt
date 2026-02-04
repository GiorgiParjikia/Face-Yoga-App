package ru.netology.faceyoga.data.articles

import android.content.Context
import java.util.Locale

object LanguageResolver {

    fun currentLangKey(context: Context): String {
        val locale = context.resources.configuration.locales[0]
        return when (locale.language.lowercase(Locale.ROOT)) {
            "ru" -> "ru"
            "ka" -> "ka"
            "en" -> "en"
            else -> "en"
        }
    }
}
