package ru.netology.faceyoga.data.model

data class ArticleJson(
    val id: Int,
    val day: Int,
    val title: Map<String, String>,
    val text: Map<String, String>
)

data class ArticleCatalogJson(
    val id: Int,
    val categoryKey: String,
    val orderInCategory: Int
)
