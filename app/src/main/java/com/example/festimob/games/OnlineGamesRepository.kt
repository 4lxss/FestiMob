package com.example.festimob.games

/**
 * Online version (future).
 *
 * When you get the backend URL + endpoints, you will implement this class.
 * Example (not implemented yet):
 * - GET https://your-api.com/editions/{editionId}/games?category=...&mechanism=...
 */
class OnlineGamesRepository(
    private val baseUrl: String
) : GamesRepository {
    override suspend fun getGames(
        editionId: String,
        category: String?,
        mechanism: String?
    ): List<Game> {
        // TODO: Replace with real HTTP call (Retrofit/Ktor/OkHttp) once you have the API.
        // For now, we throw so you don’t accidentally use this before it's ready.
        throw NotImplementedError("Online API not wired yet. Base URL: $baseUrl")
    }
}

