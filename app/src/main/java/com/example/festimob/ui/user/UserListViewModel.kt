package com.example.festimob.ui.user

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.festimob.data.api.OfflineUserRepository
import com.example.festimob.data.api.User
import com.example.festimob.data.api.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class UserListState {
    object Loading : UserListState()
    data class Success(val users: List<User>) : UserListState()
    data class Error(val message: String) : UserListState()
}

class UserListViewModel(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _state: MutableStateFlow<UserListState> = MutableStateFlow(UserListState.Loading)
    val state: StateFlow<UserListState> = _state

    init {
        fetchUsers()
    }

    private fun fetchUsers() {
        viewModelScope.launch {
            try {
                val offlineRepository = userRepository as? OfflineUserRepository
                    ?: throw IllegalStateException("UserRepository must be OfflineUserRepository")
                offlineRepository.refreshUsers()
                userRepository.getUsers().collect { users ->
                    _state.value = UserListState.Success(users)
                }
            } catch (e: Exception) {
                _state.value = UserListState.Error("Erreur: ${e.message}")
            }
        }
    }
}
