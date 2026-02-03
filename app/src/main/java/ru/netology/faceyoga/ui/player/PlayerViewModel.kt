package ru.netology.faceyoga.ui.player

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import ru.netology.faceyoga.data.db.ProgressDao
import ru.netology.faceyoga.data.db.UserDayProgressEntity
import ru.netology.faceyoga.data.repository.ProgramRepository
import ru.netology.faceyoga.ui.day.DayExerciseUi
import javax.inject.Inject
import ru.netology.faceyoga.ui.common.StateKeys


@HiltViewModel
class PlayerViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val repository: ProgramRepository,
    private val progressDao: ProgressDao,
) : ViewModel() {

    private val programDayId: Long =
        savedStateHandle[StateKeys.PROGRAM_DAY_ID] ?: 0L

    private val dayNumber: Int =
        savedStateHandle[StateKeys.DAY_NUMBER] ?: 0

    private val _queue = MutableStateFlow(PlayerQueueState())
    val queue: StateFlow<PlayerQueueState> = _queue

    /** Инициализация очереди (вызываем 1 раз) */
    fun setQueue(list: List<DayExerciseUi>) {
        if (_queue.value.list.isNotEmpty()) return
        _queue.value = PlayerQueueState(list = list, index = 0)
    }

    /** Текущее упражнение */
    fun current(): DayExerciseUi? = _queue.value.current

    /** Переход к следующему упражнению */
    fun next() {
        val state = _queue.value
        if (state.hasNext) {
            _queue.value = state.copy(index = state.index + 1)
        }
    }

    /** Завершение упражнения */
    fun completeCurrent() {
        next()
    }

    /**
     * Вызываем на "Завершить" (последнее упражнение).
     * Ставит:
     * - user_exercise_progress (все упражнения дня выполнены)
     * - user_day_progress (день выполнен)
     */
    fun finishDay() {
        val list = _queue.value.list
        if (programDayId == 0L || dayNumber == 0 || list.isEmpty()) return

        viewModelScope.launch {
            val programId = repository.getProgramIdByProgramDayId(programDayId)
            if (programId == 0L) return@launch

            // 1) отметить ВСЕ упражнения дня выполненными
            list.forEach { ex ->
                progressDao.insertOrReplaceExerciseProgress(
                    programId = programId,
                    dayNumber = dayNumber,
                    exerciseId = ex.id,
                    isCompleted = true
                )
            }

            // 2) отметить ДЕНЬ выполненным
            progressDao.upsertDayProgress(
                UserDayProgressEntity(
                    programId = programId,
                    dayNumber = dayNumber,
                    isCompleted = true,
                    completedAt = System.currentTimeMillis()
                )
            )
        }
    }
}