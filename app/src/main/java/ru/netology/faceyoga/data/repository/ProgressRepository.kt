package ru.netology.faceyoga.data.repository

interface ProgressRepository {
    suspend fun getLastCompletedDay(): Int
    suspend fun setLastCompletedDay(day: Int)
}
