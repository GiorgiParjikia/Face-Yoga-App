package ru.netology.faceyoga.domain.model

import androidx.annotation.StringRes

data class ArticleCard(
    val id: Int,
    val title: String,
    val isLocked: Boolean,
    val lockedAfterDay: Int?
)

data class ArticleSection(
    @param:StringRes val titleRes: Int,
    val categoryKey: String?,
    val items: List<ArticleCard>
)