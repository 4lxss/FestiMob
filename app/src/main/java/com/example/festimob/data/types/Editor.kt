package com.example.festimob.data.types

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "editors")
data class Editor(
    @PrimaryKey(autoGenerate = false)
    val id : Int,
    val name : String,
    val state : String,
    val present : Boolean,
    val facture : String
    //to finish
)
