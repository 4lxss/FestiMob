package com.example.festimob.ui.editor

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.festimob.FestiMobApplication
import com.example.festimob.data.models.Editor
import com.example.festimob.data.repositories.EditorsRepository
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

data class EditorDetailsUiState(
    val editor: Editor?,
    val isEditingAddress: Boolean = false,
    // Edition address
    val editStreet: String = "",
    val editCity: String = "",
    val editPostalCode: String = "",
)

class EditorDetailsViewModel(
    private val editorsRepository: EditorsRepository
) : ViewModel() {
    var uiState by mutableStateOf(EditorDetailsUiState(editor = null))

    fun setUiState(editorId: Int) {
        viewModelScope.launch {
            val editor = editorsRepository.getEditorById(editorId)
            uiState = EditorDetailsUiState(editor)
        }
    }

    // Edit billing address
    fun toggleEditAddress() {
        uiState = uiState.copy(
            isEditingAddress = !uiState.isEditingAddress,
            editStreet = uiState.editor?.address?.street ?: "",
            editCity = uiState.editor?.address?.city ?: "",
            editPostalCode = uiState.editor?.address?.postalCode ?: ""
        )
    }

    fun updateEditField(street: String, city: String, zip: String) {
        uiState = uiState.copy(editStreet = street, editCity = city, editPostalCode = zip)
    }

    fun saveChanges() {
        val currentEditor = uiState.editor ?: return
        viewModelScope.launch {
            val updatedEditor = currentEditor.copy(
                address = currentEditor.address?.copy(
                    street = uiState.editStreet,
                    city = uiState.editCity,
                    postalCode = uiState.editPostalCode
                )
            )
            uiState = uiState.copy(editor = updatedEditor, isEditingAddress = false)
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as FestiMobApplication)
                EditorDetailsViewModel(application.editorsRepository)
            }
        }
    }
}
