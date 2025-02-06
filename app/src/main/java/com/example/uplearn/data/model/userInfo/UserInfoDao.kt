package com.example.uplearn.data.model.userInfo

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface UserInfoDao {

    @Query("SELECT * FROM userInfo ORDER BY username ASC ")
    fun getUserInfoOrderedByUsername(): Flow<List<UserInfo>>

    @Query("SELECT * FROM userInfo ORDER BY course ASC ")
    fun getUserInfoOrderedByCourse(): Flow<List<UserInfo>>

    @Upsert
    suspend fun addUserInfo(userInfo: UserInfo)

    @Delete
    suspend fun deleteUserInfo(userInfo: UserInfo)
}