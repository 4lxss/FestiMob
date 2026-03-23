package com.example.festimob.data.api

import android.os.Build
import androidx.annotation.RequiresApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.time.LocalDate
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.Date

// 1. Création du Serializer pour le format ISO avec Timezone
object LocalDateSerializer : KSerializer<LocalDate> {
    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("LocalDate", PrimitiveKind.STRING)

    @RequiresApi(Build.VERSION_CODES.O)
    override fun serialize(encoder: Encoder, value: LocalDate) {
        encoder.encodeString(value.format(DateTimeFormatter.ISO_LOCAL_DATE))
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun deserialize(decoder: Decoder): LocalDate {
        // Décode la chaîne complète "2026-04-19T00:00:00.000Z" et convertit en LocalDate
        return ZonedDateTime.parse(decoder.decodeString()).toLocalDate()
    }
}

@Serializable
data class FestivalDto(
    val id_f: Int,
    val name: String,
    @Serializable(with = LocalDateSerializer::class)
    val start_date: LocalDate,
    @Serializable(with = LocalDateSerializer::class)
    val end_date: LocalDate,
    val nb_table_big: Int,
    val nb_table_small: Int,
    val nb_table_mairie: Int,
    val nb_chair: Int,
    val nb_chair_mairie: Int,
    val public:	Boolean,
    val price_multi_socket: Int
)