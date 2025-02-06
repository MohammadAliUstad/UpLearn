package com.example.uplearn.data.model.subject

import com.example.uplearn.util.SortType2

data class SubjectState(
    val subject: List<Subject> = emptyList(),
    val subjectName: String = "",
    val difficulty: String = "",
    val priorityLevel: String = "",
    val isAddingSubject: Boolean = false,
    val sortType2: SortType2 = SortType2.SUBJECT_NAME
)