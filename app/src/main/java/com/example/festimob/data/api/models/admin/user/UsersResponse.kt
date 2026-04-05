package com.example.festimob.data.api.models.admin.user

import com.example.festimob.data.api.User
import kotlinx.serialization.Serializable

@Serializable
data class UsersResponse(
    val users: List<User>
)
