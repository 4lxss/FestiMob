package com.example.festimob.data.api.models.admin.user

import kotlinx.serialization.Serializable

@Serializable
data class UpdateUserRoleRequest(
    val role: String
)
