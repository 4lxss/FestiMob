package com.example.festimob.ui.home

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.festimob.data.api.festival.Festival
import com.example.festimob.data.api.festival.FestivalRepository
import com.example.festimob.ui.utils.formatIsoNative
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

/**
 * ViewModel for the Home screen.
 * It fetches festival data and handles the countdown logic for the featured banner.
 */
class HomeViewModel(
    private val festivalRepository: FestivalRepository
) : ViewModel() {
    // Internal state for the currently active or upcoming festival
    private val _currentFestival = mutableStateOf<Festival?>(null)
    val currentFestival: State<Festival?> = _currentFestival

    // Internal state for the human-readable countdown string
    private val _countdownMessage = mutableStateOf("Calcul...")
    val countdownMessage: State<String> = _countdownMessage

    // Loading state to show a progress indicator in the UI
    private val _isLoading = mutableStateOf(false)
    val isLoading: State<Boolean> = _isLoading

    init {
        loadCurrentFestival()
    }

    /**
     * Connects to the repository to collect the festival list and identify the current one.
     */
    fun loadCurrentFestival() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                festivalRepository.getFestivals().collect { festivals ->
                    val now = Date()
                    // Find a festival that hasn't ended yet, or pick the first available one
                    val active = festivals.firstOrNull {
                        it.end_date != null && parseDate(it.end_date).after(now)
                    } ?: festivals.firstOrNull()

                    _currentFestival.value = active
                    updateCountdown(active)
                    _isLoading.value = false
                }
            } catch (e: Exception) {
                _isLoading.value = false
            }
        }
    }

    /**
     * Calculates the remaining time or active status
     */
    private fun updateCountdown(festival: Festival?) {
        if (festival == null || festival.start_date == null) {
            _countdownMessage.value = ""
            return
        }

        val now = Date()
        val startDate = parseDate(festival.start_date)
        val endDate = parseDate(festival.end_date ?: "")

        // Calculate the difference in days
        val diff = startDate.time - now.time
        val days = diff / (1000 * 60 * 60 * 24)

        // Select the appropriate label based on the date range
        _countdownMessage.value = when {
            now.after(startDate) && now.before(endDate) -> "En cours !"
            days > 0 -> "J-$days"
            days == 0L -> "C'est demain !"
            else -> "Terminé"
        }
    }
    /**
     * Utility function to convert date strings from the API into Date objects.
     */
    private fun parseDate(dateStr: String): Date {
        return try {
            // Standard format used by the API for storage and filtering
            SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(dateStr) ?: Date()
        } catch (e: Exception) {
            Date()
        }
    }
}