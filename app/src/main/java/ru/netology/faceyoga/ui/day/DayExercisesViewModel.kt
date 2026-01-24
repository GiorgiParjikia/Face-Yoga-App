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
import javax.inject.Inject

@HiltViewModel
class DayExercisesViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    repository: ProgramRepository,
    private val imageUrlResolver: ru.netology.faceyoga.data.media.ImageUrlResolver
) : ViewModel() {

    private val programDayId: Long = savedStateHandle["programDayId"] ?: 0L

    val exercises: StateFlow<List<DayExerciseUi>> =
        repository.observeDayExercises(programDayId)
            .map { list ->
                list.map { row ->
                    DayExerciseUi(
                        id = row.exerciseId,
                        title = row.title,
                        zone = row.zone,
                        type = row.type,
                        rightInfo = formatValue(
                            reps = row.overrideReps ?: row.defaultReps,
                            seconds = row.overrideSeconds ?: row.defaultSeconds,
                            type = row.type
                        ),
                        videoUri = row.videoUri,
                        previewImageUri = row.previewImageUri,

                        // NEW: предметы
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

    /**
     * NEW: уникальные предметы дня — для инфо-блока сверху списка
     * (например ["pencil"])
     */
    val requiredItemKeys: StateFlow<List<String>> =
        exercises
            .map { list ->
                list.mapNotNull { it.requiredItemKey }
                    .distinct()
            }
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5_000),
                emptyList()
            )

    private fun formatValue(
        reps: Int?,
        seconds: Int?,
        type: ExerciseType
    ): String =
        when (type) {
            ExerciseType.REPS -> "x${reps ?: 0}"
            ExerciseType.TIMER -> secondsToMmSs(seconds ?: 0)
        }

    private fun secondsToMmSs(total: Int): String {
        val m = total / 60
        val s = total % 60
        return "%02d:%02d".format(m, s)
    }
}