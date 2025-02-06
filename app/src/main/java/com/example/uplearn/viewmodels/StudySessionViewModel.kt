package com.example.uplearn.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.uplearn.data.model.studysession.ContactEventStudySession
import com.example.uplearn.data.model.studysession.StudySession
import com.example.uplearn.data.model.studysession.StudySessionDao
import com.example.uplearn.data.model.studysession.StudySessionState
import com.example.uplearn.util.SortType3
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
class StudySessionViewModel(
    private val dao: StudySessionDao
) : ViewModel() {
    private val _sortType3 = MutableStateFlow(SortType3.DURATION_NAME)
    private val _studySession = _sortType3.flatMapLatest { sortType ->
        when (sortType) {
            SortType3.DURATION_NAME -> dao.getStudySessionOrderedByDuration()
            SortType3.DATE_NAME -> dao.getStudySessionOrderedByDate()
        }
    }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())
    private val _state = MutableStateFlow(StudySessionState())
    private val state =
        combine(_state, _sortType3, _studySession) { state, sortType3, studySession ->
            state.copy(
                studySession = studySession,
                sortType3 = sortType3
            )
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), StudySessionState())

    fun onEvent(event: ContactEventStudySession) {
        when (event) {
            is ContactEventStudySession.DeleteEventStudySession -> {
                viewModelScope.launch {
                    dao.deleteStudySession(event.studySession)
                }
            }

            ContactEventStudySession.HideDialog3 -> {
                _state.update {
                    it.copy(
                        isAddingStudySession = false
                    )
                }
            }

            ContactEventStudySession.ShowDialog3 -> {
                _state.update { it.copy(isAddingStudySession = true) }
            }

            is ContactEventStudySession.SortEventStudySession -> {
                _sortType3.value = event.sortType3
            }

            ContactEventStudySession.SaveEventStudySession -> {
                val duration = state.value.duration
                val date = state.value.date

                if (duration.isBlank() || date.isBlank()) {
                    return
                }
                val studySession = StudySession(
                    duration = duration,
                    date = date
                )
                viewModelScope.launch {
                    dao.addStudySession(studySession)
                }
                _state.update {
                    it.copy(
                        isAddingStudySession = false,
                        duration = "",
                        date = ""
                    )
                }
            }

            is ContactEventStudySession.SetEventStudySessionDate -> {
                _state.update { it.copy(date = event.studySessionDate) }
            }

            is ContactEventStudySession.SetEventStudySessionDuration -> {
                _state.update { it.copy(duration = event.studySessionDuration) }
            }
        }
    }
}