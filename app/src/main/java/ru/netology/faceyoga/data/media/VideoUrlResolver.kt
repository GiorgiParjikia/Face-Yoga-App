package ru.netology.faceyoga.data.media

interface VideoUrlResolver {
    suspend fun resolve(gsOrHttp: String): String
}
