package ru.netology.faceyoga.data.repository

import ru.netology.faceyoga.ui.articles.model.ArticleSectionUi

interface ArticlesRepository {
    suspend fun loadSections(maxCompletedDay: Int): List<ArticleSectionUi>
}
