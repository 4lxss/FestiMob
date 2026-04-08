package com.example.festimob.ui.festival

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.festimob.data.api.Festival
import com.example.festimob.data.api.FestivalAddRequest
import com.example.festimob.data.api.FestivalRepository
import com.example.festimob.data.api.FestivalUpdateRequest
import com.example.festimob.data.api.zone.ZoneTarif
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.text.NumberFormat
import com.example.festimob.ui.utils.formatIsoToInput

/**
 * ViewModel to validate and insert items in the Room database.
 */
class FestivalEntryViewModel(
    private val festivalId: Int,
    private val festivalRepository: FestivalRepository) : ViewModel() {

    /**
     * Holds current festival ui state
     */
    var festivalUiState by mutableStateOf(FestivalUiState())
        private set

    fun loadFestivalData(id: Int) {
        viewModelScope.launch {
            festivalRepository.getFestival(id)
                .filterNotNull()
                .first()
                .let { festival ->
                    Log.d("DEBUG", "Chargement du festival ID: ${festival.id_f}")
                    festivalUiState = festival.toFestivalUiState(true)
                }
        }
    }

    fun resetToDefault() {
        festivalUiState = FestivalUiState(
            festivalDetails = FestivalDetails(),
            isEntryValid = false
        )
    }

    /**
     * Updates the [festivalUiState] with the value provided in the argument. This method also triggers
     * a validation for input values.
     */
    fun updateUiState(festivalDetails: FestivalDetails) {
        festivalUiState =
            FestivalUiState(festivalDetails = festivalDetails, isEntryValid = validateInput(festivalDetails))
    }

    private fun validateInput(uiState: FestivalDetails = festivalUiState.festivalDetails): Boolean {
        val areZonesValid = uiState.zones.isNotEmpty() && uiState.zones.all { it.name.isNotBlank() }
        return with(uiState) {
            name.isNotBlank() &&
                    price_multi_socket.isNotBlank() &&
                    nb_table_big.isNotBlank() &&
                    nb_table_small.isNotBlank() &&
                    nb_table_mairie.isNotBlank() &&
                    nb_chair.isNotBlank() &&
                    nb_chair_mairie.isNotBlank() &&
                    start_date.isNotBlank() &&
                    end_date.isNotBlank() &&
                    areZonesValid
        }
    }

    fun resetErrorMessage() {
        festivalUiState = festivalUiState.copy(errorMessage = null)
    }

    suspend fun saveFestival(): Boolean {
        if (!validateInput()) {
            festivalUiState = festivalUiState.copy(errorMessage = "Veuillez remplir tous les champs.")
            return false
        }

        return try {
            if (festivalId == 0) {
                festivalRepository.insert(
                    festival = festivalUiState.festivalDetails.toFestivalAddRequest(),
                    zones = festivalUiState.festivalDetails.zones
                )
            } else {
                festivalRepository.update(festivalUiState.festivalDetails.toFestivalUpdateRequest())
            }
            festivalUiState = festivalUiState.copy(errorMessage = null)
            true
        } catch (e: Exception) {
            val errorMsg = when {
                e.message?.contains("500") == true -> "Erreur serveur (500)."
                e is java.net.UnknownHostException -> "Pas de connexion internet."
                else -> "Échec : ${e.localizedMessage}"
            }
            festivalUiState = festivalUiState.copy(errorMessage = errorMsg)
            false
        }
    }

    fun addZone() {
        val currentZones = festivalUiState.festivalDetails.zones.toMutableList()
        currentZones.add(ZoneTarif(id_zt = 0, name = "Nouvelle Zone", nb_table = 1, price_table = 5.00, price_m2 = 1.00))
        updateUiState(festivalUiState.festivalDetails.copy(zones = currentZones))
    }

    fun removeZone(index: Int) {
        val currentZones = festivalUiState.festivalDetails.zones.toMutableList()
        if (currentZones.size > 1) {
            currentZones.removeAt(index)
            updateUiState(festivalUiState.festivalDetails.copy(zones = currentZones))
        }
    }

    fun updateZone(index: Int, updatedZone: ZoneTarif) {
        val currentZones = festivalUiState.festivalDetails.zones.toMutableList()
        currentZones[index] = updatedZone
        updateUiState(festivalUiState.festivalDetails.copy(zones = currentZones))
    }
}

/**
 * Represents Ui State for an Item.
 */
data class FestivalUiState(
    val festivalDetails: FestivalDetails = FestivalDetails(),
    val isEntryValid: Boolean = false,
    val errorMessage: String? = null
)

data class FestivalDetails(
    val id_f: Int = 0,
    val name: String = "",
    val start_date: String = "",
    val end_date: String = "",
    val nb_table_big: String = "",
    val nb_table_small: String = "",
    val nb_table_mairie: String = "",
    val nb_chair: String = "",
    val nb_chair_mairie: String = "",
    val public:	Boolean = false,
    val price_multi_socket: String = "",
    val zones: List<ZoneTarif> = listOf(ZoneTarif(id_zt = 0, name = "Zone A", nb_table = 0, price_table = 0.00, price_m2 = 0.00))
)

/**
 * Extension function to convert [ItemDetails] to [Item]. If the value of [ItemDetails.price] is
 * not a valid [Double], then the price will be set to 0.0. Similarly if the value of
 * [ItemDetails.quantity] is not a valid [Int], then the quantity will be set to 0
 */
fun FestivalDetails.toFestivalAddRequest(): FestivalAddRequest = FestivalAddRequest(
    name = name,
    start_date = "${start_date}T00:00:00.000Z",
    end_date = "${end_date}T00:00:00.000Z",
    nb_table_big = nb_table_big.toIntOrNull() ?: 0,
    nb_table_small = nb_table_small.toIntOrNull() ?: 0,
    nb_table_mairie = nb_table_mairie.toIntOrNull() ?: 0,
    nb_chair = nb_chair.toIntOrNull() ?: 0,
    nb_chair_mairie = nb_chair_mairie.toIntOrNull() ?: 0,
    public = public,
    price_multi_socket = price_multi_socket.toIntOrNull() ?: 0
)

fun FestivalDetails.toFestival(): Festival = Festival(
    id_f = id_f,
    name = name,
    start_date = start_date,
    end_date = end_date,
    nb_table_big = nb_table_big.toIntOrNull() ?: 0,
    nb_table_small = nb_table_small.toIntOrNull() ?: 0,
    nb_table_mairie = nb_table_mairie.toIntOrNull() ?: 0,
    nb_chair = nb_chair.toIntOrNull() ?: 0,
    nb_chair_mairie = nb_chair_mairie.toIntOrNull() ?: 0,
    public = public,
    price_multi_socket = price_multi_socket.toIntOrNull() ?: 0,
    zones = zones,
)

fun Festival.formatedPrice(): String {
    return NumberFormat.getCurrencyInstance().format(price_multi_socket)
}

/**
 * Extension function to convert [Item] to [ItemUiState]
 */
fun Festival.toFestivalUiState(isEntryValid: Boolean = false): FestivalUiState = FestivalUiState(
    festivalDetails = this.toFestivalDetails(),
    isEntryValid = isEntryValid
)

/**
 * Extension function to convert [Item] to [ItemDetails]
 */
fun Festival.toFestivalDetails(): FestivalDetails = FestivalDetails(
    id_f = id_f,
    name = name,
    start_date = formatIsoToInput(start_date),
    end_date = formatIsoToInput(end_date),
    nb_table_big = nb_table_big.toString(),
    nb_table_small = nb_table_small.toString(),
    nb_table_mairie = nb_table_mairie.toString(),
    nb_chair = nb_chair.toString(),
    nb_chair_mairie = nb_chair_mairie.toString(),
    public = public,
    price_multi_socket = price_multi_socket.toString(),
    zones = this.zones
)

fun FestivalDetails.toFestivalUpdateRequest(): FestivalUpdateRequest = FestivalUpdateRequest(
    id_f = id_f,
    name = name,
    start_date = if (start_date.contains("T")) start_date else "${start_date}T00:00:00.000Z",
    end_date = if (end_date.contains("T")) end_date else "${end_date}T00:00:00.000Z",
    nb_table_big = nb_table_big.toIntOrNull() ?: 0,
    nb_table_small = nb_table_small.toIntOrNull() ?: 0,
    nb_table_mairie = nb_table_mairie.toIntOrNull() ?: 0,
    nb_chair = nb_chair.toIntOrNull() ?: 0,
    nb_chair_mairie = nb_chair_mairie.toIntOrNull() ?: 0,
    price_multi_socket = price_multi_socket.toIntOrNull() ?: 0,
    zones = zones
)




