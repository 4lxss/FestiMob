package com.example.festimob.games

// Small interface: the app asks here for games list, mechanism names, and creating a game. OnlineGamesRepository does the real HTTP.
interface GamesRepository {
    suspend fun getGames(
        editionId: String,
        category: String? = null,
        mechanism: String? = null
    ): List<Game>

    // Names for the mechanism filter dropdown.
    suspend fun getMechanismNames(): List<String>

    suspend fun createGame(
        name: String,
        description: String?,
        editeurId: Int,
        idE: Int?
    ): Game
}

