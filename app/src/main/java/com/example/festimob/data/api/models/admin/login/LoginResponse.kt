package com.example.festimob.data.api.models.admin.login

import kotlinx.serialization.Serializable

@Serializable
data class LoginResponse(
    val message: String = "",
    val user: LoginUser? = null
)
