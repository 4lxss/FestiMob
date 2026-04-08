package com.example.festimob.ui.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.BookOnline
import androidx.compose.material.icons.filled.PlaylistAddCircle
import androidx.compose.material.icons.filled.Logout
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
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModelProvider
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.MutableCreationExtras
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.ui.NavDisplay
import com.example.festimob.FestiMobApplication
import com.example.festimob.R
import com.example.festimob.data.api.Role
import com.example.festimob.data.api.hasMinimumRole
import com.example.festimob.ui.AppViewModelProvider
import com.example.festimob.ui.festival.FestivalDetailsScreen
import com.example.festimob.ui.festival.FestivalDetailsViewModel
import com.example.festimob.ui.festival.FestivalEntryScreen
import com.example.festimob.ui.festival.FestivalEntryViewModel
import com.example.festimob.ui.festival.FestivalListScreen
import com.example.festimob.ui.admin.AdminScreen
import com.example.festimob.ui.auth.LoginScreen
import com.example.festimob.ui.auth.LoginViewModel
import com.example.festimob.ui.auth.RegisterScreen
import com.example.festimob.ui.auth.RegisterViewModel
import com.example.festimob.ui.reservation.ReservationDetailScreen
import com.example.festimob.ui.reservation.ReservationFormScreen
import com.example.festimob.ui.reservation.ReservationListScreen
import com.example.festimob.ui.reservation.ReservationViewModel
import com.example.festimob.ui.zoneplan.ZonePlanListScreen
import com.example.festimob.ui.zoneplan.ZonePlanListViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SmallNavigation() {
    val backStack = remember { mutableStateListOf<Destination>(Destination.Accueil) }
    var isLoggedIn by rememberSaveable { mutableStateOf(false) }
    var currentUserRole by rememberSaveable { mutableStateOf<String?>(null) }
    var currentUserId by rememberSaveable { mutableStateOf(0) }
    var logoutError by rememberSaveable { mutableStateOf<String?>(null) }
    val scope = rememberCoroutineScope()
    val canAccessAdmin = isLoggedIn && hasMinimumRole(currentUserRole, Role.ADMIN)
    val bottomNavItems = buildList {
        add(Destination.Accueil)
        add(Destination.FestivalList)
        if (canAccessAdmin) add(Destination.Admin)
    }
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
                actions = {
                    if (!isLoggedIn) {
                        TextButton(onClick = { backStack.add(Destination.Login) }) {
                            Text("Connexion")
                        }
                    } else {
                        IconButton(
                            onClick = {
                                scope.launch {
                                    try {
                                        com.example.festimob.data.api.RetrofitInstance.api.logout()
                                        isLoggedIn = false
                                        currentUserRole = null
                                        logoutError = null
                                        backStack.clear()
                                        backStack.add(Destination.Accueil)
                                    } catch (e: Exception) {
                                        logoutError = e.message ?: "Erreur lors de la déconnexion"
                                    }
                                }
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Logout,
                                contentDescription = "Déconnexion"
                            )
                        }
                    }
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
                        val context = LocalContext.current.applicationContext as FestiMobApplication
                        val extras = MutableCreationExtras().apply {
                            set(ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY, context)
                        }

                        FestivalListScreen(
                            navigateToFestivalEntry = { backStack.add(Destination.FestivalEntry()) },
                            navigateToFestivalDetails = { id -> backStack.add(Destination.FestivalDetails(id)) },
                            navigateBack = { backStack.removeLastOrNull() },
                        )
                    }
                    is Destination.Admin -> NavEntry(key) {
                        val context = LocalContext.current.applicationContext as FestiMobApplication
                        val extras = MutableCreationExtras().apply {
                            set(ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY, context)
                        }

                        AdminScreen(
                            viewModel = if (canAccessAdmin) {
                                viewModel(
                                    factory = AppViewModelProvider.Factory,
                                    extras = extras
                                )
                            } else {
                                null
                            }
                        )
                    }
                    is Destination.Login -> NavEntry(key) {
                        val context = LocalContext.current.applicationContext as FestiMobApplication
                        val extras = MutableCreationExtras().apply {
                            set(ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY, context)
                        }

                        val loginViewModel: LoginViewModel = viewModel(
                            factory = AppViewModelProvider.Factory,
                            extras = extras
                        )

                        LoginScreen(
                            viewModel = loginViewModel,
                            onLoginSuccess = { role, userId ->
                                isLoggedIn = true
                                currentUserRole = role
                                currentUserId = userId
                                logoutError = null
                                backStack.clear()
                                backStack.add(Destination.Accueil)
                            },
                            onRegisterClick = { backStack.add(Destination.Register) }
                        )
                    }
                    is Destination.Register -> NavEntry(key) {
                        val context = LocalContext.current.applicationContext as FestiMobApplication
                        val extras = MutableCreationExtras().apply {
                            set(ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY, context)
                        }

                        val registerViewModel: RegisterViewModel = viewModel(
                            factory = AppViewModelProvider.Factory,
                            extras = extras
                        )

                        RegisterScreen(
                            viewModel = registerViewModel,
                            onRegisterSuccess = { backStack.removeLastOrNull() }
                        )
                    }
                    is Destination.FestivalEntry -> NavEntry(key) {
                        val context = androidx.compose.ui.platform.LocalContext.current.applicationContext as com.example.festimob.FestiMobApplication
                        val extras = androidx.lifecycle.viewmodel.MutableCreationExtras().apply {
                            set(androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY, context)
                        }

                        FestivalEntryScreen(
                            navigateBack = { backStack.removeLastOrNull() },
                            viewModel = viewModel(
                                factory = com.example.festimob.ui.AppViewModelProvider.Factory,
                                extras = extras
                            )
                        )

                    }
                    is Destination.FestivalDetails -> NavEntry(key) {
                        val context = LocalContext.current.applicationContext as FestiMobApplication
                        val extras = MutableCreationExtras().apply {
                            set(ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY, context)
                            set(AppViewModelProvider.FestivalIdKey, key.id)
                        }
                        key(key.id) {
                            val vm: FestivalDetailsViewModel = viewModel(
                                key = "festival_details_${key.id}",
                                factory = AppViewModelProvider.Factory,
                                extras = extras
                            )
                            FestivalDetailsScreen(
                                navigateBack = { backStack.removeLastOrNull() },
                                navigateToEditItem = { id ->
                                    backStack.add(Destination.FestivalEntry(id = id))
                                },
                                navigateToReservations = { id ->
                                    backStack.add(Destination.ReservationList(id))
                                },
                                viewModel = vm
                            )
                        }
                    }

                    is Destination.ZonePlans -> NavEntry(key) {
                        val context = LocalContext.current.applicationContext as FestiMobApplication
                        val extras = MutableCreationExtras().apply {
                            set(ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY, context)
                        }
                        val vm: ZonePlanListViewModel = viewModel(
                            factory = AppViewModelProvider.Factory,
                            key = "zoneplans_all",
                            extras = extras
                        )
                        ZonePlanListScreen(viewModel = vm)
                    }

                    is Destination.ReservationList -> NavEntry(key) {
                        val context = LocalContext.current.applicationContext as FestiMobApplication
                        val extras = MutableCreationExtras().apply {
                            set(ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY, context)
                            set(AppViewModelProvider.FestivalIdKey, key.festivalId)
                            set(AppViewModelProvider.UserIdKey, currentUserId)
                        }

                        val vm: ReservationViewModel = viewModel(
                            key = "reservations_${key.festivalId}",
                            factory = AppViewModelProvider.Factory,
                            extras = extras
                        )
                        ReservationListScreen(
                            viewModel = vm,
                            navigateToDetail = { id ->
                                backStack.add(Destination.ReservationDetail(key.festivalId, id))
                            },
                            navigateToAdd = {
                                backStack.add(Destination.ReservationForm(key.festivalId))
                            },
                            navigateBack = { backStack.removeLastOrNull() }
                        )
                    }

                    is Destination.ReservationDetail -> NavEntry(key) {
                        val context = LocalContext.current.applicationContext as FestiMobApplication
                        val extras = MutableCreationExtras().apply {
                            set(ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY, context)
                            set(AppViewModelProvider.UserIdKey, currentUserId)
                        }

                        val vm: ReservationViewModel = viewModel(
                            key = "reservations_${key.festivalId}",
                            factory = AppViewModelProvider.Factory,
                            extras = extras
                        )
                        ReservationDetailScreen(
                            reservationId = key.reservationId,
                            viewModel = vm,
                            navigateToEdit = { id ->
                                backStack.add(Destination.ReservationForm(key.festivalId, id))
                            },
                            navigateBack = { backStack.removeLastOrNull() }
                        )
                    }

                    is Destination.ReservationForm -> NavEntry(key) {
                        val context = LocalContext.current.applicationContext as FestiMobApplication
                        val extras = MutableCreationExtras().apply {
                            set(ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY, context)
                            set(AppViewModelProvider.FestivalIdKey, key.festivalId)
                            set(AppViewModelProvider.UserIdKey, currentUserId)
                        }

                        val vm: ReservationViewModel = viewModel(
                            key = "reservations_${key.festivalId}",
                            factory = AppViewModelProvider.Factory,
                            extras = extras
                        )
                        ReservationFormScreen(
                            viewModel = vm,
                            zones = vm.uiState.festivalZones,
                            reservationId = key.reservationId,
                            navigateBack = { backStack.removeLastOrNull() }
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
    object Admin : Destination("Admin", Icons.Default.AdminPanelSettings, "admin", R.string.item_entry_title)
    object Login : Destination("Connexion", Icons.Default.AdminPanelSettings, "login", R.string.item_entry_title)
    object Register : Destination("Inscription", Icons.Default.AdminPanelSettings, "register", R.string.item_entry_title)

    object ZonePlans : Destination("Zones", Icons.Default.AdminPanelSettings, "zone_plans", R.string.details_title)
    data class FestivalDetails(val id: Int) : Destination("Détails", Icons.Default.AdminPanelSettings, "details", R.string.details_title)
    data class ReservationList(val festivalId: Int) : Destination("Réservations", Icons.Default.BookOnline, "reservation_list", R.string.details_title)
    data class ReservationDetail(val festivalId: Int, val reservationId: Int) : Destination("Détail réservation", Icons.Default.BookOnline, "reservation_detail", R.string.details_title)
    data class ReservationForm(val festivalId: Int, val reservationId: Int? = null) : Destination("Formulaire réservation", Icons.Default.BookOnline, "reservation_form", R.string.details_title)

}