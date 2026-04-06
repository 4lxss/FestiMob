package com.example.festimob.data.api.reservation

import com.example.festimob.data.api.APIService

import com.example.festimob.data.api.reservation.Log

class OfflineReservationRepository(
    private val api: APIService
) : ReservationRepository {

    override suspend fun getReservations(festivalId: Int): List<Reservation> {
        return try {
            api.getReservations(festivalId)
        } catch (e: Exception) {
            android.util.Log.e("RESERVATION", "Erreur chargement réservations: ${e.message}")
            throw e
        }
    }

    override suspend fun addReservation(festivalId: Int, request: ReservationAddRequest): Reservation {
        val response = api.addReservation(festivalId, request)
        if (response.isSuccessful) {
            return response.body() ?: throw Exception("Réponse vide")
        }
        throw Exception("Erreur serveur : ${response.code()}")
    }

    override suspend fun editReservation(festivalId: Int, reservationId: Int, request: ReservationEditRequest) {
        val response = api.editReservation(festivalId, reservationId, request)
        if (!response.isSuccessful) {
            throw Exception("Erreur modification : ${response.code()}")
        }
    }

    override suspend fun deleteReservation(festivalId: Int, reservationId: Int) {
        val response = api.deleteReservation(festivalId, reservationId)
        if (!response.isSuccessful) {
            throw Exception("Erreur suppression : ${response.code()}")
        }
    }

    override suspend fun addGames(festivalId: Int, reservationId: Int, jeux: List<JeuPlanRequest>) {
        val response = api.addGames(festivalId, reservationId, AddGamesRequest(jeux))
        if (!response.isSuccessful) {
            throw Exception("Erreur ajout jeux : ${response.code()}")
        }
    }

    override suspend fun getLogs(festivalId: Int): List<Log> {
        return api.getLogs(festivalId)
    }

    override suspend fun addLog(festivalId: Int, request: LogAddRequest): Log {
        val response = api.addLog(festivalId, request)
        if (response.isSuccessful) {
            return response.body() ?: throw Exception("Réponse vide")
        }
        throw Exception("Erreur ajout log : ${response.code()}")
    }

    override suspend fun getEditeurs(): List<Editeur> {
        return api.getEditeurs()
    }
}