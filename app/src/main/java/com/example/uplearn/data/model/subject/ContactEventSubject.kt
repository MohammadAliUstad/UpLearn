package com.example.uplearn.data.model.subject

import com.example.uplearn.util.SortType2

sealed interface ContactEventSubject {

    data object SaveSubject : ContactEventSubject
    data class SetSubjectName(val subjectName: String) : ContactEventSubject
    data class SetSubjectDifficulty(val subjectDifficulty: String) : ContactEventSubject
    data class SetSubjectPriorityLevel(val subjectPriorityLevel: String) : ContactEventSubject

    data object ShowDialog : ContactEventSubject
    data object HideDialog : ContactEventSubject

    data class SortSubject(val sortType2: SortType2) : ContactEventSubject
    data class DeleteSubject(val subject: Subject) : ContactEventSubject
}