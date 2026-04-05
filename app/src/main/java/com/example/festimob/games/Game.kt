package com.example.festimob.games

/** One cell in the games grid. Filled from the REST API later; [imageUrl] may be empty for placeholders. */
data class Game(
    val id: String,
    val name: String,
    val imageUrl: String,
    val description: String
)

