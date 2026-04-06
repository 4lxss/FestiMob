package com.example.festimob.data.api

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface APIService {
    @GET("api/festivals/all")
    suspend fun getFestivals() : List<Festival>

    @GET("api/jeux/all")
    suspend fun getJeux(): List<JeuDto>

    @GET("api/mecanisms/all")
    suspend fun getMecanisms(): List<MecanismDto>

    /**
     * Creates a jeu for an éditeur (publisher). See API doc: `POST /api/editeurs/:id/jeux`.
     * May require auth (cookies / Bearer) depending on server middleware.
     */
    @POST("api/editeurs/{editeurId}/jeux")
    suspend fun createJeuForEditeur(
        @Path("editeurId") editeurId: Int,
        @Body body: CreateJeuRequest
    ): JeuDto
}