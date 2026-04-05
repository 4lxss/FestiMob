package com.example.festimob.data.api

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable


@Serializable
@Entity(tableName = "zonetarif")
data class ZoneTarif(
    val id_zt: Int = 0,
    val name: String,
    val nb_table: Int,
    val price_table: Double,
    val price_m2: Double,
    val id_f: Int? = null
)
