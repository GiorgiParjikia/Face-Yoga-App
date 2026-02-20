package ru.netology.faceyoga.ui.articles

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import ru.netology.faceyoga.data.repository.ArticlesRepository
import ru.netology.faceyoga.data.repository.ProgramRepository
import ru.netology.faceyoga.data.repository.ProgressRepository
import ru.netology.faceyoga.ui.articles.mapper.toUi
import ru.netology.faceyoga.ui.articles.model.ArticleSectionUi
import javax.inject.Inject

@HiltViewModel
class ArticlesViewModel @Inject constructor(
    private val repo: ArticlesRepository,
    private val progressRepo: ProgressRepository,
    private val programRepo: ProgramRepository
) : ViewModel() {

    private val _sections = MutableStateFlow<List<ArticleSectionUi>>(emptyList())
    val sections: StateFlow<List<ArticleSectionUi>> = _sections.asStateFlow()

    private val _programId = MutableStateFlow<Long?>(null)

    fun start() {
        // 1) programId
        viewModelScope.launch {
            _programId.value = programRepo.getDefaultProgramId()
        }

        // 2) Room maxCompletedDay -> пересборка секций с lock
        viewModelScope.launch {
            _programId
                .filterNotNull()
                .distinctUntilChanged()
                .flatMapLatest { programId ->
                    progressRepo.observeMaxCompletedDay(programId)
                }
                .distinctUntilChanged()
                .collect { maxDay ->
                    _sections.value = repo
                        .loadSections(maxCompletedDay = maxDay)
                        .map { it.toUi() }
                }
        }
    }
}