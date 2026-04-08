package com.example.festimob.ui.zoneplan

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.festimob.data.api.zone.ZonePlanCard
import com.example.festimob.data.api.zone.ZonePlanRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class ZonePlanUiState {
    object Loading : ZonePlanUiState()
    data class Success(val zones: List<ZonePlanCard>) : ZonePlanUiState()
    data class Error(val message: String) : ZonePlanUiState()
}

class ZonePlanListViewModel(
    private val zonePlanRepository: ZonePlanRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<ZonePlanUiState>(ZonePlanUiState.Loading)
    val uiState: StateFlow<ZonePlanUiState> = _uiState

    fun load() {
        viewModelScope.launch {
            _uiState.value = ZonePlanUiState.Loading
            try {
                val zonePlans = zonePlanRepository.getAllZonePlans()
                _uiState.value = ZonePlanUiState.Success(zonePlans)
            } catch (e: Exception) {
                _uiState.value = ZonePlanUiState.Error(
                    e.message ?: "Erreur lors de la récupération des zones de plan"
                )
            }
        }
    }
}
