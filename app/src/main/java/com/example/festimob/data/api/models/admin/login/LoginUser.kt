package com.example.festimob.data.api.models.admin.login

import kotlinx.serialization.Serializable

@Serializable
data class LoginUser(
    val username: String,
    val role: String
)
