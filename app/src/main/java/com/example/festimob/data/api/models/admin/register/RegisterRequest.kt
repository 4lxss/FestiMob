package com.example.festimob.data.api.models.admin.register

import kotlinx.serialization.Serializable

@Serializable
data class RegisterRequest(
    val username: String,
    val password: String,
    val role: String
)
