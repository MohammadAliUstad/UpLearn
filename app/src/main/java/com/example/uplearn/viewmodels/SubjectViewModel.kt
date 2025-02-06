package com.example.uplearn.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.uplearn.data.model.subject.ContactEventSubject
import com.example.uplearn.data.model.subject.Subject
import com.example.uplearn.data.model.subject.SubjectDao
import com.example.uplearn.data.model.subject.SubjectState
import com.example.uplearn.util.SortType2
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
class SubjectViewModel(
    private val dao: SubjectDao
) : ViewModel() {
    private val _sortType2 = MutableStateFlow(SortType2.SUBJECT_NAME)
    private val _subject = _sortType2.flatMapLatest { sortType ->
        when (sortType) {
            SortType2.SUBJECT_NAME -> dao.getSubjectOrderedBySubjectName()
            SortType2.DIFFICULTY_NAME -> dao.getSubjectOrderedByDifficulty()
            SortType2.PRIORITY_LEVEL_NAME -> dao.getSubjectOrderedByPriorityLevel()
        }
    }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())
    private val _state = MutableStateFlow(SubjectState())
    private val state = combine(_state, _sortType2, _subject) { state, sortType2, subject ->
        state.copy(
            subject = subject,
            sortType2 = sortType2
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), SubjectState())

    fun onEvent(event: ContactEventSubject) {
        when (event) {
            is ContactEventSubject.DeleteSubject -> {
                viewModelScope.launch {
                    dao.deleteSubject(event.subject)
                }
            }

            ContactEventSubject.HideDialog -> {
                _state.update {
                    it.copy(
                        isAddingSubject = false
                    )
                }
            }

            ContactEventSubject.ShowDialog -> {
                _state.update { it.copy(isAddingSubject = true) }
            }

            is ContactEventSubject.SortSubject -> {
                _sortType2.value = event.sortType2
            }

            ContactEventSubject.SaveSubject -> {
                val subjectName = state.value.subjectName
                val difficulty = state.value.difficulty
                val priorityLevel = state.value.priorityLevel

                if (subjectName.isBlank() || difficulty.isBlank() || priorityLevel.isBlank()) {
                    return
                }
                val subject = Subject(
                    subjectName = subjectName,
                    difficulty = difficulty,
                    priorityLevel = priorityLevel
                )
                viewModelScope.launch {
                    dao.addSubject(subject)
                }
                _state.update {
                    it.copy(
                        isAddingSubject = false,
                        subjectName = "",
                        difficulty = "",
                        priorityLevel = ""
                    )
                }
            }

            is ContactEventSubject.SetSubjectDifficulty -> {
                _state.update { it.copy(difficulty = event.subjectDifficulty) }
            }

            is ContactEventSubject.SetSubjectName -> {
                _state.update { it.copy(subjectName = event.subjectName) }
            }

            is ContactEventSubject.SetSubjectPriorityLevel -> {
                _state.update { it.copy(priorityLevel = event.subjectPriorityLevel) }
            }
        }
    }
}