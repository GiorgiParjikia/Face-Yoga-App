package ru.netology.faceyoga.ui.articles.mapper

import ru.netology.faceyoga.domain.model.ArticleCard
import ru.netology.faceyoga.domain.model.ArticleSection
import ru.netology.faceyoga.ui.articles.model.ArticleCardUi
import ru.netology.faceyoga.ui.articles.model.ArticleSectionUi

fun ArticleCard.toUi(): ArticleCardUi =
    ArticleCardUi(
        id = id,
        title = title,
        isLocked = isLocked,
        lockedAfterDay = lockedAfterDay
    )

fun ArticleSection.toUi(): ArticleSectionUi =
    ArticleSectionUi(
        titleRes = titleRes,
        categoryKey = categoryKey,
        items = items.map { it.toUi() }
    )