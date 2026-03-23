package com.example.festimob.ui.viewmodels

import com.example.festimob.data.api.FestivalDto

class APIStateViewModel {
}

sealed class UiState {
    object Loading: UiState()
    data class Success(val festivals: List<FestivalDto>): UiState()
    data class Error(val message: String): UiState()
}