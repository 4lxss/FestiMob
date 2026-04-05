package com.example.festimob.data.api

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT

interface APIService {
    @GET("api/festivals/all")
    suspend fun getFestivalsRaw(): List<FestivalNetwork>

    @GET("api/zoneTarif/getzones")
    suspend fun getAllZones(): List<ZoneTarif>

    @POST("api/festivals/add")
    suspend fun addFestival(@Body wrapper: FestivalAddWrapper): retrofit2.Response<FestivalAddResponse>

    @PUT("api/festivals/update-full")
    suspend fun updateFestival(@Body festival: FestivalUpdateRequest): retrofit2.Response<Unit>
}