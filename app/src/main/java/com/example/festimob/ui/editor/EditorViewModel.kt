package com.example.festimob.ui.editor

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import com.example.festimob.FestiMobApplication
import com.example.festimob.R
import com.example.festimob.data.repositories.UserPreferencesRepository
import com.example.festimob.data.models.Editor
import com.example.festimob.data.repositories.EditorsRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class EditorUiState(
    val editorsList: List<Editor> = emptyList(),
    @param:StringRes val toggleContentDescription: Int = R.string.grid_layout,
    @param:DrawableRes val toggleIcon: Int = R.drawable.ic_grid,
    val isLinearLayout: Boolean = false
)

class EditorViewModel(
    private val editorsRepository: EditorsRepository,
    private val userPreferencesRepository: UserPreferencesRepository
) : ViewModel() {

    val uiState: StateFlow<EditorUiState> = combine(
        editorsRepository.getEditorsStream(),
        userPreferencesRepository.isLinearLayout
    ) { editors, isLinear ->
        // This block runs whenever either flow emits a new value
        EditorUiState(
            editorsList = editors,
            isLinearLayout = isLinear,
            // Logic for icons/descriptions based on the layout state
            toggleContentDescription = if (isLinear) R.string.grid_layout else R.string.linear_layout,
            toggleIcon = if (isLinear) R.drawable.ic_grid else R.drawable.ic_linear
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = EditorUiState(
            toggleContentDescription = R.string.linear_layout,
            toggleIcon = R.drawable.ic_linear
        )
    )

    /*
     * [selectLayout] change the layout and icons accordingly and
     * save the selection in DataStore through [userPreferencesRepository]
     */
    fun selectLayout(isLinearLayout: Boolean) {
        viewModelScope.launch {
            userPreferencesRepository.saveLayoutPreference(isLinearLayout)
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as FestiMobApplication)
                EditorViewModel(application.editorsRepository,application.userPreferencesRepository)
            }
        }
    }
}