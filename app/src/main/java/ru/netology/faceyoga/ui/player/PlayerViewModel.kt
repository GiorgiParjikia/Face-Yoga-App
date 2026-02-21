package ru.netology.faceyoga.ui.player

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.netology.faceyoga.data.db.ProgressDao
import ru.netology.faceyoga.data.db.ProgramDayDao
import ru.netology.faceyoga.data.db.UserDayProgressEntity
import ru.netology.faceyoga.ui.common.StateKeys
import ru.netology.faceyoga.ui.day.DayExerciseUi
import javax.inject.Inject

@HiltViewModel
class PlayerViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val progressDao: ProgressDao,
    private val programDayDao: ProgramDayDao,
) : ViewModel() {

    private val programDayId: Long =
        savedStateHandle[StateKeys.PROGRAM_DAY_ID] ?: 0L

    private val dayNumber: Int =
        savedStateHandle[StateKeys.DAY_NUMBER] ?: 0

    private val _queue = MutableStateFlow(PlayerQueueState())
    val queue: StateFlow<PlayerQueueState> = _queue.asStateFlow()

    // ✅ Упражнения, которые реально “засчитались” (видео реально стартовало)
    private val _completedIds = MutableStateFlow<Set<Long>>(emptySet())
    val completedIds: StateFlow<Set<Long>> = _completedIds.asStateFlow()

    /** Инициализация очереди (вызываем 1 раз) */
    fun setQueue(list: List<DayExerciseUi>) {
        if (_queue.value.list.isNotEmpty()) return
        _queue.value = PlayerQueueState(list = list, index = 0)
    }

    /** Текущее упражнение */
    fun current(): DayExerciseUi? = _queue.value.current

    /** Проверка: текущее упражнение уже засчитано? */
    fun isCurrentCompleted(): Boolean {
        val cur = current() ?: return false
        return _completedIds.value.contains(cur.id)
    }

    /** ✅ Засчитать текущее упражнение (вызываем, когда видео реально пошло) */
    fun markCurrentCompleted() {
        val cur = current() ?: return
        if (_completedIds.value.contains(cur.id)) return
        _completedIds.value = _completedIds.value + cur.id
    }

    /** Переход к следующему упражнению */
    fun next() {
        val state = _queue.value
        if (state.hasNext) {
            _queue.value = state.copy(index = state.index + 1)
        }
    }

    /**
     * Вызываем на "Завершить" (последнее упражнение).
     * ✅ Пишем прогресс ТОЛЬКО по реально засчитанным упражнениям.
     * ✅ День отмечаем completed только если засчитались ВСЕ упражнения очереди.
     */
    fun finishDay() {
        val list = _queue.value.list
        if (programDayId == 0L || dayNumber == 0 || list.isEmpty()) return

        viewModelScope.launch {
            val programId = programDayDao.getProgramIdByProgramDayId(programDayId) ?: 0L
            if (programId == 0L) return@launch

            val completed = _completedIds.value
            if (completed.isEmpty()) return@launch

            // 1) отметить выполненными только те упражнения, которые реально засчитались
            list.forEach { ex ->
                if (completed.contains(ex.id)) {
                    progressDao.insertOrReplaceExerciseProgress(
                        programId = programId,
                        dayNumber = dayNumber,
                        exerciseId = ex.id,
                        isCompleted = true
                    )
                }
            }

            // 2) день — только если засчитались ВСЕ упражнения из очереди
            val allCompleted = list.all { completed.contains(it.id) }
            if (allCompleted) {
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
}