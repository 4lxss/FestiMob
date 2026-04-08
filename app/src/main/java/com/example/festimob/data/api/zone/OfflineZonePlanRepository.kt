package com.example.festimob.data.api.zone

import com.example.festimob.data.api.APIService

class OfflineZonePlanRepository(
    private val apiService: APIService
) : ZonePlanRepository {

    override suspend fun getAllZonePlans(): List<ZonePlanCard> {
        val zonePlans = apiService.getAllZonePlans()
        val zoneTarifs = apiService.getAllZones()
            .associateBy { it.id_zt }

        return zonePlans.map { zonePlan ->
            val associatedZoneTarifName = zonePlan.id_zt
                ?.let { zoneTarifs[it]?.name }
                ?: "Zone tarifaire non définie"

            val gameNames = zonePlan.placements.map { "Jeu #${it.id_j}" }
            val tableCountFromZoneTarif = zonePlan.id_zt?.let { zoneTarifs[it]?.nb_table }
            val tableCountFromPlacements = zonePlan.placements.sumOf { it.nb_tables ?: 0 }

            ZonePlanCard(
                id = zonePlan.id_zp,
                name = zonePlan.name,
                zoneTarifName = associatedZoneTarifName,
                description = zonePlan.description?.takeIf { it.isNotBlank() } ?: "Aucune description",
                gameNames = gameNames,
                tableCount = tableCountFromZoneTarif ?: zonePlan.max_table ?: tableCountFromPlacements
            )
        }
    }
}