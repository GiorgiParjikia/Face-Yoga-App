package ru.netology.faceyoga.ui.days

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import ru.netology.faceyoga.data.db.ProgressDao
import ru.netology.faceyoga.data.repository.ProgramRepository
import javax.inject.Inject
import kotlin.math.roundToInt

@HiltViewModel
class DaysViewModel @Inject constructor(
    private val repository: ProgramRepository,
    private val progressDao: ProgressDao
) : ViewModel() {

    private val programIdFlow = flow {
        emit(repository.getDefaultProgramId())
    }

    // ✅ NEW: есть ли вообще какой-то прогресс
    val hasProgress: StateFlow<Boolean> =
        programIdFlow
            .flatMapLatest { programId ->
                repository.observeDays(programId)
            }
            .map { rows ->
                rows.any { row ->
                    row.doneCount > 0 || row.isCompleted
                }
            }
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5_000),
                false
            )

    val days: StateFlow<List<DayUi>> =
        programIdFlow
            .flatMapLatest { programId ->
                repository.observeDays(programId)
            }
            .map { rows ->
                val sorted = rows.sortedBy { it.dayNumber }

                var prevCompleted = true

                sorted.map { row ->
                    val locked = row.dayNumber != 1 && !prevCompleted

                    val total = row.exercisesCount.coerceAtLeast(0)
                    val done = row.doneCount.coerceAtLeast(0)

                    val percent =
                        if (total <= 0) 0
                        else ((done * 100f) / total).roundToInt().coerceIn(0, 100)

                    val percentFinal = if (row.isCompleted) 100 else percent

                    val ui = DayUi(
                        programDayId = row.programDayId,
                        dayNumber = row.dayNumber,
                        title = row.title,
                        exercisesCount = row.exercisesCount,
                        isCompleted = row.isCompleted,
                        isLocked = locked,
                        progressPercent = percentFinal
                    )

                    prevCompleted = row.isCompleted
                    ui
                }
            }
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5_000),
                emptyList()
            )

    fun resetProgress() {
        viewModelScope.launch {
            val programId = repository.getDefaultProgramId()
            progressDao.resetProgressForProgram(programId)
        }
    }
}