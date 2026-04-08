package com.example.festimob.data.api.reservation

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

// ─── Réservation ───────────────────────────────────────────────

@Serializable
@Entity(tableName = "reservation")
data class Reservation(
    @PrimaryKey
    val id_r: Int,
    val id_f: Int,
    val id_u: Int,
    val id_reservant: Int? = null,
    val name_r: String,
    val type_reservation: String,
    val nb_chair: Int,
    val multi_socket: Double,
    val state: String,
    val total_price: Double,
    val zones: List<ReservationZone> = emptyList(),
    val jeux: List<JeuPlan> = emptyList()
)

@Serializable
data class ReservationZone(
    val id_zt: Int,
    val nb_table: Int,
    val nom_zone: String = ""
)

@Serializable
data class JeuPlan(
    val id_j: Int,
    val id_zp: Int,
    val id_r: Int? = null,
    val nb_tables: Double,
    val nb_exemplaires: Int,
    val name: String = ""
)

// ─── Requêtes ──────────────────────────────────────────────────

@Serializable
data class ReservationAddRequest(
    val id_u: Int?,
    val id_reservant: Int?,
    val state: String,
    val nb_chair: Int,
    val multi_socket: Double,
    val type_reservation: String,
    val total_price: Double,
    val name_r: String,
    val zones: List<ReservationZoneRequest>
)

@Serializable
data class ReservationZoneRequest(
    val id_zt: Int,
    val nb_table: Int
)

@Serializable
data class ReservationEditRequest(
    val nb_chair: Int,
    val multi_socket: Double,
    val total_price: Double,
    val state: String,
    val id_reservant: Int?,
    val id_u: Int,
    val type_reservation: String,
    val nb_table: Int = 0,
    val id_zt: Int? = null
)

@Serializable
data class AddGamesRequest(
    val selectedJeux: List<JeuPlanRequest>
)

@Serializable
data class JeuPlanRequest(
    val id_j: Int,
    val nb_exemplaires: Int,
    val nb_tables_occupees: Double
)

@Serializable
data class LogAddRequest(
    val id_u: Int,
    val content: String,
    val id_e: Int,
    val id_f: Int,
)

@Serializable
data class Log(
    val id_l: Int = 0,
    val id_e: Int,
    val id_f: Int,
    val id_u: Int,
    val content: String,
    val date: String = ""
)

// ─── Éditeur pour liste ────────────────────────

@Serializable
@Entity(tableName = "editeur")
data class Editeur(
    @PrimaryKey
    val id_e: Int,
    val name: String,
    val street: String? = null,
    val city: String? = null,
    val postal_code: String? = null,
    val country: String? = null,
    val state: String? = null
)