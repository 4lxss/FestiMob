package com.example.festimob.ui.games

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.festimob.data.api.games.Game
import com.example.festimob.data.api.games.GameAgeCategory
import com.example.festimob.data.api.games.GamesRepository
import com.example.festimob.data.api.games.OnlineGamesRepository
import kotlinx.coroutines.launch
import retrofit2.HttpException

// Talks to GamesRepository: keeps the list loading spinner, errors, and the filter dropdown options in sync for the UI.
class GamesViewModel(
    private val repository: GamesRepository = OnlineGamesRepository()
) : ViewModel() {

    var isLoading by mutableStateOf(false)
        private set

    var errorMessage by mutableStateOf<String?>(null)
        private set

    var games by mutableStateOf<List<Game>>(emptyList())
        private set

    var isSavingGame by mutableStateOf(false)
        private set

    // Age filter labels (Kids / Teenagers / Adults rules live in GameAgeCategory).
    val ageCategoryFilterOptions = GameAgeCategory.allOptions

    // Mechanism filter: "All" plus names we got from the API.
    var mechanismFilterOptions by mutableStateOf(listOf("All"))
        private set

    fun clearError() {
        errorMessage = null
    }

    // Fetches mechanism names once so the second dropdown has real options (falls back to just "All" if it fails).
    fun loadMechanismFilterOptions() {
        viewModelScope.launch {
            mechanismFilterOptions = try {
                listOf("All") + repository.getMechanismNames()
            } catch (_: Throwable) {
                listOf("All")
            }
        }
    }

    // Pulls games from the repo with optional age + mechanism filters; shows loading + sets errorMessage on failure.
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

    // Saves a new jeu via the API, then reloads the list with the same filters you had; onSuccess closes the dialog.
    fun createGame(
        name: String,
        description: String?,
        editeurId: Int,
        idE: Int?,
        listingEditionId: String,
        category: String?,
        mechanism: String?,
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch {
            isSavingGame = true
            errorMessage = null

            try {
                repository.createGame(
                    name = name,
                    description = description,
                    editeurId = editeurId,
                    idE = idE
                )
                games = repository.getGames(
                    editionId = listingEditionId,
                    category = category,
                    mechanism = mechanism
                )
                onSuccess()
            } catch (e: HttpException) {
                errorMessage = when (e.code()) {
                    401, 403 -> "Not allowed (HTTP ${e.code()}). This route may require login / admin."
                    404 -> "Route not found (404). Check éditeur id and POST api/editeurs/{id}/jeux."
                    else -> e.message() ?: "HTTP ${e.code()}"
                }
            } catch (t: Throwable) {
                errorMessage = t.message ?: "Could not create game"
            } finally {
                isSavingGame = false
            }
        }
    }
}
