package com.example.festimob.games

/**
 * Repository = "the place we ask for data".
 *
 * - For now we will use a Fake repository (offline list).
 * - Later, we will use an Online repository (calls your API).
 */
interface GamesRepository {
    suspend fun getGames(
        editionId: String,
        category: String? = null,
        mechanism: String? = null
    ): List<Game>
}

