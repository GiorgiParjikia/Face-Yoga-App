package ru.netology.faceyoga.data.repository

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dagger.hilt.android.qualifiers.ApplicationContext
import ru.netology.faceyoga.data.model.ArticleCatalogJson
import ru.netology.faceyoga.data.model.ArticleCategory
import ru.netology.faceyoga.data.model.ArticleJson
import ru.netology.faceyoga.data.util.pick
import ru.netology.faceyoga.domain.model.ArticleCard
import ru.netology.faceyoga.domain.model.ArticleSection
import java.util.Locale
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ArticlesRepositoryImpl @Inject constructor(
    @param:ApplicationContext private val appContext: Context
) : ArticlesRepository {

    private val gson = Gson()

    override suspend fun loadSections(maxCompletedDay: Int): List<ArticleSection> {
        val lang = appContext.currentLang()

        val articles: List<ArticleJson> = loadArticles()
        val catalog: List<ArticleCatalogJson> = loadCatalog()
        val catalogById = catalog.associateBy { it.id }

        val merged = articles.mapNotNull { a ->
            val c = catalogById[a.id] ?: return@mapNotNull null
            MergedArticle(
                id = a.id,
                day = a.day,
                title = a.title.pick(lang),
                category = ArticleCategory.fromKey(c.categoryKey),
                orderInCategory = c.orderInCategory
            )
        }

        fun toCard(m: MergedArticle): ArticleCard {
            // правило: статья дня N открывается после завершения дня N
            val locked = maxCompletedDay < m.day
            return ArticleCard(
                id = m.id,
                title = m.title,
                isLocked = locked,
                lockedAfterDay = if (locked) m.day else null
            )
        }

        return merged
            .groupBy { it.category }
            .toList()
            .sortedBy { (cat, _) -> cat.ordinal }
            .map { (cat, list) ->
                ArticleSection(
                    titleRes = cat.titleRes,
                    categoryKey = cat.key,
                    items = list.sortedBy { it.orderInCategory }.map(::toCard)
                )
            }
    }

    private data class MergedArticle(
        val id: Int,
        val day: Int,
        val title: String,
        val category: ArticleCategory,
        val orderInCategory: Int
    )

    private fun loadArticles(): List<ArticleJson> = readJsonAsset("articles.json")
    private fun loadCatalog(): List<ArticleCatalogJson> = readJsonAsset("articles_catalog.json")

    private inline fun <reified T> readJsonAsset(fileName: String): T {
        val text = appContext.assets.open(fileName).bufferedReader().use { it.readText() }
        val type = object : TypeToken<T>() {}.type
        return gson.fromJson(text, type)
    }
}

private fun Context.currentLang(): String {
    val locale = resources.configuration.locales[0] ?: Locale.getDefault()
    val code = locale.language.lowercase(Locale.US)
    return when (code) {
        "ru" -> "ru"
        "en" -> "en"
        "ka", "geo" -> "ka"
        else -> "en"
    }
}