package ru.netology.faceyoga.ui.day

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import ru.netology.faceyoga.data.model.ExerciseType
import ru.netology.faceyoga.data.repository.ProgramRepository
import ru.netology.faceyoga.ui.common.StateKeys
import javax.inject.Inject

@HiltViewModel
class DayExercisesViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    repository: ProgramRepository
) : ViewModel() {

    private val programDayId: Long =
        savedStateHandle[StateKeys.PROGRAM_DAY_ID] ?: 0L

    val exercises: StateFlow<List<DayExerciseUi>> =
        repository.observeDayExercises(programDayId)
            .map { list ->
                list.map { row ->
                    DayExerciseUi(
                        id = row.exerciseId,
                        title = row.title,
                        zone = row.zone,
                        type = row.type,
                        rightInfo = shortValue(
                            reps = row.overrideReps ?: row.defaultReps,
                            seconds = row.overrideSeconds ?: row.defaultSeconds,
                            type = row.type
                        ),
                        videoUri = row.videoUri,
                        previewImageUri = row.previewImageUri,
                        requiresItem = row.requiresItem,
                        requiredItemKey = row.requiredItemKey
                    )
                }
            }
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5_000),
                emptyList()
            )

    val requiredItemKeys: StateFlow<List<String>> =
        exercises
            .map { list -> list.mapNotNull { it.requiredItemKey }.distinct() }
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5_000),
                emptyList()
            )

    // ✅ ДЛЯ СПИСКА: коротко
    private fun shortValue(
        reps: Int?,
        seconds: Int?,
        type: ExerciseType
    ): String =
        when (type) {
            ExerciseType.REPS -> "×${reps ?: 0}"
            ExerciseType.TIMER -> secondsToMmSs(seconds ?: 0)
        }

    private fun secondsToMmSs(total: Int): String {
        val m = total / 60
        val s = total % 60
        return "%02d:%02d".format(m, s)
    }
}
