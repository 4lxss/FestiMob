package com.example.festimob.ui.admin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.festimob.data.api.Role
import com.example.festimob.data.api.User
import com.example.festimob.data.api.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException

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
    val allowedRoles = listOf(
        Role.USER.value,
        Role.VOLUNTEER.value,
        Role.ORGANIZER.value,
        Role.SUPER_ORGANIZER.value
    )

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
            } catch (e: HttpException) {
                _state.value = if (e.code() == 401 || e.code() == 403) {
                    AdminScreenState.Error("Vous n'avez pas les droits nécessaires")
                } else {
                    AdminScreenState.Error("Erreur: ${e.message}")
                }
            } catch (e: Exception) {
                _state.value = AdminScreenState.Error("Erreur: ${e.message}")
            }
        }
    }

    fun updateUserRole(userId: Int, newRole: String) {
        viewModelScope.launch {
            try {
                userRepository.updateUserRole(userId, newRole)
            } catch (e: HttpException) {
                _state.value = if (e.code() == 401 || e.code() == 403) {
                    AdminScreenState.Error("Vous n'avez pas les droits nécessaires")
                } else {
                    AdminScreenState.Error("Impossible de mettre à jour le rôle")
                }
            } catch (e: Exception) {
                _state.value = AdminScreenState.Error("Impossible de mettre à jour le rôle")
            }
        }
    }

    fun deleteUser(userId: Int) {
        viewModelScope.launch {
            try {
                userRepository.deleteUser(userId)
            } catch (e: HttpException) {
                _state.value = if (e.code() == 401 || e.code() == 403) {
                    AdminScreenState.Error("Vous n'avez pas les droits nécessaires")
                } else {
                    AdminScreenState.Error("Impossible de supprimer l'utilisateur")
                }
            } catch (e: Exception) {
                _state.value = AdminScreenState.Error("Impossible de supprimer l'utilisateur")
            }
        }
    }
}
