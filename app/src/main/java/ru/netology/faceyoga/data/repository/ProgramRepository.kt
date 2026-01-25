package ru.netology.faceyoga.data.repository

import kotlinx.coroutines.flow.Flow
import ru.netology.faceyoga.data.db.DayExerciseWithExercise
import ru.netology.faceyoga.data.db.ProgramDayRow

interface ProgramRepository {
    fun observeDays(programId: Long): Flow<List<ProgramDayRow>>
    fun observeDayExercises(programDayId: Long): Flow<List<DayExerciseWithExercise>>

    /**
     * Гарантирует, что есть программа в БД и возвращает её id.
     */
    suspend fun getDefaultProgramId(): Long

    /**
     * NEW: по programDayId получить programId (нужно для сохранения прогресса в конце дня)
     */
    suspend fun getProgramIdByProgramDayId(programDayId: Long): Long
}
