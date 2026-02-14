package ru.netology.faceyoga.data.repository

import kotlinx.coroutines.flow.Flow
import ru.netology.faceyoga.data.db.DayExerciseWithExercise
import ru.netology.faceyoga.data.db.ProgramDayRow

interface ProgramRepository {

    fun observeDays(programId: Long): Flow<List<ProgramDayRow>>

    fun observeDayExercises(programDayId: Long): Flow<List<DayExerciseWithExercise>>

    suspend fun getDefaultProgramId(): Long

    // ✅ NEW: для Progress — получить programDayId по номеру дня
    suspend fun getProgramDayIdByDayNumber(programId: Long, dayNumber: Int): Long
}
