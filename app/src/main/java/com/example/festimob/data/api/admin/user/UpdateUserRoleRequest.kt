package com.example.festimob.data.api.admin.user

import kotlinx.serialization.Serializable

@Serializable
data class UpdateUserRoleRequest(
    val role: String
)
