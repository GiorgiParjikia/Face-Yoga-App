package ru.netology.faceyoga.data.articles

data class Article(
    val id: Int,
    val day: Int,
    val title: Map<String, String>,
    val text: Map<String, String>,
    val images: List<String> = emptyList(),
    val captions: Map<String, List<String>> = emptyMap()
) {

    fun titleFor(lang: String): String =
        title[lang] ?: title["en"] ?: title.values.firstOrNull().orEmpty()

    fun textFor(lang: String): String =
        text[lang] ?: text["en"] ?: text.values.firstOrNull().orEmpty()

    fun captionsFor(lang: String): List<String> =
        captions[lang] ?: captions["en"] ?: emptyList()
}
