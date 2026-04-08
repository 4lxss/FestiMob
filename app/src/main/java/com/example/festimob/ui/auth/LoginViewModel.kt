package com.example.festimob.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.festimob.data.api.APIService
import com.example.festimob.data.api.admin.login.LoginRequest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class LoginUiState(
    val username: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val message: String? = null,
    val isSuccess: Boolean = false,
    val loggedUserRole: String? = null,
    val loggedUserId: Int = 0
)

class LoginViewModel(
    private val apiService: APIService
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState

    fun onUsernameChange(value: String) {
        _uiState.update { it.copy(username = value, message = null) }
    }

    fun onPasswordChange(value: String) {
        _uiState.update { it.copy(password = value, message = null) }
    }

    fun consumeLoginSuccess() {
        _uiState.update { it.copy(isSuccess = false) }
    }

    fun login() {
        val current = _uiState.value
        if (current.username.isBlank() || current.password.isBlank()) {
            _uiState.update { it.copy(message = "Identifiants manquants") }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, message = null, isSuccess = false) }
            try {
                val response = apiService.login(
                    LoginRequest(
                        username = current.username.trim(),
                        password = current.password
                    )
                )

                val profileResp = apiService.whoAmI()
                val userProfile = profileResp.user

                _uiState.update {
                    it.copy(
                        isLoading = false,
                        message = response.message,
                        isSuccess = true,
                        loggedUserRole = response.user?.role,
                        loggedUserId = userProfile?.id_u ?: 0
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        isSuccess = false,
                        message = e.message ?: "Erreur de connexion",
                        loggedUserRole = null
                    )
                }
            }
        }
    }
}
