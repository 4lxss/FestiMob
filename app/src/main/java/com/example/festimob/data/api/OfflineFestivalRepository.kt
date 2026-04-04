package com.example.festimob.data.api

import android.util.Log
import kotlinx.coroutines.flow.Flow

class OfflineFestivalRepository(
    private val festivalDao: FestivalDao,
    private val apiService: APIService
) : FestivalRepository {
    override fun getFestivals(): Flow<List<Festival>> = festivalDao.getAllFestivals()

    override fun getFestival(id_f: Int): Flow<Festival> = festivalDao.getFestival(id_f)

    suspend fun refreshFestivals() : Boolean {
        try {
            val festivalsFromNetwork = apiService.getFestivals()

            festivalDao.insertAll(festivalsFromNetwork)
            return true

        } catch (e: Exception) {
            Log.e("NetworkRepository", "Erreur réseau, utilisation du mode offline", e)
            return false
        }
    }

    override suspend fun insert(festival: FestivalAddRequest) {
        try {
            val wrapper = FestivalAddWrapper(festival)
            val response = apiService.addFestival(wrapper)

            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) {
                    val festivalToSave = Festival(
                        id_f = body.id_f,
                        name = festival.name,
                        start_date = festival.start_date,
                        end_date = festival.end_date,
                        nb_table_big = festival.nb_table_big,
                        nb_table_small = festival.nb_table_small,
                        nb_table_mairie = festival.nb_table_mairie,
                        nb_chair = festival.nb_chair,
                        nb_chair_mairie = festival.nb_chair_mairie,
                        public = festival.public,
                        price_multi_socket = festival.price_multi_socket
                    )

                    festivalDao.insert(festivalToSave)
                }
            } else {
                throw Exception("Erreur serveur : ${response.code()}")
            }
        } catch (e: Exception) {
            Log.e("API_DEBUG", "Erreur final: ${e.message}")
            throw e
        }
    }

    override suspend fun update(festival: Festival) = festivalDao.update(festival)

    override suspend fun delete(festival: Festival) = festivalDao.delete(festival)
}