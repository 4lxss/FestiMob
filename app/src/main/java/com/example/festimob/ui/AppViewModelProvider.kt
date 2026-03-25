package com.example.festimob.ui

import android.app.Application
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.festimob.FestiMobApplication
import com.example.festimob.ui.festival.FestivalEntryViewModel
import com.example.festimob.ui.festival.FestivalListViewModel

/**
 * Provides Factory to create instance of ViewModel for the entire Inventory app
 */
object AppViewModelProvider {
    val Factory = viewModelFactory {
        // Initializer for ItemEditViewModel
        initializer {
            FestivalListViewModel(
                userPreferencesRepository = festimobApplication().container.userPreferencesRepository,
                festivalRepository = festimobApplication().container.festivalRepository
            )
        }
        // Initializer for ItemEntryViewModel
        initializer {
            FestivalEntryViewModel(
                festivalRepository = festimobApplication().container.festivalRepository
            )
        }
    }
}

/**
 * Extension function to queries for [Application] object and returns an instance of
 * [InventoryApplication].
 */
fun CreationExtras.festimobApplication(): FestiMobApplication =
    (this[AndroidViewModelFactory.APPLICATION_KEY] as FestiMobApplication)
