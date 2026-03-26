package com.example.festimob.games

import kotlinx.coroutines.delay

/**
 * Fake/offline data so you can build the UI without the backend.
 *
 * We use image URLs from picsum.photos so every game has a "picture".
 * (When there's no internet, images may not load, but the UI still works.)
 */
class FakeGamesRepository : GamesRepository {
    override suspend fun getGames(
        editionId: String,
        category: String?,
        mechanism: String?
    ): List<Game> {
        // Small delay so you can see "loading" in the UI (optional)
        delay(400)

        val base = listOf(
            "Catan",
            "Azul",
            "Codenames",
            "7 Wonders",
            "Carcassonne",
            "Splendor",
            "Dixit",
            "Ticket to Ride",
            "Love Letter",
            "Pandemic",
            "King of Tokyo",
            "Risk",
            "Chess",
            "Uno",
            "Monopoly",
            "Cluedo",
            "The Crew",
            "Wingspan",
            "Terraforming Mars",
            "Exploding Kittens"
        )

        // Note: category/mechanism are not used here yet. Later, you can filter for real.
        return base.mapIndexed { index, name ->
            val id = "${editionId}_${index + 1}"
            Game(
                id = id,
                name = name,
                imageUrl = "https://picsum.photos/seed/$id/400/400"
            )
        }
    }
}

