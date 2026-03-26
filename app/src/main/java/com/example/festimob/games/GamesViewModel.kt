package com.example.festimob.games

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

/**
 * ViewModel = holds screen data (games list) and loads it.
 *
 * Later, when you have an API, you keep the same ViewModel and just swap the repository.
 */
class GamesViewModel(
    private val repository: GamesRepository = FakeGamesRepository()
) : ViewModel() {

    var isLoading by mutableStateOf(false)
        private set

    var errorMessage by mutableStateOf<String?>(null)
        private set

    var games by mutableStateOf<List<Game>>(emptyList())
        private set

    fun loadGames(
        editionId: String,
        category: String? = null,
        mechanism: String? = null
    ) {
        viewModelScope.launch {
            isLoading = true
            errorMessage = null

            try {
                games = repository.getGames(
                    editionId = editionId,
                    category = category,
                    mechanism = mechanism
                )
            } catch (t: Throwable) {
                errorMessage = t.message ?: "Something went wrong"
            } finally {
                isLoading = false
            }
        }
    }
}

