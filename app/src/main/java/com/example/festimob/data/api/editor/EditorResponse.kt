package com.example.festimob.data.api.editor

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class EditorResponse(
    @SerialName("id_e")
    val id: Int,
    val name: String,
    val street: String? = null,
    val city: String? = null,
    val country: String? = null,
    @SerialName("postal_code")
    val postalCode: String? = null,
    val presence: Boolean? = null,
    val facture: String? = null,
    @SerialName("image_url")
    val imageUrl: String? = null,
    @SerialName("imageUrl")
    val imageUrlAlt: String? = null
)
