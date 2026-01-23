package ru.netology.faceyoga.ui.day

import ru.netology.faceyoga.data.model.ExerciseType
import ru.netology.faceyoga.data.model.Zone

data class DayExerciseUi(
    val id: Long,
    val title: String,
    val zone: Zone,
    val type: ExerciseType,
    val rightInfo: String,
    val videoUri: String?,
    val previewImageUri: String?
)
