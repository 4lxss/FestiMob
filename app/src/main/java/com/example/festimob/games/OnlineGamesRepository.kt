package com.example.festimob.games

import com.example.festimob.data.api.APIService
import com.example.festimob.data.api.JeuDto
import com.example.festimob.data.api.RetrofitInstance

/**
 * Loads games from the backend via [APIService] and maps them to UI [Game] models.
 */
class OnlineGamesRepository(
    private val api: APIService = RetrofitInstance.api
) : GamesRepository {

    override suspend fun getGames(
        editionId: String,
        category: String?,
        mechanism: String?
    ): List<Game> {
        var rows = api.getJeux()

        val editionInt = editionId.toIntOrNull()
        if (editionId.isNotBlank() && editionId != "default" && editionInt != null) {
            rows = rows.filter { it.id_e == editionInt }
        }

        if (!mechanism.isNullOrBlank()) {
            rows = rows.filter { jeu ->
                jeu.mecanisms.any { it.name.equals(mechanism, ignoreCase = true) }
            }
        }

        // `category` in the UI does not yet map to API `type_game` codes; reserved for later.

        return rows.map { it.toGame() }
    }
}

private fun JeuDto.toGame(): Game {
    return Game(
        id = id_j.toString(),
        name = name,
        imageUrl = image_url.orEmpty(),
        description = buildUserFacingDescription()
    )
}

private fun JeuDto.buildUserFacingDescription(): String {
    val main = description?.trim()?.takeIf { it.isNotEmpty() }

    val meta = buildList {
        val playersLine = when {
            nb_min_players != null && nb_max_players != null ->
                "$nb_min_players–$nb_max_players players"
            nb_min_players != null -> "$nb_min_players+ players"
            else -> null
        }
        playersLine?.let { add(it) }
        age_min?.let { add("Age $it+") }
        time?.let { add("${it} min") }
        author?.trim()?.takeIf { it.isNotEmpty() }?.let { add("By $it") }
        if (mecanisms.isNotEmpty()) {
            add("Mechanisms: " + mecanisms.joinToString { it.name })
        }
        notice?.trim()?.takeIf { it.isNotEmpty() }?.let { add("Notice: $it") }
        if (prototype == true) add("Prototype")
    }.joinToString(" · ")

    return when {
        main != null && meta.isNotEmpty() -> "$main\n\n$meta"
        main != null -> main
        meta.isNotEmpty() -> meta
        else -> "No description yet for this game."
    }
}
