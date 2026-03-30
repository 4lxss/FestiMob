package com.example.festimob.data.api

import retrofit2.http.GET

interface APIService {
    @GET("api/festivals/all")
    suspend fun getFestivals() : List<Festival>

    @GET("api/admin/users")
    suspend fun getUsers(): List<User>
}
