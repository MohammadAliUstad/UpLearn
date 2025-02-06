package com.example.uplearn.data.model.subject

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Subject(
    @PrimaryKey(autoGenerate = true)
    val subjectId: Int = 0,
    val subjectName: String,
    val difficulty: String,
    val priorityLevel: String
)