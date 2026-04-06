package com.example.festimob.data.api.zone

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable


@Serializable
@Entity(tableName = "zonetarif")
data class ZoneTarif(
    @PrimaryKey
    val id_zt: Int,
    val name: String,
    val nb_table: Int,
    val price_table: Double,
    val price_m2: Double,
    val id_f: Int? = null
)

@Serializable
data class ZoneTarifAddWrapper(
    val zone: ZoneTarifAddRequest,
    val id: Int,
)

@Serializable
data class ZoneTarifAddRequest(
    val name: String,
    val nb_table: Int,
    val price_table: Double,
    val price_m2: Double,
    val id_f: Int? = null
)

@Serializable
data class FestivalDeleteRequest(
    val id: Int
)

@Serializable
data class ZoneDeleteRequest(
    val festivalId: Int
)
