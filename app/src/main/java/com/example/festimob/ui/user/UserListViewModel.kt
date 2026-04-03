package com.example.festimob.ui.user

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
        observeUsers()
    }

    private fun observeUsers() {
        viewModelScope.launch {
            userRepository.getUsers().collect { users ->
                _state.value = UserListState.Success(users)
            }
        }
    }

    fun refreshUsers() {
        viewModelScope.launch {
            try {
                _state.value = UserListState.Loading
                userRepository.refreshUsers()
            } catch (e: Exception) {
                _state.value = UserListState.Error("Erreur: ${e.message}")
            }
        }
    }
}
