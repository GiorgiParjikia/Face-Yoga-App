package ru.netology.faceyoga.data.articles

import android.content.Context
import org.json.JSONArray
import org.json.JSONObject

class ArticlesRepository(
    private val context: Context
) {

    private var cache: List<Article>? = null

    fun getById(id: Int): Article? =
        getAll().firstOrNull { it.id == id }

    fun getAll(): List<Article> {
        cache?.let { return it }

        val json = context.assets
            .open("articles.json")
            .bufferedReader()
            .use { it.readText() }

        val array = JSONArray(json)
        val result = mutableListOf<Article>()

        for (i in 0 until array.length()) {
            result += array.getJSONObject(i).toArticle()
        }

        cache = result
        return result
    }

    private fun JSONObject.toArticle(): Article {
        return Article(
            id = getInt("id"),
            day = getInt("day"),
            title = getJSONObject("title").toStringMap(),
            text = getJSONObject("text").toStringMap(),

            // OPTIONAL FIELDS (MVP-safe)
            images = optJSONArray("images")?.toStringList() ?: emptyList(),
            captions = optJSONObject("captions")?.toStringListMap() ?: emptyMap()
        )
    }

    private fun JSONObject.toStringMap(): Map<String, String> =
        keys().asSequence().associateWith { getString(it) }

    private fun JSONArray.toStringList(): List<String> =
        (0 until length()).map { getString(it) }

    private fun JSONObject.toStringListMap(): Map<String, List<String>> =
        keys().asSequence().associateWith { key ->
            optJSONArray(key)?.toStringList() ?: emptyList()
        }
}
