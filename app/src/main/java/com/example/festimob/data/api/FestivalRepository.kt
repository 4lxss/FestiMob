package com.example.festimob.data.api

import com.example.festimob.data.api.zone.ZoneTarif
import com.example.festimob.data.api.zone.ZoneTarifAddRequest
import kotlinx.coroutines.flow.Flow

interface FestivalRepository {
    fun getFestivals(): Flow<List<Festival>>

    fun getFestival(id_f: Int): Flow<Festival>

    suspend fun insert(festival: FestivalAddRequest, zones: List<ZoneTarif> = emptyList()) {}

    suspend fun update(festival: FestivalUpdateRequest) {}

    suspend fun delete(festival: Festival)

    suspend fun addZone(zone: ZoneTarifAddRequest, festivalId: Int) {}

    suspend fun getZonesByFestival(festivalId: Int): List<ZoneTarif>

}