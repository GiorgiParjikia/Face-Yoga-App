package ru.netology.faceyoga.ui.common

import android.content.Context
import ru.netology.faceyoga.R
import ru.netology.faceyoga.data.model.ExerciseType
import ru.netology.faceyoga.data.model.Zone

fun Context.localizedZone(zone: Zone): String = getString(
    when (zone) {
        Zone.FOREHEAD -> R.string.zone_forehead
        Zone.EYES -> R.string.zone_eyes
        Zone.CHEEKS -> R.string.zone_cheeks
        Zone.LIPS -> R.string.zone_lips
        Zone.JAWLINE_CHIN -> R.string.zone_jawline_chin
        Zone.NECK -> R.string.zone_neck
        Zone.FULL_FACE -> R.string.zone_full_face
    }
)

fun Context.localizedExerciseType(type: ExerciseType): String = getString(
    when (type) {
        ExerciseType.REPS -> R.string.type_reps
        ExerciseType.TIMER -> R.string.type_timer
    }
)
