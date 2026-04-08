package com.example.festimob.data.api.admin.user

import com.example.festimob.data.api.user.User
import kotlinx.serialization.Serializable

@Serializable
data class UsersResponse(
    val users: List<User>
)
