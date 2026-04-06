package com.example.festimob.data.api

import kotlinx.serialization.Serializable

/** JSON item from `GET api/jeux/all` (game row + nested mechanisms). */
@Serializable
data class JeuDto(
    val id_j: Int,
    val name: String,
    val author: String? = null,
    val age_min: Int? = null,
    val nb_min_players: Int? = null,
    val nb_max_players: Int? = null,
    val time: Int? = null,
    val type_game: String? = null,
    val image_url: String? = null,
    val id_e: Int? = null,
    val notice: String? = null,
    val prototype: Boolean? = null,
    val description: String? = null,
    val video_rules: String? = null,
    val mecanisms: List<MecanismDto> = emptyList()
)

/** Mechanism embedded in a jeu, or a row from `GET api/mecanisms/all`. */
@Serializable
data class MecanismDto(
    val id_mecanism: Int,
    val name: String,
    val description: String? = null
)
