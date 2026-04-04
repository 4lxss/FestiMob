package com.example.festimob.data.api

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface APIService {
    @GET("api/festivals/all")
    suspend fun getFestivals() : List<Festival>

    @POST("api/festivals/add")
    suspend fun addFestival(@Body wrapper: FestivalAddWrapper): retrofit2.Response<FestivalAddResponse>
}