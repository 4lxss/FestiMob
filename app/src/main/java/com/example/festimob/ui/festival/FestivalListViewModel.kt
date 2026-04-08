package com.example.festimob.ui.festival

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.festimob.R
import com.example.festimob.data.api.UserPreferencesRepository
import com.example.festimob.data.api.festival.Festival
import com.example.festimob.data.api.festival.FestivalRepository
import com.example.festimob.data.api.festival.OfflineFestivalRepository
import com.example.festimob.ui.viewmodels.UiState
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

/**
 * ViewModel responsible for managing the festival list, search logic, and layout preferences.
 */
class FestivalListViewModel(
    private val userPreferencesRepository: UserPreferencesRepository,
    private val festivalRepository: FestivalRepository
) : ViewModel() {
    // Tracks if the app is currently connected to the network
    private var _isOnline = mutableStateOf(false)
    val isOnline: State<Boolean> = _isOnline

    // Main UI state (Loading, Success, or Error)
    private var internalState : MutableState<UiState> = mutableStateOf(UiState.Loading)
    val state : State<UiState> = internalState

    // Current search string typed by the user
    private val _searchQuery = mutableStateOf("")
    val searchQuery: State<String> = _searchQuery

    /**
     * UI state flow for layout preferences (Linear vs Grid).
     * Uses stateIn to survive configuration changes like screen rotation.
     */
    val uiState: StateFlow<FestivalListUiState> =
        userPreferencesRepository.isLinearLayout.map { isLinearLayout ->
            FestivalListUiState(isLinearLayout)
        }.stateIn(
            scope = viewModelScope,
            // Flow is set to emits value for when app is on the foreground
            // 5 seconds stop delay is added to ensure it flows continuously
            // for cases such as configuration change
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = runBlocking {
                FestivalListUiState(
                    isLinearLayout = userPreferencesRepository.isLinearLayout.first()
                )
            }
        )

    /**
     * Saves the user's preferred layout (List or Grid) to local storage.
     */
    fun selectLayout(isLinearLayout: Boolean) {
        viewModelScope.launch {
            userPreferencesRepository.saveLayoutPreference(isLinearLayout)
        }
    }

    /**
     * Updates the search query and triggers a filter on the list.
     */
    fun onSearchQueryChange(newQuery: String) {
        _searchQuery.value = newQuery
    }

    /**
     * Computed property that filters the festival list based on the search query.
     */
    val filteredFestivals: List<Festival>
        get() {
            val currentStats = state.value
            return if (currentStats is UiState.Success) {
                if (searchQuery.value.isEmpty()) {
                    currentStats.festivals
                } else {
                    currentStats.festivals.filter {
                        it.name.contains(searchQuery.value, ignoreCase = true)
                    }
                }
            } else emptyList()
        }

    // Initialize data observation and refresh on creation
    init {
        observeFestivals()
        refreshData()
    }

    /**
     * Collects festival data from the repository and updates the internal state.
     */
    private fun observeFestivals() {
        viewModelScope.launch {
            festivalRepository.getFestivals().collect { festivals ->
                festivals.forEach { f ->
                    Log.d("CHECK_DATA", "Festival: ${f.name}, Nb Zones: ${f.zones.size}")
                }
                internalState.value = UiState.Success(festivals)
            }
        }
    }

    /**
     * Forces a data refresh from the remote API, typically used for pull-to-refresh.
     */
    fun refreshData() {
        viewModelScope.launch {
            try {
                if (state.value !is UiState.Success) internalState.value = UiState.Loading

                val repo = (festivalRepository as? OfflineFestivalRepository)
                val success = repo?.refreshFestivals() ?: false
                _isOnline.value = success
            } catch (e: Exception) {
                if (state.value !is UiState.Success) {
                    internalState.value = UiState.Error("Erreur: ${e.message}")
                }
            }
        }
    }
}

/**
 * Data class for layout-specific UI states like icons and accessibility descriptions.
 */
data class FestivalListUiState(
    val isLinearLayout: Boolean = true,
    val toggleContentDescription: Int =
        if (isLinearLayout) R.string.grid_layout_toggle else R.string.linear_layout_toggle,
    val toggleIcon: Int =
        if (isLinearLayout) R.drawable.ic_grid_layout else R.drawable.ic_linear_layout
)

/**
 * Centered loading spinner shown during data fetching.
 */
@Composable
fun LoadingView() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator()
    }
}

/**
 * Error message display when a data fetch fails.
 */
@Composable
fun ErrorView(message: String) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(text=message, color=Color.Red)
    }
}

/**
 * Scrollable list of festival cards.
 */
@Composable
fun FestivalList(festivals: List<Festival>, modifier : Modifier) {
    LazyColumn(
        modifier = modifier.fillMaxSize().padding(8.dp)
    ) {
        items(items=festivals) {festival ->
            Card(
                modifier=modifier.fillMaxWidth().padding(6.dp)
            ) {
                Column(
                    modifier = modifier.padding(12.dp)
                ) {
                    Text(
                        text=festival.name,
                        color = Color.Red
                    )
                    HorizontalDivider()
                    Text(text=festival.id_f.toString())
                }
            }
        }
    }
}