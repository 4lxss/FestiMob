package com.example.festimob.data.api

import android.util.Log
import kotlinx.coroutines.flow.Flow

class OfflineFestivalRepository(
    private val festivalDao: FestivalDao,
    private val apiService: APIService
) : FestivalRepository {
    override fun getFestivals(): Flow<List<Festival>> = festivalDao.getAllFestivals()

    override fun getFestival(id_f: Int): Flow<Festival> = festivalDao.getFestival(id_f)

    suspend fun refreshFestivals(): Boolean {
        Log.d("REFRESH", "1. Début du refresh...")
        return try {
            val rawFestivals = apiService.getFestivalsRaw()
            Log.d("REFRESH", "2. Festivals reçus du réseau: ${rawFestivals.size}")

            val allZones = apiService.getAllZones()
            Log.d("REFRESH", "3. Zones reçues du réseau: ${allZones.size}")

            if (rawFestivals.isEmpty()) {
                Log.w("REFRESH", "Attention: La liste des festivals est vide, le map ne tournera pas.")
            }

            val festivalsWithZones = rawFestivals.map { raw ->
                Log.d("REFRESH", "4. Tentative de fusion pour: ${raw.name}")
                val zonesForThisFestival = allZones.filter { it.id_f == raw.id_f }

                Log.d("FUSION_TRACE", "Festival: ${raw.name} (ID:${raw.id_f}) | Zones trouvées: ${zonesForThisFestival.size}")

                Festival(
                    id_f = raw.id_f,
                    name = raw.name,
                    start_date = raw.start_date,
                    end_date = raw.end_date,
                    nb_table_big = raw.nb_table_big,
                    nb_table_small = raw.nb_table_small,
                    nb_table_mairie = raw.nb_table_mairie,
                    nb_chair = raw.nb_chair,
                    nb_chair_mairie = raw.nb_chair_mairie,
                    public = raw.public,
                    price_multi_socket = raw.price_multi_socket,
                    zones = zonesForThisFestival
                )
            }

            Log.d("REFRESH", "5. Envoi de ${festivalsWithZones.size} éléments au DAO")
            festivalDao.resyncAll(festivalsWithZones)
            Log.d("REFRESH", "6. Fin du processus.")
            true
        } catch (e: Exception) {
            Log.e("REFRESH", "ERREUR FATALE: ${e.message}", e)
            false
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

    override suspend fun update(festival: FestivalUpdateRequest) {
        try {
            val response = apiService.updateFestival(festival)

            if (response.isSuccessful) {
                val festivalToSave = Festival(
                    id_f = festival.id_f,
                    name = festival.name,
                    start_date = festival.start_date,
                    end_date = festival.end_date,
                    nb_table_big = festival.nb_table_big,
                    nb_table_small = festival.nb_table_small,
                    nb_table_mairie = festival.nb_table_mairie,
                    nb_chair = festival.nb_chair,
                    nb_chair_mairie = festival.nb_chair_mairie,
                    public = true,
                    price_multi_socket = festival.price_multi_socket,
                    zones = festival.zones
                )

                festivalDao.update(festivalToSave)

                Log.d("API_DEBUG", "Festival mis à jour avec succès : ${festival.id_f}")
            } else {
                val errorBody = response.errorBody()?.string()
                Log.e("API_DEBUG", "Erreur serveur ${response.code()}: $errorBody")
                throw Exception("Erreur serveur : ${response.code()}")
            }
        } catch (e: Exception) {
            Log.e("API_DEBUG", "Erreur lors de l'update: ${e.message}")
            throw e
        }
    }

    override suspend fun delete(festival: Festival) = festivalDao.delete(festival)
}