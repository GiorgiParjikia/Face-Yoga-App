package ru.netology.faceyoga.ui.days

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import ru.netology.faceyoga.data.repository.ProgramRepository
import javax.inject.Inject

@HiltViewModel
class DaysViewModel @Inject constructor(
    private val repository: ProgramRepository
) : ViewModel() {

    private val programIdFlow = flow { emit(repository.getDefaultProgramId()) }

    val days: StateFlow<List<DayUi>> =
        programIdFlow
            .flatMapLatest { programId -> repository.observeDays(programId) }
            .map { list ->
                list.map { row ->
                    DayUi(
                        programDayId = row.programDayId,
                        dayNumber = row.dayNumber,
                        title = row.title,
                        exercisesCount = row.exercisesCount,
                        isCompleted = row.isCompleted,
                        progressPercent = if (row.isCompleted) 100 else 0
                    )
                }
            }
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())
}