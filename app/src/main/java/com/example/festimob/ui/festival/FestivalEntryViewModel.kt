package com.example.festimob.ui.festival

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.festimob.data.api.Festival
import com.example.festimob.data.api.FestivalAddRequest
import com.example.festimob.data.api.FestivalRepository
import java.text.NumberFormat

/**
 * ViewModel to validate and insert items in the Room database.
 */
class FestivalEntryViewModel(private val festivalRepository: FestivalRepository) : ViewModel() {

    /**
     * Holds current festival ui state
     */
    var festivalUiState by mutableStateOf(FestivalUiState())
        private set

    /**
     * Updates the [festivalUiState] with the value provided in the argument. This method also triggers
     * a validation for input values.
     */
    fun updateUiState(festivalDetails: FestivalDetails) {
        festivalUiState =
            FestivalUiState(festivalDetails = festivalDetails, isEntryValid = validateInput(festivalDetails))
    }

    private fun validateInput(uiState: FestivalDetails = festivalUiState.festivalDetails): Boolean {
        return with(uiState) {
            name.isNotBlank() &&
                    price_multi_socket.isNotBlank() &&
                    nb_table_big.isNotBlank() &&
                    nb_table_small.isNotBlank() &&
                    nb_table_mairie.isNotBlank() &&
                    nb_chair.isNotBlank() &&
                    nb_chair_mairie.isNotBlank() &&
                    start_date.isNotBlank() &&
                    end_date.isNotBlank()
        }
    }

    fun resetErrorMessage() {
        festivalUiState = festivalUiState.copy(errorMessage = null)
    }

    suspend fun saveFestival(): Boolean {
        return try {
            if (validateInput()) {
                festivalRepository.insert(festivalUiState.festivalDetails.toFestivalAddRequest())
                // Si réussi, on efface l'erreur
                festivalUiState = festivalUiState.copy(errorMessage = null)
                true
            } else {
                festivalUiState = festivalUiState.copy(errorMessage = "Veuillez remplir tous les champs.")
                false
            }
        } catch (e: Exception) {
            // On traduit l'erreur technique en message utilisateur
            val errorMsg = when {
                e.message?.contains("404") == true -> "Serveur introuvable (404)."
                e.message?.contains("500") == true -> "Erreur interne du serveur (500)."
                e is java.net.UnknownHostException -> "Pas de connexion internet."
                e is java.net.SocketTimeoutException -> "Le serveur met trop de temps à répondre."
                else -> "Échec de la sauvegarde : ${e.localizedMessage}"
            }
            festivalUiState = festivalUiState.copy(errorMessage = errorMsg)
            false
        }
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
    val price_multi_socket: String = ""
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
    price_multi_socket = price_multi_socket.toIntOrNull() ?: 0
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
    start_date = start_date,
    end_date = end_date,
    nb_table_big = nb_table_big.toString(),
    nb_table_small = nb_table_small.toString(),
    nb_table_mairie = nb_table_mairie.toString(),
    nb_chair = nb_chair.toString(),
    nb_chair_mairie = nb_chair_mairie.toString(),
    public = public,
    price_multi_socket = price_multi_socket.toString()
)
