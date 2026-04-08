package com.example.festimob.data.api.games

import com.example.festimob.data.api.APIService
import com.example.festimob.data.api.RetrofitInstance

// This class actually calls Retrofit: GET all jeux, filter in memory, map DTO → Game for Compose.
class OnlineGamesRepository(
    private val api: APIService = RetrofitInstance.api
) : GamesRepository {

    override suspend fun getGames(
        editionId: String,
        category: String?,
        mechanism: String?
    ): List<Game> {
        var rows = api.getJeux()

        // Optional: only games tied to this edition (id_e), unless we're in the "show everything" mode.
        val editionInt = editionId.toIntOrNull()
        if (editionId.isNotBlank() && editionId != "default" && editionInt != null) {
            rows = rows.filter { it.id_e == editionInt }
        }

        // Drop-in filter: keep jeux whose mechanism list contains this name (case-insensitive).
        if (!mechanism.isNullOrBlank()) {
            rows = rows.filter { jeu ->
                jeu.mecanisms.any { it.name.equals(mechanism, ignoreCase = true) }
            }
        }

        // Age group filter uses API age_min vs our Kids / Teenagers / Adults buckets.
        if (!category.isNullOrBlank()) {
            rows = rows.filter { jeu ->
                GameAgeCategory.matches(jeu.age_min, category)
            }
        }

        return rows.map { it.toGame() }
    }

    override suspend fun getMechanismNames(): List<String> {
        return api.getMecanisms()
            .map { it.name }
            .distinct()
            .sorted()
    }

    override suspend fun createGame(
        name: String,
        description: String?,
        editeurId: Int,
        idE: Int?
    ): Game {
        val trimmedName = name.trim()
        require(trimmedName.isNotEmpty()) { "Name is required" }
        require(editeurId > 0) { "Éditeur id is required (POST api/editeurs/{id}/jeux)" }

        val body = CreateJeuRequest(
            name = trimmedName,
            description = description?.trim()?.takeIf { it.isNotEmpty() },
            id_e = idE
        )
        // Backend wants POST under a specific publisher id, not a generic /jeux URL.
        return api.createJeuForEditeur(editeurId, body).toGame()
    }
}

// Turn one API row into what the grid + detail dialog need.
private fun JeuDto.toGame(): Game {
    return Game(
        id = id_j.toString(),
        name = name,
        imageUrl = image_url.orEmpty(),
        description = buildUserFacingDescription()
    )
}

// Builds the long text under the game: description first, then a one-line meta (players, age, time, mechanisms…).
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
