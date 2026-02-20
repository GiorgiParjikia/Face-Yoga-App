package ru.netology.faceyoga.data.repository

import ru.netology.faceyoga.domain.model.ArticleSection

interface ArticlesRepository {
    suspend fun loadSections(maxCompletedDay: Int): List<ArticleSection>
}