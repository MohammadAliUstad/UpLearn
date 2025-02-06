package com.example.uplearn.data.model.studysession

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class StudySession(
    @PrimaryKey(autoGenerate = true)
    val studySessionId: Int = 0,
    val duration: String,
    val date: String
)