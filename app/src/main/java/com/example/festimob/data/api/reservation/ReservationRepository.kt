package com.example.festimob.data.api.reservation

interface ReservationRepository {

    suspend fun getReservations(festivalId: Int): List<Reservation>

    suspend fun addReservation(festivalId: Int, request: ReservationAddRequest): Reservation

    suspend fun editReservation(festivalId: Int, reservationId: Int, request: ReservationEditRequest)

    suspend fun deleteReservation(festivalId: Int, reservationId: Int)

    suspend fun addGames(festivalId: Int, reservationId: Int, jeux: List<JeuPlanRequest>)

    suspend fun getLogs(festivalId: Int): List<Log>

    suspend fun addLog(festivalId: Int, request: LogAddRequest): Log

    suspend fun getEditeurs(): List<Editeur>
}