package com.example.festimob.data.api

import com.example.festimob.data.api.models.admin.login.LoginRequest
import com.example.festimob.data.api.models.admin.login.LoginResponse
import com.example.festimob.data.api.models.admin.logout.LogoutResponse
import com.example.festimob.data.api.models.admin.register.RegisterRequest
import com.example.festimob.data.api.models.admin.register.RegisterResponse
import com.example.festimob.data.api.models.admin.user.DeleteUserResponse
import com.example.festimob.data.api.models.admin.user.UpdateUserRoleRequest
import com.example.festimob.data.api.models.admin.user.UpdateUserRoleResponse
import com.example.festimob.data.api.models.admin.user.UsersResponse
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.Path
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

    @PATCH("api/admin/users/{id}/role")
    suspend fun updateUserRole(
        @Path("id") userId: Int,
        @Body request: UpdateUserRoleRequest
    ): UpdateUserRoleResponse

    @DELETE("api/admin/users/{id}")
    suspend fun deleteUser(
        @Path("id") userId: Int
    ): DeleteUserResponse
}
