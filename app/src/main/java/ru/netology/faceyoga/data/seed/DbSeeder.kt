package ru.netology.faceyoga.data.seed

import javax.inject.Inject
import javax.inject.Singleton
import ru.netology.faceyoga.data.db.*

@Singleton
class DbSeeder @Inject constructor(
    private val programDao: ProgramDao,
    private val programDayDao: ProgramDayDao,
    private val exerciseDao: ExerciseDao,
    private val dayExerciseDao: DayExerciseDao,
) {

    suspend fun seedIfNeeded(): Long {
        val programTitle = "Базовая программа 30 дней"

        // 1) Program
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

        // 2) Days
        val dayIds = (1..30).map { day ->
            val title = when (day) {
                in 1..10 -> "Easy"
                in 11..20 -> "Medium"
                else -> "Hard"
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

        // 3) Clear old links
        dayExerciseDao.deleteAllForProgram(programId)

        // 4) Seed exercises
        SeedExercises.exercises.forEach { ex ->
            exerciseDao.insert(ex).let { id ->
                if (id == -1L) exerciseDao.getIdByTitle(ex.title)!! else id
            }
        }

        // 5) Resolve exercise id (ДЛЯ ВСЕХ: Reps и Timer)
        suspend fun idByTitle(title: String): Long =
            exerciseDao.getIdByTitle(title)
                ?: error("Exercise not found: $title")

        // 6) Plans
        val planEasy = SeedPlansEasy.build()
        val planMedium = SeedPlansMedium.build()
        val planHard = SeedPlansHard.build()

        // 7) Insert day ↔ exercise with overrides
        dayIds.forEachIndexed { index, programDayId ->
            val dayNumber = index + 1

            val seeds = when (dayNumber) {
                in 1..10 -> planEasy[dayNumber].orEmpty()
                in 11..20 -> planMedium[dayNumber - 10].orEmpty()
                else -> planHard[dayNumber - 20].orEmpty()
            }

            val links = seeds.mapIndexed { i, seed ->
                when (seed) {

                    is DayExerciseSeed.Reps -> DayExerciseEntity(
                        programDayId = programDayId,
                        exerciseId = idByTitle(seed.title),
                        order = i + 1,
                        overrideReps = seed.reps,
                        overrideSeconds = null
                    )

                    is DayExerciseSeed.Timer -> DayExerciseEntity(
                        programDayId = programDayId,
                        exerciseId = idByTitle(seed.title), // ✅ FIX: Timer тоже Exercise
                        order = i + 1,
                        overrideReps = null,
                        overrideSeconds = seed.seconds
                    )
                }
            }

            dayExerciseDao.insertAll(links)
        }

        return programId
    }
}
