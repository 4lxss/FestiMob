package com.example.festimob.data.api.zone

interface ZonePlanRepository {
    suspend fun getAllZonePlans(): List<ZonePlanCard>
}

data class ZonePlanCard(
    val id: Int,
    val name: String,
    val zoneTarifName: String,
    val description: String,
    val gameNames: List<String>,
    val tableCount: Int
)
