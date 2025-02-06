package com.example.uplearn.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.uplearn.data.model.userInfo.ContactEventUserInfo
import com.example.uplearn.data.model.userInfo.UserInfo
import com.example.uplearn.data.model.userInfo.UserInfoDao
import com.example.uplearn.data.model.userInfo.UserInfoState
import com.example.uplearn.util.SortType1
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
class UserInfoViewModel(
    private val dao: UserInfoDao
) : ViewModel() {
    private val _sortType = MutableStateFlow(SortType1.USER_NAME)
    private val _userInfo = _sortType.flatMapLatest { sortType ->
        when (sortType) {
            SortType1.USER_NAME -> dao.getUserInfoOrderedByUsername()
            SortType1.COURSE_NAME -> dao.getUserInfoOrderedByCourse()
        }
    }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())
    private val _state = MutableStateFlow(UserInfoState())
    private val state = combine(_state, _sortType, _userInfo) { state, sortType, userInfo ->
        state.copy(
            userInfo = userInfo,
            sortType = sortType
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), UserInfoState())

    fun onEvent(event: ContactEventUserInfo) {
        when (event) {
            is ContactEventUserInfo.DeleteUserInfo -> {
                viewModelScope.launch {
                    dao.deleteUserInfo(event.userInfo)
                }
            }

            ContactEventUserInfo.HideDialog -> {
                _state.update {
                    it.copy(
                        isAddingUserInfo = false
                    )
                }
            }

            ContactEventUserInfo.ShowDialog -> {
                _state.update { it.copy(isAddingUserInfo = true) }
            }

            is ContactEventUserInfo.SortUserInfo -> {
                _sortType.value = event.sortType
            }

            ContactEventUserInfo.SaveUserInfo -> {
                val username = state.value.userName
                val course = state.value.course

                if (username.isBlank() || course.isBlank()) {
                    return
                }
                val userInfo = UserInfo(
                    username = username,
                    course = course,
                )
                viewModelScope.launch {
                    dao.addUserInfo(userInfo)
                }
                _state.update {
                    it.copy(
                        isAddingUserInfo = false,
                        userName = "",
                        course = ""
                    )
                }
            }

            is ContactEventUserInfo.SetCourse -> {
                _state.update { it.copy(course = event.course) }
            }

            is ContactEventUserInfo.SetUsername -> {
                _state.update { it.copy(userName = event.username) }
            }
        }
    }
}