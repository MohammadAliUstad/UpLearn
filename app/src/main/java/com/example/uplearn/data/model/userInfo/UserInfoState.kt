package com.example.uplearn.data.model.userInfo

import com.example.uplearn.util.SortType1

data class UserInfoState(
    val userInfo: List<UserInfo> = emptyList(),
    val userName: String = "",
    val course: String = "",
    val isAddingUserInfo: Boolean = false,
    val sortType: SortType1 = SortType1.USER_NAME
)