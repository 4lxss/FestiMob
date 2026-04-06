package com.example.festimob.data.api.models.zoneplan

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationException
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.doubleOrNull
import kotlinx.serialization.json.intOrNull
import kotlinx.serialization.json.longOrNull
import kotlinx.serialization.json.jsonPrimitive
import kotlinx.serialization.json.floatOrNull
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.JsonNames

@Serializable
data class ZonePlanResponse(
    val id_zp: Int,
    val name: String,
    val id_f: Int,
    val description: String? = null,
    val id_zt: Int? = null,
    @Serializable(with = FlexibleIntSerializer::class)
    val max_table: Int? = null,
    val placements: List<ZonePlanPlacementResponse> = emptyList()
)

@OptIn(ExperimentalSerializationApi::class)
@Serializable
data class ZonePlanPlacementResponse(
    val id_jp: Int? = null,
    val id_j: Int,
    val id_zp: Int,
    val id_r: Int? = null,
    @JsonNames("nb_tables", "bm_tables")
    @Serializable(with = FlexibleIntSerializer::class)
    val nb_tables: Int? = null,
    @Serializable(with = FlexibleIntSerializer::class)
    val nb_exemplaires: Int? = null
)

object FlexibleIntSerializer : KSerializer<Int?> {
    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("FlexibleIntSerializer", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: Int?) {
        if (value == null) {
            encoder.encodeNull()
        } else {
            encoder.encodeInt(value)
        }
    }

    override fun deserialize(decoder: Decoder): Int? {
        if (decoder !is JsonDecoder) {
            return decoder.decodeInt()
        }

        val primitive = decoder.decodeJsonElement().jsonPrimitive
        return parseInt(primitive)
    }

    private fun parseInt(primitive: JsonPrimitive): Int? {
        primitive.intOrNull?.let { return it }
        primitive.longOrNull?.let { return it.toInt() }
        primitive.doubleOrNull?.let { return it.toInt() }
        primitive.floatOrNull?.let { return it.toInt() }

        val content = primitive.toString().trim().trim('"')
        if (content.isBlank() || content.equals("null", ignoreCase = true)) return null

        content.toIntOrNull()?.let { return it }
        content.toDoubleOrNull()?.let { return it.toInt() }

        throw SerializationException("Impossible de parser en Int: $content")
    }
}
