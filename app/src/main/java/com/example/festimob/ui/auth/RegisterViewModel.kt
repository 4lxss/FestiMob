package com.example.festimob.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.festimob.data.api.APIService
import com.example.festimob.data.api.admin.register.RegisterRequest
import retrofit2.HttpException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class RegisterUiState(
    val username: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val message: String? = null,
    val isSuccess: Boolean = false
)

class RegisterViewModel(
    private val apiService: APIService
) : ViewModel() {

    private val _uiState = MutableStateFlow(RegisterUiState())
    val uiState: StateFlow<RegisterUiState> = _uiState

    fun onUsernameChange(value: String) {
        _uiState.update { it.copy(username = value, message = null, isSuccess = false) }
    }

    fun onPasswordChange(value: String) {
        _uiState.update { it.copy(password = value, message = null, isSuccess = false) }
    }

    fun consumeRegisterSuccess() {
        _uiState.update { it.copy(isSuccess = false) }
    }

    fun register() {
        val current = _uiState.value
        if (current.username.isBlank() || current.password.isBlank()) {
            _uiState.update { it.copy(message = "Identifiants manquants") }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, message = null, isSuccess = false) }
            try {
                val response = apiService.register(
                    RegisterRequest(
                        username = current.username.trim(),
                        password = current.password.trim(),
                        role = "user"
                    )
                )
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        message = response.message,
                        isSuccess = true
                    )
                }
            } catch (e: HttpException) {
                val errorBody = e.response()?.errorBody()?.string()
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        message = errorBody ?: e.message ?: "Erreur lors de l'inscription",
                        isSuccess = false
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        message = e.message ?: "Erreur lors de l'inscription",
                        isSuccess = false
                    )
                }
            }
        }
    }
}
