package ru.netology.faceyoga.ui.articles.model

sealed class ArticleBlock {

    data class Text(
        val text: String
    ) : ArticleBlock()

    data class Image(
        val name: String,
        val caption: String?
    ) : ArticleBlock()
}
