package com.example.uplearn.data.model.studysession

import com.example.uplearn.util.SortType3

sealed interface ContactEventStudySession {

    data object SaveEventStudySession : ContactEventStudySession
    data class SetEventStudySessionDuration(val studySessionDuration: String) : ContactEventStudySession
    data class SetEventStudySessionDate(val studySessionDate: String) : ContactEventStudySession

    data object ShowDialog3 : ContactEventStudySession
    data object HideDialog3 : ContactEventStudySession

    data class SortEventStudySession(val sortType3: SortType3) : ContactEventStudySession
    data class DeleteEventStudySession(val studySession: StudySession) : ContactEventStudySession
}