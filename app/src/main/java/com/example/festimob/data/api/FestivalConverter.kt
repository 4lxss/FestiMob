package com.example.festimob.data.api

import androidx.room.TypeConverter
import android.util.Log
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

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
}