package ru.netology.faceyoga.data.repository

import kotlinx.coroutines.flow.Flow
import ru.netology.faceyoga.data.db.DayExerciseDao
import ru.netology.faceyoga.data.db.DayExerciseWithExercise
import ru.netology.faceyoga.data.db.ProgramDao
import ru.netology.faceyoga.data.db.ProgramDayDao
import ru.netology.faceyoga.data.db.ProgramDayRow
import ru.netology.faceyoga.data.seed.DbSeeder
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProgramRepositoryImpl @Inject constructor(
    private val programDao: ProgramDao,
    private val programDayDao: ProgramDayDao,
    private val dayExerciseDao: DayExerciseDao,
    private val seeder: DbSeeder
) : ProgramRepository {

    override fun observeDays(programId: Long): Flow<List<ProgramDayRow>> =
        programDayDao.observeDays(programId)

    override fun observeDayExercises(programDayId: Long): Flow<List<DayExerciseWithExercise>> =
        dayExerciseDao.observeDayExercises(programDayId)

    override suspend fun getDefaultProgramId(): Long {
        // 1) пытаемся найти уже сидированную программу по title
        val existingId = programDao.getIdByTitle("Базовая программа 30 дней")
        if (existingId != null) return existingId

        // 2) если нет — сидим
        return seeder.seedIfNeeded()
    }

    override suspend fun getProgramDayIdByDayNumber(programId: Long, dayNumber: Int): Long {
        return programDayDao.getId(programId, dayNumber) ?: 0L
    }

}