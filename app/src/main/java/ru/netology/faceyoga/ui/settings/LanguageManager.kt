package ru.netology.faceyoga.ui.settings

import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.edit
import androidx.core.os.LocaleListCompat

object LanguageManager {
    private const val PREFS = "faceyoga_settings"
    private const val KEY_LANG = "lang" // system | en | ru | ka

    fun getSelectedLang(context: Context): String {
        val prefs = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
        return prefs.getString(KEY_LANG, "system") ?: "system"
    }

    fun setSelectedLang(context: Context, lang: String) {
        context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
            .edit {
                putString(KEY_LANG, lang)
            }

        apply(lang)
    }

    fun apply(lang: String) {
        val locales = when (lang) {
            "en" -> LocaleListCompat.forLanguageTags("en")
            "ru" -> LocaleListCompat.forLanguageTags("ru")
            "ka" -> LocaleListCompat.forLanguageTags("ka")
            else -> LocaleListCompat.getEmptyLocaleList() // system
        }
        AppCompatDelegate.setApplicationLocales(locales)
    }
}