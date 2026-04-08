package com.example.festimob.ui.festival


import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.StateFlow
import androidx.lifecycle.viewModelScope
import com.example.festimob.data.api.festival.FestivalRepository
import com.example.festimob.data.api.festival.OfflineFestivalRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/**
 * ViewModel to retrieve, update and delete an item from the [ItemsRepository]'s data source.
 */
class FestivalDetailsViewModel(
    private val festivalRepository: FestivalRepository,
    private val festivalId: Int
) : ViewModel() {
    private var _isOnline = mutableStateOf(false)
    val isOnline: State<Boolean> = _isOnline
    val uiState: StateFlow<FestivalDetailsUiState> =
        festivalRepository.getFestival(festivalId)
            .filterNotNull()
            .map {
                FestivalDetailsUiState(outOfStock = it.price_multi_socket <= 0, festivalDetails = it.toFestivalDetails())
            }.stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = FestivalDetailsUiState()
            )

    fun checkConnection() {
        viewModelScope.launch {
            val repo = (festivalRepository as? OfflineFestivalRepository)
            // On utilise refreshFestivals pour vérifier si le wifi répond
            val success = repo?.refreshFestivals() ?: false
            _isOnline.value = success
        }
    }

    suspend fun deleteFestival() {
        festivalRepository.delete(uiState.value.festivalDetails.toFestival())
    }

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }
}

/**
 * UI state for ItemDetailsScreen
 */

data class FestivalDetailsUiState(
    val outOfStock: Boolean = true,
    val festivalDetails: FestivalDetails = FestivalDetails()
)