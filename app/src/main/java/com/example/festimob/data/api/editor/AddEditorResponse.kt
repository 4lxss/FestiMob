package com.example.festimob.data.api.editor

import kotlinx.serialization.Serializable

@Serializable
data class AddEditorResponse(
    val id: Int,
    val message: String
)
