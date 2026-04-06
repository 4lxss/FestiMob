package com.example.festimob.data.api

import retrofit2.http.GET

interface APIService {
    @GET("api/festivals/all")
    suspend fun getFestivals() : List<Festival>

    @GET("api/jeux/all")
    suspend fun getJeux(): List<JeuDto>

    @GET("api/mecanisms/all")
    suspend fun getMecanisms(): List<MecanismDto>
}