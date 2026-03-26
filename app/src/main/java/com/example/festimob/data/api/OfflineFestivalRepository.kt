package com.example.festimob.data.api

import android.util.Log
import kotlinx.coroutines.flow.Flow

class OfflineFestivalRepository(
    private val festivalDao: FestivalDao,
    private val apiService: APIService
) : FestivalRepository {
    override fun getFestivals(): Flow<List<Festival>> = festivalDao.getAllFestivals()

    override fun getFestival(id_f: Int): Flow<Festival> = festivalDao.getFestival(id_f)

    suspend fun refreshFestivals() {
        try {
            val festivalsFromNetwork = apiService.getFestivals()

            festivalDao.insertAll(festivalsFromNetwork)

        } catch (e: Exception) {
            Log.e("NetworkRepository", "Erreur réseau, utilisation du mode offline", e)
        }
    }

    override suspend fun insert(festival: Festival) = festivalDao.insert(festival)

    override suspend fun update(festival: Festival) = festivalDao.update(festival)

    override suspend fun delete(festival: Festival) = festivalDao.delete(festival)
}