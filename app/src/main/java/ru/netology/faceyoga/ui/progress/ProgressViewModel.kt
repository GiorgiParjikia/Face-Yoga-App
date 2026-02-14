package ru.netology.faceyoga.ui.progress

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.netology.faceyoga.data.repository.ProgramRepository
import ru.netology.faceyoga.data.repository.ProgressRepository
import javax.inject.Inject
import kotlin.math.roundToInt

@HiltViewModel
class ProgressViewModel @Inject constructor(
    private val progressRepo: ProgressRepository,
    private val programRepo: ProgramRepository
) : ViewModel() {

    private val _ui = MutableStateFlow(
        ProgressUi(doneDays = 0, totalDays = 30, percent = 0, streak = 0, days = emptyList())
    )
    val ui: StateFlow<ProgressUi> = _ui.asStateFlow()

    fun start() {
        viewModelScope.launch {
            progressRepo.observeLastCompletedDay().collect { last ->
                _ui.value = buildUi(lastCompletedDay = last)
            }
        }
    }

    /**
     * Для клика по дню в Progress:
     * находим programDayId по номеру дня.
     */
    suspend fun resolveProgramDayId(dayNumber: Int): Long {
        val programId = programRepo.getDefaultProgramId()
        return programRepo.getProgramDayIdByDayNumber(programId, dayNumber)
    }

    private fun buildUi(lastCompletedDay: Int): ProgressUi {
        val total = 30
        val done = lastCompletedDay.coerceIn(0, total)
        val percent = if (total == 0) 0 else ((done.toFloat() / total) * 100f).roundToInt()

        val days = (1..total).map { day ->
            val state = when {
                day <= done -> DayState.DONE
                day == done + 1 -> DayState.AVAILABLE
                else -> DayState.LOCKED
            }
            ProgressDayUi(day = day, state = state)
        }

        // Без истории мы не можем честно посчитать streak.
        // Поэтому делаем "условный": если дошёл до N — streak = min(N, 7) (или 0 если не начинал).
        val streak = if (done == 0) 0 else minOf(done, 7)

        return ProgressUi(
            doneDays = done,
            totalDays = total,
            percent = percent,
            streak = streak,
            days = days
        )
    }
}
