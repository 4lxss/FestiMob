package com.example.festimob.ui.festival

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
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.festimob.FestiMobApplication
import com.example.festimob.R
import com.example.festimob.data.UserPreferencesRepository
import com.example.festimob.data.api.Festival
import com.example.festimob.data.api.FestivalRepository
import com.example.festimob.data.api.OfflineFestivalRepository
import com.example.festimob.ui.viewmodels.UiState
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class FestivalListViewModel(
    private val userPreferencesRepository: UserPreferencesRepository,
    private val festivalRepository: FestivalRepository
) : ViewModel() {
    private var internalState : MutableState<UiState> = mutableStateOf(UiState.Loading)
    val state : State<UiState> = internalState
    // UI states access for various [FestivalListUiState]
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

    /*
     * [selectLayout] change the layout and icons accordingly and
     * save the selection in DataStore through [userPreferencesRepository]
     */
    fun selectLayout(isLinearLayout: Boolean) {
        viewModelScope.launch {
            userPreferencesRepository.saveLayoutPreference(isLinearLayout)
        }
    }

    init {
        fetchFestivals()
    }

    private fun fetchFestivals() {
        viewModelScope.launch {
            try {
                // On lance le rafraîchissement réseau (via la méthode qu'on a créée dans NetworkRepository)
                // Si tu as gardé le nom "refreshFestivals"
                (festivalRepository as? OfflineFestivalRepository)?.refreshFestivals()

                // On observe les données qui viennent de Room
                festivalRepository.getFestivals().collect { festivals ->
                    internalState.value = UiState.Success(festivals)
                }
            } catch (e: Exception) {
                internalState.value = UiState.Error("Erreur: ${e.message}")
            }
        }
    }
}

/*
 * Data class containing various UI States for Dessert Release screens
 */
data class FestivalListUiState(
    val isLinearLayout: Boolean = true,
    val toggleContentDescription: Int =
        if (isLinearLayout) R.string.grid_layout_toggle else R.string.linear_layout_toggle,
    val toggleIcon: Int =
        if (isLinearLayout) R.drawable.ic_grid_layout else R.drawable.ic_linear_layout
)

@Composable
fun LoadingView() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator()
    }
}

@Composable
fun ErrorView(message: String) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(text=message, color=Color.Red)
    }
}

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