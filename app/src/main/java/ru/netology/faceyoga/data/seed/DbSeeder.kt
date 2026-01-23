package ru.netology.faceyoga.data.seed

import javax.inject.Inject
import javax.inject.Singleton
import ru.netology.faceyoga.data.db.DayExerciseDao
import ru.netology.faceyoga.data.db.DayExerciseEntity
import ru.netology.faceyoga.data.db.ExerciseDao
import ru.netology.faceyoga.data.db.ProgramDao
import ru.netology.faceyoga.data.db.ProgramDayDao
import ru.netology.faceyoga.data.db.ProgramDayEntity
import ru.netology.faceyoga.data.db.ProgramEntity

@Singleton
class DbSeeder @Inject constructor(
    private val programDao: ProgramDao,
    private val programDayDao: ProgramDayDao,
    private val exerciseDao: ExerciseDao,
    private val dayExerciseDao: DayExerciseDao,
) {

    suspend fun seedIfNeeded(): Long {
        val programTitle = "Базовая программа 30 дней"

        // 1) Программа (создать или получить id)
        val programId = programDao.insert(
            ProgramEntity(
                title = programTitle,
                description = "Стартовая программа для MVP",
                durationDays = 30,
                level = 1
            )
        ).let { id ->
            if (id == -1L) programDao.getIdByTitle(programTitle)!! else id
        }

        // 2) Дни 1..30 (создать или получить id)
        val dayIds: List<Long> = (1..30).map { day ->
            val title: String? = when (day) {
                in 1..10 -> "Easy $day"
                in 11..20 -> "Medium ${day - 10}"
                in 21..30 -> "Hard ${day - 20}"
                else -> null
            }

            programDayDao.insert(
                ProgramDayEntity(
                    programId = programId,
                    dayNumber = day,
                    title = title
                )
            ).let { id ->
                if (id == -1L) programDayDao.getId(programId, day)!! else id
            }
        }

        // 3) Чистим связи, чтобы пересидинг реально перезаписывал порядок
        dayExerciseDao.deleteAllForProgram(programId)

        // 4) Сидим упражнения (берём из SeedExercises)
        SeedExercises.exercises.forEach { ex ->
            exerciseDao.insert(ex).let { id ->
                if (id == -1L) exerciseDao.getIdByTitle(ex.title)!! else id
            }
        }

        // 5) Резолвим id упражнений по EN title
        suspend fun idEn(title: String): Long =
            exerciseDao.getIdByTitle(title)
                ?: error("Exercise not found by EN title: $title")

        val relaxId = idEn("Relax")

        // 6) Строим планы (берём из SeedPlans*)
        val planEasy = SeedPlansEasy.build(::idEn, relaxId)
        val planMedium = SeedPlansMedium.build(::idEn, relaxId)
        val planHard = SeedPlansHard.build(::idEn, relaxId)


        // 7) Вставка связок: 1–10 Easy, 11–20 Medium, 21–30 Hard
        dayIds.forEachIndexed { index, programDayId ->
            val dayNumber = index + 1

            val ordered: List<Long> = when (dayNumber) {
                in 1..10 -> planEasy[dayNumber].orEmpty()
                in 11..20 -> planMedium[dayNumber - 10].orEmpty()
                in 21..30 -> planHard[dayNumber - 20].orEmpty()
                else -> emptyList()
            }

            val links = ordered.mapIndexed { i, exId ->
                DayExerciseEntity(
                    programDayId = programDayId,
                    exerciseId = exId,
                    order = i + 1
                )
            }

            dayExerciseDao.insertAll(links)
        }

        return programId
    }
}