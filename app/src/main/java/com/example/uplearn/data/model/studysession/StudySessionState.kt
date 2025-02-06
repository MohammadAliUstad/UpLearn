package com.example.uplearn.data.model.studysession

import com.example.uplearn.util.SortType3

data class StudySessionState(
    val studySession: List<StudySession> = emptyList(),
    val duration: String = "",
    val date: String = "",
    val isAddingStudySession: Boolean = false,
    val sortType3: SortType3 = SortType3.DURATION_NAME
)