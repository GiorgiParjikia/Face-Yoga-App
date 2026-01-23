package ru.netology.faceyoga.ui.player

import ru.netology.faceyoga.ui.day.DayExerciseUi

data class PlayerQueueState(
    val list: List<DayExerciseUi> = emptyList(),
    val index: Int = 0
) {
    val current: DayExerciseUi? get() = list.getOrNull(index)
    val hasNext: Boolean get() = index < list.lastIndex
}
