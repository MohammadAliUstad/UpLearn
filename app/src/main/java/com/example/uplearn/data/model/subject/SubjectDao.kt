package com.example.uplearn.data.model.subject

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface SubjectDao {

    @Query("SELECT * FROM subject ORDER BY subjectName ASC ")
    fun getSubjectOrderedBySubjectName(): Flow<List<Subject>>

    @Query("SELECT * FROM subject ORDER BY difficulty ASC ")
    fun getSubjectOrderedByDifficulty(): Flow<List<Subject>>

    @Query("SELECT * FROM subject ORDER BY priorityLevel ASC ")
    fun getSubjectOrderedByPriorityLevel(): Flow<List<Subject>>

    @Upsert
    suspend fun addSubject(subject: Subject)

    @Delete
    suspend fun deleteSubject(subject: Subject)
}