package ru.netology.faceyoga.data.repository

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProgressRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : ProgressRepository {

    private val prefs by lazy {
        context.getSharedPreferences("faceyoga_progress", Context.MODE_PRIVATE)
    }

    override suspend fun getLastCompletedDay(): Int {
        return prefs.getInt(KEY_LAST_COMPLETED_DAY, 0)
    }

    override suspend fun setLastCompletedDay(day: Int) {
        val current = prefs.getInt(KEY_LAST_COMPLETED_DAY, 0)
        if (day > current) {
            prefs.edit().putInt(KEY_LAST_COMPLETED_DAY, day).apply()
        }
    }

    private companion object {
        const val KEY_LAST_COMPLETED_DAY = "last_completed_day"
    }
}