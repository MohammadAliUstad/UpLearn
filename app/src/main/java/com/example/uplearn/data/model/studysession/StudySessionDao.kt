package com.example.uplearn.data.model.studysession

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface StudySessionDao {

    @Query("SELECT * FROM studySession ORDER BY duration ASC ")
    fun getStudySessionOrderedByDuration(): Flow<List<StudySession>>

    @Query("SELECT * FROM studySession ORDER BY date ASC ")
    fun getStudySessionOrderedByDate(): Flow<List<StudySession>>

    @Upsert
    suspend fun addStudySession(studySession: StudySession)

    @Delete
    suspend fun deleteStudySession(studySession: StudySession)
}