package com.example.uplearn.data.model.userInfo

import com.example.uplearn.util.SortType1

sealed interface ContactEventUserInfo {

    data object SaveUserInfo : ContactEventUserInfo
    data class SetUsername(val username: String) : ContactEventUserInfo
    data class SetCourse(val course: String) : ContactEventUserInfo

    data object ShowDialog : ContactEventUserInfo
    data object HideDialog : ContactEventUserInfo

    data class SortUserInfo(val sortType: SortType1) : ContactEventUserInfo
    data class DeleteUserInfo(val userInfo: UserInfo) : ContactEventUserInfo
}