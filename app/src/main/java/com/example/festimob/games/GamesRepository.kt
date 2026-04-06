package com.example.festimob.games

/**
 * Repository = "the place we ask for data".
 *
 * [OnlineGamesRepository] is wired by default; it loads games from the REST API.
 */
interface GamesRepository {
    suspend fun getGames(
        editionId: String,
        category: String? = null,
        mechanism: String? = null
    ): List<Game>
}

