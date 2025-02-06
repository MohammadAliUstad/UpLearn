package com.example.uplearn.data.model.userInfo

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class UserInfo(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val username: String,
    val course: String
)