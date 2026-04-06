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
import com.example.festimob.data.models.EditorCreate
import com.example.festimob.data.repositories.EditorsRepository

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

    suspend fun saveForm() {
        if (validateInput(uiState.editorDetails)) {
            editorsRepository.insertEditor(
                EditorCreate(
                    name = uiState.editorDetails.name,
                    street = uiState.editorDetails.billingStreet,
                    city = uiState.editorDetails.billingCity,
                    country = uiState.editorDetails.billingCountry,
                    postalCode = uiState.editorDetails.billingPostcode,
                    contactFirstName = uiState.editorDetails.contactFirstname,
                    contactLastName = uiState.editorDetails.contactName,
                    contactEmail = uiState.editorDetails.contactEmail,
                    contactPhone = uiState.editorDetails.contactPhone,
                    contactProfession = uiState.editorDetails.contactJob
                )
            )
        }
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
