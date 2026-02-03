package ru.netology.faceyoga.data.seed

sealed class DayExerciseSeed {
    abstract val title: String

    data class Reps(
        override val title: String,
        val reps: Int
    ) : DayExerciseSeed()

    data class Timer(
        override val title: String,
        val seconds: Int
    ) : DayExerciseSeed()
}
