package com.example.festimob.data.api.festival

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.festimob.data.api.zone.ZoneTarif
import kotlinx.serialization.Serializable

@Serializable
@Entity(tableName = "festival")
data class Festival(
    @PrimaryKey(autoGenerate = true)
    val id_f: Int = 0,
    val name: String,
    val start_date: String,
    val end_date: String,
    val nb_table_big: Int,
    val nb_table_small: Int,
    val nb_table_mairie: Int,
    val nb_chair: Int,
    val nb_chair_mairie: Int,
    val public:	Boolean,
    val price_multi_socket: Int,
    val zones: List<ZoneTarif> = emptyList()
)

@Serializable
data class FestivalAddWrapper(
    val festival: FestivalAddRequest
)

@Serializable
data class FestivalAddResponse(
    val id_f: Int,
    val message: String
)

@Serializable
data class FestivalAddRequest(
    val name: String,
    val start_date: String,
    val end_date: String,
    val nb_table_big: Int,
    val nb_table_small: Int,
    val nb_table_mairie: Int,
    val nb_chair: Int,
    val nb_chair_mairie: Int,
    val public: Boolean,
    val price_multi_socket: Int
)

@Serializable
data class FestivalUpdateRequest(
    val id_f: Int = 0,
    val name: String,
    val start_date: String,
    val end_date: String,
    val nb_table_big: Int,
    val nb_table_small: Int,
    val nb_table_mairie: Int,
    val nb_chair: Int,
    val nb_chair_mairie: Int,
    val price_multi_socket: Int,
    val zones: List<ZoneTarif> = emptyList()
)

@Serializable
data class FestivalNetwork(
    val id_f: Int,
    val name: String,
    val start_date: String,
    val end_date: String,
    val nb_table_big: Int,
    val nb_table_small: Int,
    val nb_table_mairie: Int,
    val nb_chair: Int,
    val nb_chair_mairie: Int,
    val public: Boolean,
    val price_multi_socket: Int
)