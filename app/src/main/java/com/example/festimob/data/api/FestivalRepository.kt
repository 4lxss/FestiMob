package com.example.festimob.data.api

class FestivalRepository {
    suspend fun getFestivals(): List<FestivalDto> {
        return RetrofitInstance.api.getFestivals()
    }
}