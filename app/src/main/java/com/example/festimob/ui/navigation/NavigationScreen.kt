package com.example.festimob.ui.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Album
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.PlaylistAddCircle
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.MutableCreationExtras
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.ui.NavDisplay
import com.example.festimob.FestiMobApplication
import com.example.festimob.R
import com.example.festimob.ui.AppViewModelProvider
import com.example.festimob.ui.festival.FestivalDetailsScreen
import com.example.festimob.ui.festival.FestivalDetailsViewModel
import com.example.festimob.ui.festival.FestivalEntryScreen
import com.example.festimob.ui.festival.FestivalListScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SmallNavigation() {
    val backStack = rememberSaveable { mutableStateListOf<Destination>(Destination.Accueil) }
    val bottomNavItems = listOf(Destination.Accueil, Destination.FestivalList, Destination.Album)
    Scaffold (
        bottomBar = {
            BottomAppBar(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.primary,
            ) {
                NavigationBar (
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    contentColor = MaterialTheme.colorScheme.primary,
                ) {
                    bottomNavItems.forEach { destination ->
                        NavigationBarItem(
                            selected = backStack.lastOrNull() == destination,
                            onClick = {
                                if (backStack.lastOrNull() != destination) {
                                    if (destination == Destination.Accueil) {
                                        backStack.clear()
                                        backStack.add(Destination.Accueil)
                                    } else {
                                        if (backStack.isEmpty() || backStack[0] != Destination.Accueil) {
                                            backStack.clear()
                                            backStack.add(Destination.Accueil)
                                        }
                                        backStack.remove(destination)
                                        backStack.add(destination)
                                    }
                                }
                            },
                            icon = { Icon(destination.icon, contentDescription = destination.label) },
                            label = { Text(destination.label) }
                        )
                    }
                }
            }
        }
    )
    {
            innerPadding ->
        NavDisplay (
            backStack = backStack,
            onBack = { backStack.removeLastOrNull() },
            modifier = Modifier.padding(innerPadding),
            entryProvider = { key ->
                when(key) {
                    is Destination.Accueil -> NavEntry(key) {
                        Box(contentAlignment = Alignment.Center) {
                            Text("PLAYLISTS")
                        }
                    }
                    is Destination.FestivalList -> NavEntry(key) {
                        val context = LocalContext.current.applicationContext as FestiMobApplication
                        val extras = MutableCreationExtras().apply {
                            set(ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY, context)
                        }

                        FestivalListScreen(
                            navigateToFestivalEntry = { backStack.add(Destination.FestivalEntry()) },
                            navigateToFestivalDetails = { id -> backStack.add(Destination.FestivalDetails(id)) },
                        )
                    }
                    is Destination.Album -> NavEntry(key) {
                        Box(contentAlignment = Alignment.Center) {
                            Text("ALBUM")
                        }
                    }
                    is Destination.FestivalEntry -> NavEntry(key) {
                        val context = LocalContext.current.applicationContext as FestiMobApplication

                        val extras = MutableCreationExtras().apply {
                            set(ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY, context)
                            set(AppViewModelProvider.FestivalIdKey, key.id)
                        }

                        FestivalEntryScreen(
                            navigateBack = { backStack.removeLastOrNull() },
                            isEditMode = key.id != 0,
                            viewModel = viewModel(
                                factory = AppViewModelProvider.Factory,
                                extras = extras
                            )
                        )

                    }
                    is Destination.FestivalDetails -> NavEntry(key) {
                        val context = LocalContext.current.applicationContext as FestiMobApplication

                        // 2. Créer les extras en mettant les DEUX clés nécessaires
                        val extras = MutableCreationExtras().apply {
                            // Clé pour l'application (nécessaire pour le container/DB)
                            set(ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY, context)
                            // Clé pour l'ID du festival (nécessaire pour charger les données)
                            set(AppViewModelProvider.FestivalIdKey, key.id)
                        }

                        val viewModel: FestivalDetailsViewModel = viewModel(
                            factory = AppViewModelProvider.Factory,
                            extras = extras
                        )

                        FestivalDetailsScreen(
                            navigateBack = { backStack.removeLastOrNull() },
                            navigateToEditItem = { id ->
                                backStack.add(Destination.FestivalEntry(id = id))
                            },
                            viewModel = viewModel
                        )
                    }
                }
            }
        )
    }
}

sealed class Destination(
    val label: String,
    val icon: ImageVector,
    override val route: String,
    override val titleRes: Int
) : NavigationDestination {
    object Accueil : Destination("Accueil", Icons.Default.Home, "accueil", R.string.app_name)
    object FestivalList : Destination("Festivals", Icons.Default.PlaylistAddCircle, "festivals", R.string.festivals_title)
    data class FestivalEntry(val id: Int = 0) : Destination("Ajout", Icons.Default.Add, "entry", R.string.item_entry_title)
    object Album : Destination("Album", Icons.Default.Album, "album", R.string.item_entry_title)

    data class FestivalDetails(val id: Int) : Destination("Détails", Icons.Default.Album, "details", R.string.details_title)
}