package ru.netology.faceyoga.ui.player

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import ru.netology.faceyoga.ui.day.DayExerciseUi
import javax.inject.Inject

@HiltViewModel
class PlayerViewModel @Inject constructor() : ViewModel() {

    private val _queue = MutableStateFlow(PlayerQueueState())
    val queue: StateFlow<PlayerQueueState> = _queue

    /** Инициализация очереди (вызываем 1 раз) */
    fun setQueue(list: List<DayExerciseUi>) {
        if (_queue.value.list.isNotEmpty()) return
        _queue.value = PlayerQueueState(list = list, index = 0)
    }

    /** Текущее упражнение */
    fun current(): DayExerciseUi? =
        _queue.value.current

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
}