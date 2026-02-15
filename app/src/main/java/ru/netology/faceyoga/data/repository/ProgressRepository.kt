package ru.netology.faceyoga.data.repository

import kotlinx.coroutines.flow.Flow

interface ProgressRepository {

    // OLD (prefs/datastore)
    fun observeLastCompletedDay(): Flow<Int>
    suspend fun getLastCompletedDay(): Int
    suspend fun setLastCompletedDay(day: Int)

    /**
     * Нужно для навигации из Progress в конкретный день:
     * по programId + dayNumber получить programDayId (id строки в program_days)
     */
    suspend fun getProgramDayIdByDayNumber(programId: Long, dayNumber: Int): Long

    // NEW (Room)
    fun observeMaxCompletedDay(programId: Long): Flow<Int>
    fun observeCompletedDaysCount(programId: Long): Flow<Int>
    fun observeCompletedExercisesCount(programId: Long): Flow<Int>

    // NEW (prefs reset, чтобы старый прогресс не торчал после сброса)
    suspend fun resetLocalProgress()
}
