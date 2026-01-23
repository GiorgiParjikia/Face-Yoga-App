package ru.netology.faceyoga.data.media

import java.util.concurrent.ConcurrentHashMap
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ImageUrlResolver @Inject constructor(
    private val videoUrlResolver: VideoUrlResolver
) {
    private val cache = ConcurrentHashMap<String, String>()

    suspend fun resolve(gsOrHttps: String): String {
        if (gsOrHttps.startsWith("http")) return gsOrHttps
        return cache.getOrPut(gsOrHttps) {
            // VideoUrlResolver уже умеет gs:// -> https
            // Просто переиспользуем его
            // (getOrPut требует синхронный блок, поэтому ниже сделаем по-другому)
            gsOrHttps
        }
    }

    suspend fun resolveCached(gsOrHttps: String): String {
        if (gsOrHttps.startsWith("http")) return gsOrHttps
        cache[gsOrHttps]?.let { return it }
        val https = videoUrlResolver.resolve(gsOrHttps)
        cache[gsOrHttps] = https
        return https
    }
}
