package com.example.festimob.data.api.games

// Lightweight model for the UI: what we show in the grid and in the detail popup (comes from JeuDto after mapping).
data class Game(
    val id: String,
    val name: String,
    val imageUrl: String,
    val description: String
)

