package ru.netology.faceyoga.ui.progress

enum class DayState { DONE, AVAILABLE, LOCKED }

data class ProgressDayUi(
    val day: Int,
    val state: DayState
)

data class ProgressUi(
    val doneDays: Int,
    val totalDays: Int,
    val percent: Int,
    val streak: Int,
    val days: List<ProgressDayUi>
)