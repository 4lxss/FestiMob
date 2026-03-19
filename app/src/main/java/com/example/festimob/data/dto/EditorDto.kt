package com.example.festimob.data.remote.dto

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room3.Entity
import androidx.room3.PrimaryKey

@Entity(tableName = "editors")
data class EditorDto(
    @PrimaryKey(autoGenerate = false)
    val id : Int,
    val name : String,
    val state : String,
    val present : Boolean,
    val facture : String
    //to finish
)

