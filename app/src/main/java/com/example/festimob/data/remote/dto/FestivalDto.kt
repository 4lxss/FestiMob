package com.example.festimob.data.remote.dto

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "festival")
data class Festival(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val price: Double,
    val quantity: Int
)

