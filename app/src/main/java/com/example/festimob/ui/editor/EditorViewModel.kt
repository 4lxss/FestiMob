package com.example.festimob.ui.editor

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.festimob.data.repositories.EditorsRepository
import com.example.festimob.data.repositories.OfflineEditorsRepository

class EditorViewModel(
    private val editorsRepository : EditorsRepository
) : ViewModel() {
    /*
    // UI states access for various [DessertReleaseUiState]
    val uiState: StateFlow<EditorsUiState> = userPreferencesRepository.isLinearLayout.map { isLinearLayout ->
        DessertReleaseUiState(isLinearLayout)
    }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = DessertReleaseUiState()
        )
    */
    /*
     * [selectLayout] change the layout and icons accordingly and
     * save the selection in DataStore through [userPreferencesRepository]
     */
    /*
    fun selectLayout(isLinearLayout: Boolean) {
        viewModelScope.launch {
            userPreferencesRepository.saveLayoutPreference(isLinearLayout)
        }
    }
    */
    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as FestiMobApplication)
                EditorViewModel(application.editorsRepository)
            }
        }
    }
}