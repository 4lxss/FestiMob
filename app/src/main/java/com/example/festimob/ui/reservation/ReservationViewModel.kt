package com.example.festimob.ui.reservation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.festimob.data.api.reservation.Editeur
import com.example.festimob.data.api.reservation.Log
import com.example.festimob.data.api.reservation.LogAddRequest
import com.example.festimob.data.api.reservation.Reservation
import com.example.festimob.data.api.reservation.ReservationAddRequest
import com.example.festimob.data.api.reservation.ReservationEditRequest
import com.example.festimob.data.api.reservation.ReservationRepository
import com.example.festimob.data.api.reservation.ReservationZoneRequest
import com.example.festimob.data.api.zone.ZoneTarif
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

data class ReservationUiState(
    val reservations: List<Reservation> = emptyList(),
    val logs: List<Log> = emptyList(),
    val editeurs: List<Editeur> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val successMessage: String? = null,
    val festivalZones: List<ZoneTarif> = emptyList(),
    val festivalName: String = "Chargement...",

)

class ReservationViewModel(
    private val repository: ReservationRepository,
    private val festivalRepository: com.example.festimob.data.api.FestivalRepository,
    private val festivalId: Int,
    private val currentUserId: Int
) : ViewModel() {

    var uiState by mutableStateOf(ReservationUiState())
        private set

    init {
        loadAll()
    }

    fun loadAll() {
        viewModelScope.launch {
            uiState = uiState.copy(isLoading = true, errorMessage = null)
            try {
                val reservations = repository.getReservations(festivalId)
                val logs = repository.getLogs(festivalId)
                val editeurs = repository.getEditeurs()
                val zones = festivalRepository.getZonesByFestival(festivalId)
                val festival = festivalRepository.getFestival(festivalId).first()

                uiState = uiState.copy(
                    festivalName = festival.name,
                    reservations = reservations,
                    logs = logs,
                    editeurs = editeurs,
                    isLoading = false,
                    festivalZones = zones,
                )
            } catch (e: Exception) {
                uiState = uiState.copy(
                    isLoading = false,
                    errorMessage = "Erreur de chargement : ${e.localizedMessage}"
                )
            }
        }
    }

    fun addReservation(
        id_u: Int?,
        nameR: String,
        typeReservation: String,
        nbChair: Int,
        multiSocket: Double,
        state: String,
        totalPrice: Double,
        idReservant: Int?,
        zones: List<ReservationZoneRequest>,
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch {
            uiState = uiState.copy(isLoading = true, errorMessage = null)
            try {
                val request = ReservationAddRequest(
                    id_u = currentUserId,
                    id_reservant = idReservant,
                    state = state,
                    nb_chair = nbChair,
                    multi_socket = multiSocket,
                    type_reservation = typeReservation,
                    total_price = totalPrice,
                    name_r = nameR,
                    zones = zones
                )
                repository.addReservation(festivalId, request)
                loadAll()
                uiState = uiState.copy(successMessage = "Réservation ajoutée")
                onSuccess()
            } catch (e: Exception) {
                uiState = uiState.copy(
                    isLoading = false,
                    errorMessage = "Erreur : ${e.localizedMessage}"
                )
            }
        }
    }

    fun editReservation(
        reservationId: Int,
        nbChair: Int,
        multiSocket: Double,
        totalPrice: Double,
        state: String,
        idReservant: Int?,
        typeReservation: String,
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch {
            uiState = uiState.copy(isLoading = true, errorMessage = null)
            try {
                val request = ReservationEditRequest(
                    nb_chair = nbChair,
                    multi_socket = multiSocket,
                    total_price = totalPrice,
                    state = state,
                    id_reservant = idReservant,
                    id_u = currentUserId,
                    type_reservation = typeReservation
                )
                repository.editReservation(festivalId, reservationId, request)
                loadAll()
                uiState = uiState.copy(successMessage = "Réservation modifiée")
                onSuccess()
            } catch (e: Exception) {
                uiState = uiState.copy(
                    isLoading = false,
                    errorMessage = "Erreur : ${e.localizedMessage}"
                )
            }
        }
    }

    fun deleteReservation(reservationId: Int, onSuccess: () -> Unit) {
        viewModelScope.launch {
            uiState = uiState.copy(isLoading = true, errorMessage = null)
            try {
                repository.deleteReservation(festivalId, reservationId)
                loadAll()
                uiState = uiState.copy(successMessage = "Réservation supprimée")
                onSuccess()
            } catch (e: Exception) {
                uiState = uiState.copy(
                    isLoading = false,
                    errorMessage = "Erreur suppression : ${e.localizedMessage}"
                )
            }
        }
    }

    fun addLog(editeurId: Int, content: String, id_f: Int, onSuccess: () -> Unit) {
        viewModelScope.launch {
            uiState = uiState.copy(errorMessage = null)
            try {
                val request = LogAddRequest(
                    id_u = currentUserId,
                    content = content,
                    id_e = editeurId,
                    id_f = id_f
                )
                repository.addLog(festivalId, request)
                val logs = repository.getLogs(festivalId)
                uiState = uiState.copy(logs = logs, successMessage = "Log ajouté")
                onSuccess()
            } catch (e: Exception) {
                uiState = uiState.copy(errorMessage = "Erreur log : ${e.localizedMessage}")
            }
        }
    }

    fun resetMessages() {
        uiState = uiState.copy(errorMessage = null, successMessage = null)
    }


}