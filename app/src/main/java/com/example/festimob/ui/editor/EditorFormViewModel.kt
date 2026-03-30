package com.example.festimob.ui.editor

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.festimob.FestiMobApplication
import com.example.festimob.R
import com.example.festimob.data.models.Editor
import com.example.festimob.data.repositories.EditorsRepository
import com.example.festimob.data.repositories.UserPreferencesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class EditorFormUiState(
    val editorName: String = "",
    val lastName: String = "",
    val firstName: String = "",
    val profession: String = "",
    val email: String = "",
    val phone: String = "",
    val street: String = "",
    val zipCode: String = "",
    val city: String = "",
    val country: String = "",
    val isEntryValid: Boolean = false // Helper for the Save button
)
class EditorFormViewModel(
    private val editorsRepository: EditorsRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(EditorFormUiState())
    val uiState: StateFlow<EditorFormUiState> = _uiState.asStateFlow()

    fun updateUiState(newEditorState: EditorFormUiState) {
        _uiState.update {
            newEditorState.copy(isEntryValid = validateInput(newEditorState))
        }
    }

    private fun validateInput(uiState: EditorFormUiState): Boolean {
        return uiState.editorName.isNotBlank() && uiState.email.contains("@")
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as FestiMobApplication)
                EditorFormViewModel(application.editorsRepository)
            }
        }
    }
}