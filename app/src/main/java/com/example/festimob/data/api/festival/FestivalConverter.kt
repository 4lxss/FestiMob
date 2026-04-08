package com.example.festimob.data.api.festival

import androidx.room.TypeConverter
import android.util.Log
import com.example.festimob.data.api.reservation.JeuPlan
import com.example.festimob.data.api.reservation.ReservationZone
import com.example.festimob.data.api.zone.ZoneTarif
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlin.collections.emptyList

class FestivalConverters {
    private val json = Json {
        ignoreUnknownKeys = true
        coerceInputValues = true
    }

    @TypeConverter
    fun fromZoneList(value: List<ZoneTarif>?): String {
        return try {
            json.encodeToString(value ?: emptyList())
        } catch (e: Exception) {
            Log.e("ROOM_CONVERTER", "Erreur encodage: ${e.message}")
            "[]"
        }
    }

    @TypeConverter
    fun toZoneList(value: String?): List<ZoneTarif> {
        if (value.isNullOrBlank()) return emptyList()
        return try {
            json.decodeFromString<List<ZoneTarif>>(value)
        } catch (e: Exception) {
            Log.e("ROOM_CONVERTER", "Erreur décodage: ${e.message}")
            emptyList()
        }
    }

    @TypeConverter
    fun fromResZoneList(value: List<ReservationZone>?): String = json.encodeToString(value ?: emptyList())

    @TypeConverter
    fun toResZoneList(value: String?): List<ReservationZone> =
        if (value.isNullOrBlank()) emptyList() else json.decodeFromString(value)

    @TypeConverter
    fun fromJeuPlanList(value: List<JeuPlan>?): String = json.encodeToString(value ?: emptyList())

    @TypeConverter
    fun toJeuPlanList(value: String?): List<JeuPlan> =
        if (value.isNullOrBlank()) emptyList() else json.decodeFromString(value)

}