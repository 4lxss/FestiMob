package com.example.festimob.data.api

import kotlinx.serialization.Serializable
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface APIService {
    @GET("api/festivals/all")
    suspend fun getFestivals() : List<Festival>

    @GET("api/admin/users")
    suspend fun getUsers(): UsersResponse

    @POST("api/auth/login")
    suspend fun login(@Body request: LoginRequest): LoginResponse

    @POST("api/auth/logout")
    suspend fun logout(): LogoutResponse

    @POST("api/auth/register")
    suspend fun register(@Body request: RegisterRequest): RegisterResponse
}

@Serializable
data class LoginRequest(
    val username: String,
    val password: String
)

@Serializable
data class LoginUser(
    val username: String,
    val role: String
)

@Serializable
data class LoginResponse(
    val message: String,
    val user: LoginUser? = null
)

@Serializable
data class LogoutResponse(
    val message: String
)

@Serializable
data class UsersResponse(
    val users: List<User>
)

@Serializable
data class RegisterRequest(
    val username: String,
    val password: String,
    val role: String
)

@Serializable
data class RegisterResponse(
    val message: String
)
