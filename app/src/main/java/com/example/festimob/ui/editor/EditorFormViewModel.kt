package com.example.festimob.ui.editor

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
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
    val editorDetails : EditorDetails,
    val isEntryValid: Boolean = false // Helper for the Save button
)
class EditorFormViewModel(
    private val editorsRepository: EditorsRepository,
) : ViewModel() {
    var uiState by mutableStateOf(EditorFormUiState(EditorDetails()))
        private set

    fun updateUiState(editorDetails: EditorDetails) {
        uiState = EditorFormUiState(editorDetails, validateInput(editorDetails))
    }

    private fun validateInput(editorDetails: EditorDetails): Boolean {
        return editorDetails.name.isNotBlank() && editorDetails.contactEmail.contains("@")
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