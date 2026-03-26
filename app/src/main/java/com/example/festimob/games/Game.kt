package com.example.festimob.games

/**
 * This is the data we want to show in each square in the grid.
 *
 * Later, your API will probably return something like this (id, name, image url, etc.).
 */
data class Game(
    val id: String,
    val name: String,
    val imageUrl: String
)

