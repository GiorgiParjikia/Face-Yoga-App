package ru.netology.faceyoga.data.db

import ru.netology.faceyoga.data.model.ExerciseType
import ru.netology.faceyoga.data.model.Zone

data class ProgramDayRow(
    val programDayId: Long,
    val dayNumber: Int,
    val title: String?,
    val exercisesCount: Int,
    val doneCount: Int,
    val isCompleted: Boolean
)

data class DayExerciseWithExercise(
    val linkId: Long,
    val order: Int,
    val overrideReps: Int?,
    val overrideSeconds: Int?,

    val exerciseId: Long,
    val title: String,
    val zone: Zone,
    val description: String,
    val type: ExerciseType,

    val defaultReps: Int?,
    val defaultSeconds: Int?,
    val level: Int?,
    val videoUri: String?,
    val previewImageUri: String?,

    // NEW
    val requiresItem: Boolean,
    val requiredItemKey: String?
)
