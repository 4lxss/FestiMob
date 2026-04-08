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
import com.example.festimob.data.api.editor.Editor
import com.example.festimob.data.api.editor.EditorsRepository
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

data class EditorDetailsUiState(
    val editor: Editor?
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


    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as FestiMobApplication)
                EditorDetailsViewModel(application.editorsRepository)
            }
        }
    }
}
