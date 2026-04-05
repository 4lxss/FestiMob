package com.example.festimob.games

import kotlinx.coroutines.delay

/**
 * Placeholder until the real REST API exists.
 *
 * TODO: Replace [getGames] with HTTP (Retrofit/Ktor) to:
 *   GET {baseUrl}/editions/{editionId}/games?category=...&mechanism=...
 */
class OnlineGamesRepository(
    private val baseUrl: String = PLACEHOLDER_BASE_URL
) : GamesRepository {

    init {
        require(baseUrl.isNotBlank()) { "baseUrl must not be blank" }
    }

    companion object {
        /** Swap this for your team’s real API base URL when ready. */
        const val PLACEHOLDER_BASE_URL = "https://api.example.com"
        private const val PLACEHOLDER_GRID_ITEMS = 12
    }

    override suspend fun getGames(
        editionId: String,
        category: String?,
        mechanism: String?
    ): List<Game> {
        // TODO: remove delay; use real network latency instead
        delay(300)

        // TODO: GET "$baseUrl/.../editions/$editionId/games" with category & mechanism query params
        val filterKey = listOfNotNull(category, mechanism).joinToString("_").ifEmpty { "all" }

        return List(PLACEHOLDER_GRID_ITEMS) { index ->
            Game(
                id = "placeholder_${editionId}_${filterKey}_$index",
                name = "Placeholder ${index + 1}",
                imageUrl = "",
                description = "Full description for this game will come from the API. " +
                    "This is placeholder text so you can test the detail popup."
            )
        }
    }
}
