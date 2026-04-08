package com.example.festimob.data.api.editor

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AddEditorRequest(
    val editeur: AddEditorPayload,
    val contact: AddEditorContactPayload
)

@Serializable
data class AddEditorPayload(
    val name: String,
    val street: String? = null,
    val city: String? = null,
    @SerialName("postalCode")
    val postalCode: String? = null,
    val country: String? = null
)

@Serializable
data class AddEditorContactPayload(
    @SerialName("last_name")
    val lastName: String,
    @SerialName("first_name")
    val firstName: String,
    val email: String? = null,
    val telephone: String? = null,
    val profession: String
)
