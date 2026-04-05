package com.example.festimob.data.api

import kotlinx.coroutines.flow.Flow

interface FestivalRepository {
    fun getFestivals(): Flow<List<Festival>>

    fun getFestival(id_f: Int): Flow<Festival>

    suspend fun insert(festival: FestivalAddRequest) {}

    suspend fun update(festival: FestivalUpdateRequest) {}

    suspend fun delete(festival: Festival)

}