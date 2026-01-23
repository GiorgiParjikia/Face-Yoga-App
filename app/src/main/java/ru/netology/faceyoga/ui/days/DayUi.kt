package ru.netology.faceyoga.ui.days

data class DayUi(
    val programDayId: Long,
    val dayNumber: Int,
    val title: String?,         // "Зона глаз" и т.п.
    val exercisesCount: Int,
    val isCompleted: Boolean,
    val progressPercent: Int = 0 // пока 0, позже считаем по упражнениям
)
