package com.example.festimob.data.remote.dto

import androidx.room3.Entity
import androidx.room3.PrimaryKey

@Entity(tableName = "items")
data class Item(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val price: Double,
    val quantity: Int
)

