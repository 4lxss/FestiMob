package com.example.festimob.ui.admin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.festimob.data.api.User
import com.example.festimob.data.api.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class AdminScreenState {
    object Loading : AdminScreenState()
    data class Success(val users: List<User>) : AdminScreenState()
    data class Error(val message: String) : AdminScreenState()
}

class AdminScreenViewModel(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _state: MutableStateFlow<AdminScreenState> = MutableStateFlow(AdminScreenState.Loading)
    val state: StateFlow<AdminScreenState> = _state

    init {
        observeUsers()
    }

    private fun observeUsers() {
        viewModelScope.launch {
            userRepository.getUsers().collect { users ->
                _state.value = AdminScreenState.Success(users)
            }
        }
    }

    fun refreshUsers() {
        viewModelScope.launch {
            try {
                _state.value = AdminScreenState.Loading
                userRepository.refreshUsers()
            } catch (e: Exception) {
                _state.value = AdminScreenState.Error("Erreur: ${e.message}")
            }
        }
    }
}
