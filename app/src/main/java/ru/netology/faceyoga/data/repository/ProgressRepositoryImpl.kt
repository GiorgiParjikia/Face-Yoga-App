package ru.netology.faceyoga.data.repository

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import ru.netology.faceyoga.data.db.ProgramDayDao
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProgressRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val programDayDao: ProgramDayDao
) : ProgressRepository {

    private val prefs by lazy {
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }

    private val _lastDayFlow = MutableStateFlow(
        prefs.getInt(KEY_LAST_COMPLETED_DAY, 0)
    )

    override fun observeLastCompletedDay(): Flow<Int> = _lastDayFlow.asStateFlow()

    override suspend fun getLastCompletedDay(): Int = _lastDayFlow.value

    override suspend fun getProgramDayIdByDayNumber(programId: Long, dayNumber: Int): Long {
        // У тебя в ProgramDayDao уже есть:
        // suspend fun getId(programId: Long, dayNumber: Int): Long?
        return programDayDao.getId(programId, dayNumber) ?: 0L
    }

    override suspend fun setLastCompletedDay(day: Int) {
        val current = _lastDayFlow.value
        if (day > current) {
            prefs.edit()
                .putInt(KEY_LAST_COMPLETED_DAY, day)
                .apply()

            _lastDayFlow.value = day
        }
    }

    private companion object {
        const val PREFS_NAME = "faceyoga_progress"
        const val KEY_LAST_COMPLETED_DAY = "last_completed_day"
    }
}
