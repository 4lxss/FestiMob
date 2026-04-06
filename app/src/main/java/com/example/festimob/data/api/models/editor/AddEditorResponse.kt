package com.example.festimob.data.api.models.editor

import kotlinx.serialization.Serializable

@Serializable
data class AddEditorResponse(
    val id: Int,
    val message: String
)
