package com.example.festimob.ui.editor

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import com.example.festimob.FestiMobApplication
import com.example.festimob.data.models.Editor
import com.example.festimob.data.repositories.EditorsRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
data class EditorUiState(val editorsList: List<Editor> = emptyList())

class EditorViewModel(private val editorsRepository: EditorsRepository) : ViewModel() {

    val uiState: StateFlow<EditorUiState> = editorsRepository.getEditorsStream()
        .map { EditorUiState(it) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = EditorUiState()
        )

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as FestiMobApplication)
                EditorViewModel(application.editorsRepository)
            }
        }
    }
}