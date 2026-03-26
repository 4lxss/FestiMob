package com.example.festimob.ui

import android.app.Application
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.festimob.FestiMobApplication
import com.example.festimob.ui.festival.FestivalDetailsViewModel
import com.example.festimob.ui.festival.FestivalEntryViewModel
import com.example.festimob.ui.festival.FestivalListViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY

/**
 * Provides Factory to create instance of ViewModel for the entire Inventory app
 */
object AppViewModelProvider {

    val FestivalIdKey = object : CreationExtras.Key<Int> {}
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
        initializer {
            // On récupère l'ID depuis une clé personnalisée ou on le passe manuellement
            val festivalId = this[FestivalIdKey] ?: 0
            FestivalDetailsViewModel(
                festivalId = festivalId,
                festivalRepository = festimobApplication().container.festivalRepository
            )
        }

    }
}

/**
 * Extension function to queries for [Application] object and returns an instance of
 * [InventoryApplication].
 */
// Dans AppViewModelProvider.kt
fun CreationExtras.festimobApplication(): FestiMobApplication {
    // On récupère ce que le système nous donne pour APPLICATION_KEY
    val appObject = this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY]

    // Tentative 1 : Cast direct
    if (appObject is FestiMobApplication) return appObject

    // Tentative 2 : Si c'est une Application standard, on check son context
    if (appObject is android.app.Application) {
        return appObject as? FestiMobApplication
            ?: throw IllegalStateException("L'application n'est pas de type FestiMobApplication")
    }

    // Si on arrive ici, c'est que APPLICATION_KEY est vraiment null
    throw IllegalStateException("APPLICATION_KEY est nulle dans les CreationExtras. " +
            "Vérifiez que vous passez bien 'factory = AppViewModelProvider.Factory' dans votre appel viewModel().")
}