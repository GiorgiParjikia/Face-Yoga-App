package ru.netology.faceyoga.ui.articles

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.netology.faceyoga.data.repository.ArticlesRepository
import ru.netology.faceyoga.data.repository.ProgressRepository
import ru.netology.faceyoga.ui.articles.model.ArticleSectionUi
import javax.inject.Inject

@HiltViewModel
class ArticlesViewModel @Inject constructor(
    private val repo: ArticlesRepository,
    private val progressRepo: ProgressRepository
) : ViewModel() {

    private val _sections = MutableStateFlow<List<ArticleSectionUi>>(emptyList())
    val sections: StateFlow<List<ArticleSectionUi>> = _sections.asStateFlow()

    fun start() {
        viewModelScope.launch {
            progressRepo.observeLastCompletedDay().collect {
                // прогресс поменялся — пересобрали UI
                _sections.value = repo.loadSections()
            }
        }
    }
}
