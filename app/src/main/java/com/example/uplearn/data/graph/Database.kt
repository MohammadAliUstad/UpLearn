package com.example.uplearn.data.graph

import androidx.room.Database
import com.example.uplearn.data.model.studysession.StudySession
import com.example.uplearn.data.model.studysession.StudySessionDao
import com.example.uplearn.data.model.subject.Subject
import com.example.uplearn.data.model.subject.SubjectDao
import com.example.uplearn.data.model.userInfo.UserInfo
import com.example.uplearn.data.model.userInfo.UserInfoDao

@Database(
    entities = [
        StudySession::class,
        Subject::class,
        UserInfo::class
    ],
    version = 1
)
abstract class UpLearnDatabase {

    abstract fun getUserInfoDao(): UserInfoDao

    abstract fun getSubjectDao(): SubjectDao

    abstract fun getStudySessionDao(): StudySessionDao
}