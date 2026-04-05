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

    override suspend fun insert(festival: FestivalAddRequest, zones: List<ZoneTarif>) {
        try {
            val wrapper = FestivalAddWrapper(festival)
            val response = apiService.addFestival(wrapper)

            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) {
                    val festivalId = body.id_f

                    zones.forEach { zone ->
                        val zoneRequest = ZoneTarifAddRequest(
                            name = zone.name,
                            nb_table = zone.nb_table,
                            price_table = zone.price_table,
                            price_m2 = zone.price_m2
                        )
                        val zoneWrapper = ZoneTarifAddWrapper(zone = zoneRequest, id = festivalId)
                        val zoneResponse = apiService.addZone(zoneWrapper)

                        if (!zoneResponse.isSuccessful) {
                            Log.e("API_DEBUG", "Erreur ajout zone '${zone.name}': ${zoneResponse.code()}")
                        } else {
                            Log.d("API_DEBUG", "Zone '${zone.name}' ajoutée avec succès")
                        }
                    }

                    // 3. Sauvegarder le festival en local avec ses zones
                    val festivalToSave = Festival(
                        id_f = festivalId,
                        name = festival.name,
                        start_date = festival.start_date,
                        end_date = festival.end_date,
                        nb_table_big = festival.nb_table_big,
                        nb_table_small = festival.nb_table_small,
                        nb_table_mairie = festival.nb_table_mairie,
                        nb_chair = festival.nb_chair,
                        nb_chair_mairie = festival.nb_chair_mairie,
                        public = festival.public,
                        price_multi_socket = festival.price_multi_socket,
                        zones = zones
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

    override suspend fun delete(festival: Festival) {
        try {
            val zoneResponse = apiService.deleteZonesByFestival(
                ZoneDeleteRequest(festivalId = festival.id_f)
            )
            if (!zoneResponse.isSuccessful) {
                Log.e("API_DEBUG", "Erreur suppression zones: ${zoneResponse.code()}")
                throw Exception("Erreur suppression zones : ${zoneResponse.code()}")
            }
            Log.d("API_DEBUG", "Zones du festival ${festival.id_f} supprimées")

            val festivalResponse = apiService.deleteFestival(
                FestivalDeleteRequest(id = festival.id_f)
            )
            if (!festivalResponse.isSuccessful) {
                Log.e("API_DEBUG", "Erreur suppression festival: ${festivalResponse.code()}")
                throw Exception("Erreur suppression festival : ${festivalResponse.code()}")
            }
            Log.d("API_DEBUG", "Festival ${festival.id_f} supprimé du serveur")

            festivalDao.delete(festival)
            Log.d("API_DEBUG", "Festival ${festival.id_f} supprimé localement")

        } catch (e: Exception) {
            Log.e("API_DEBUG", "Erreur lors de la suppression: ${e.message}")
            throw e
        }
    }
}