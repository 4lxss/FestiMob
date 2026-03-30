package com.example.festimob.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AdminPanelSettings
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
import androidx.lifecycle.viewmodel.MutableCreationExtras
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.ui.NavDisplay
import com.example.festimob.R
import com.example.festimob.ui.AppViewModelProvider
import com.example.festimob.ui.festival.FestivalDetailsScreen
import com.example.festimob.ui.festival.FestivalDetailsViewModel
import com.example.festimob.ui.festival.FestivalEntryScreen
import com.example.festimob.ui.festival.FestivalListScreen
import com.example.festimob.ui.navigation.NavigationDestination

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SmallNavigation() {
    val backStack = rememberSaveable { mutableStateListOf<Destination>(Destination.Accueil) }
    val bottomNavItems = listOf(Destination.Accueil, Destination.FestivalList, Destination.Admin)
    Scaffold (
        topBar = {
            CenterAlignedTopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                title = {
                    Text("FestiJeux")
                },
                navigationIcon = {
                    if (backStack.size > 1) {
                        IconButton(onClick = { backStack.removeLastOrNull() }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back"
                            )
                        }
                    }
                }
            )
        },
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
                        val context = androidx.compose.ui.platform.LocalContext.current.applicationContext as com.example.festimob.FestiMobApplication
                        val extras = androidx.lifecycle.viewmodel.MutableCreationExtras().apply {
                            set(androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY, context)
                        }

                        FestivalListScreen(
                            navigateToFestivalEntry = { backStack.add(Destination.FestivalEntry) },
                            navigateToFestivalDetails = { id -> backStack.add(Destination.FestivalDetails(id)) },
                            // Si ton FestivalListScreen prend un viewModel en paramètre :
                            // viewModel = viewModel(factory = AppViewModelProvider.Factory, extras = extras)
                        )
                    }
                    is Destination.Admin -> NavEntry(key) {
                        val context = androidx.compose.ui.platform.LocalContext.current.applicationContext as com.example.festimob.FestiMobApplication
                        val extras = androidx.lifecycle.viewmodel.MutableCreationExtras().apply {
                            set(androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY, context)
                        }

                        com.example.festimob.ui.user.UserListScreen(
                            viewModel = androidx.lifecycle.viewmodel.compose.viewModel(
                                factory = com.example.festimob.ui.AppViewModelProvider.Factory,
                                extras = extras
                            )
                        )
                    }
                    is Destination.FestivalEntry -> NavEntry(key) {
                        val context = androidx.compose.ui.platform.LocalContext.current.applicationContext as com.example.festimob.FestiMobApplication

                        // 2. On crée des extras manuellement et on y injecte l'APPLICATION_KEY
                        val extras = androidx.lifecycle.viewmodel.MutableCreationExtras().apply {
                            set(androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY, context)
                        }

                        FestivalEntryScreen(
                            navigateBack = { backStack.removeLastOrNull() },
                            // 3. On passe la factory ET les extras qu'on vient de fabriquer
                            viewModel = viewModel(
                                factory = com.example.festimob.ui.AppViewModelProvider.Factory,
                                extras = extras
                            )
                        )

                    }
                    is Destination.FestivalDetails -> NavEntry(key) {
                        val context = androidx.compose.ui.platform.LocalContext.current.applicationContext as com.example.festimob.FestiMobApplication

                        // 2. Créer les extras en mettant les DEUX clés nécessaires
                        val extras = androidx.lifecycle.viewmodel.MutableCreationExtras().apply {
                            // Clé pour l'application (nécessaire pour le container/DB)
                            set(androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY, context)
                            // Clé pour l'ID du festival (nécessaire pour charger les données)
                            set(com.example.festimob.ui.AppViewModelProvider.FestivalIdKey, key.id)
                        }

                        val viewModel: FestivalDetailsViewModel = viewModel(
                            factory = com.example.festimob.ui.AppViewModelProvider.Factory,
                            extras = extras
                        )

                        FestivalDetailsScreen(
                            navigateBack = { backStack.removeLastOrNull() },
                            navigateToEditItem = { id -> /* Ta logique d'édition */ },
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
    object FestivalEntry : Destination("Ajout", Icons.Default.Add, "entry", R.string.item_entry_title)
    object Admin : Destination("Admin", Icons.Default.AdminPanelSettings, "admin", R.string.item_entry_title)

    data class FestivalDetails(val id: Int) : Destination("Détails", Icons.Default.AdminPanelSettings, "details", R.string.details_title)
}
