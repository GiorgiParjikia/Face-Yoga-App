package ru.netology.faceyoga.ui.articles.model

import androidx.annotation.StringRes

data class ArticleCardUi(
    val id: Int,
    val title: String,
    val isLocked: Boolean,
    val lockedAfterDay: Int? // <- вместо готовой строки
)

data class ArticleSectionUi(
    @StringRes val titleRes: Int,
    val categoryKey: String?, // null для Recommended
    val items: List<ArticleCardUi>
)
