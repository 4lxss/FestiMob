package com.example.festimob.data.api

class FestivalRepository {
    suspend fun getFestivals(): List<Festival> {
        return RetrofitInstance.api.getFestivals()
    }
}