package com.example.festimob.ui.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.BookOnline
import androidx.compose.material.icons.filled.SportsEsports
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.PlaylistAddCircle
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.MutableCreationExtras
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.ui.NavDisplay
import com.example.festimob.FestiMobApplication
import com.example.festimob.R
import com.example.festimob.data.api.RetrofitInstance
import com.example.festimob.data.api.Role
import com.example.festimob.data.api.hasMinimumRole
import com.example.festimob.games.GamesScreen
import com.example.festimob.ui.AppViewModelProvider
import com.example.festimob.ui.admin.AdminScreen
import com.example.festimob.ui.auth.LoginScreen
import com.example.festimob.ui.auth.LoginViewModel
import com.example.festimob.ui.auth.RegisterScreen
import com.example.festimob.ui.auth.RegisterViewModel
import com.example.festimob.ui.editor.EditorDetailsScreen
import com.example.festimob.ui.editor.EditorDetailsViewModel
import com.example.festimob.ui.editor.EditorFormScreen
import com.example.festimob.ui.editor.EditorScreen
import com.example.festimob.ui.editor.EditorViewModel
import com.example.festimob.ui.festival.FestivalDetailsScreen
import com.example.festimob.ui.festival.FestivalDetailsViewModel
import com.example.festimob.ui.festival.FestivalEntryScreen
import com.example.festimob.ui.festival.FestivalListScreen
import com.example.festimob.ui.reservation.ReservationDetailScreen
import com.example.festimob.ui.reservation.ReservationFormScreen
import com.example.festimob.ui.reservation.ReservationListScreen
import com.example.festimob.ui.reservation.ReservationViewModel
import com.example.festimob.ui.zoneplan.ZonePlanListScreen
import com.example.festimob.ui.zoneplan.ZonePlanListViewModel
import kotlinx.coroutines.launch

/**
 * Main navigation component of the app.
 * It manages the backstack, user roles, side drawer, and all screens.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SmallNavigation() {
    // Navigation state and user session info
    val backStack = remember { mutableStateListOf<Destination>(Destination.Accueil) }
    var isLoggedIn by rememberSaveable { mutableStateOf(false) }
    var currentUserRole by rememberSaveable { mutableStateOf<String?>(null) }
    var currentUserId by rememberSaveable { mutableStateOf(0) }
    var logoutError by rememberSaveable { mutableStateOf<String?>(null) }
    val scope = rememberCoroutineScope()

    // Role-based access control logic
    val canAccessAdmin = isLoggedIn && hasMinimumRole(currentUserRole, Role.ADMIN)
    val canAccessFestivals = isLoggedIn && hasMinimumRole(currentUserRole, Role.SUPER_ORGANIZER)
    val canAccessGames = isLoggedIn && hasMinimumRole(currentUserRole, Role.VOLUNTEER)
    val canAccessEditors = isLoggedIn && hasMinimumRole(currentUserRole, Role.VOLUNTEER)
    val canAccessDrawer = isLoggedIn && hasMinimumRole(currentUserRole, Role.VOLUNTEER)

    val bottomNavItems = listOf(Destination.Accueil)
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val currentDestination = backStack.lastOrNull() ?: Destination.Accueil
    val topBarTitle = if (currentDestination == Destination.Accueil) "FestiJeux" else currentDestination.label

    // Close the menu automatically if the user loses access permissions
    LaunchedEffect(canAccessDrawer) {
        if (!canAccessDrawer && drawerState.isOpen) {
            drawerState.close()
        }
    }

    // Side navigation menu containing links to different modules
    ModalNavigationDrawer(
        drawerState = drawerState,
        gesturesEnabled = canAccessDrawer,
        drawerContent = {
            ModalDrawerSheet(
                modifier = Modifier.fillMaxWidth(2f / 3f),
                drawerContainerColor = MaterialTheme.colorScheme.primaryContainer
            ) {
                // Navigation items in the drawer, filtered by user role
                if (canAccessEditors) {
                    NavigationDrawerItem(
                        label = { Text("Editors") },
                        selected = backStack.lastOrNull() == Destination.EditorList,
                        onClick = {
                            if (backStack.lastOrNull() != Destination.EditorList) {
                                backStack.remove(Destination.EditorList)
                                backStack.add(Destination.EditorList)
                            }
                            scope.launch { drawerState.close() }
                        },
                        icon = {
                            Icon(
                                imageVector = Icons.Default.List,
                                contentDescription = "Editors"
                            )
                        },
                        colors = NavigationDrawerItemDefaults.colors()
                    )
                }
                if (canAccessFestivals) {
                    NavigationDrawerItem(
                        label = { Text("Festivals") },
                        selected = backStack.lastOrNull() == Destination.FestivalList,
                        onClick = {
                            if (backStack.lastOrNull() != Destination.FestivalList) {
                                backStack.remove(Destination.FestivalList)
                                backStack.add(Destination.FestivalList)
                            }
                            scope.launch { drawerState.close() }
                        },
                        icon = {
                            Icon(
                                imageVector = Icons.Default.PlaylistAddCircle,
                                contentDescription = "Festivals"
                            )
                        },
                        colors = NavigationDrawerItemDefaults.colors()
                    )
                }
                if (canAccessGames) {
                    NavigationDrawerItem(
                        label = { Text("Jeux") },
                        selected = backStack.lastOrNull() == Destination.Games,
                        onClick = {
                            if (backStack.lastOrNull() != Destination.Games) {
                                backStack.remove(Destination.Games)
                                backStack.add(Destination.Games)
                            }
                            scope.launch { drawerState.close() }
                        },
                        icon = {
                            Icon(
                                imageVector = Icons.Default.SportsEsports,
                                contentDescription = "Jeux"
                            )
                        },
                        colors = NavigationDrawerItemDefaults.colors()
                    )
                }
                if (canAccessAdmin) {
                    NavigationDrawerItem(
                        label = { Text("Admin") },
                        selected = backStack.lastOrNull() == Destination.Admin,
                        onClick = {
                            if (backStack.lastOrNull() != Destination.Admin) {
                                backStack.remove(Destination.Admin)
                                backStack.add(Destination.Admin)
                            }
                            scope.launch { drawerState.close() }
                        },
                        icon = {
                            Icon(
                                imageVector = Icons.Default.AdminPanelSettings,
                                contentDescription = "Admin"
                            )
                        },
                        colors = NavigationDrawerItemDefaults.colors()
                    )
                }
                NavigationDrawerItem(
                    label = { Text("Zones") },
                    selected = backStack.lastOrNull() == Destination.ZonePlans,
                    onClick = {
                        if (backStack.lastOrNull() != Destination.ZonePlans) {
                            backStack.remove(Destination.ZonePlans)
                            backStack.add(Destination.ZonePlans)
                        }
                        scope.launch { drawerState.close() }
                    },
                    icon = {
                        Icon(
                            imageVector = Icons.Default.AdminPanelSettings,
                            contentDescription = "Zones"
                        )
                    },
                    colors = NavigationDrawerItemDefaults.colors()
                )
            }
        }
    ) {
        Scaffold(
            topBar = {
                // Top bar with title, menu toggle, and Login/Logout button
                CenterAlignedTopAppBar(
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        titleContentColor = Color.White,
                        actionIconContentColor = Color.White,
                        navigationIconContentColor = Color.White
                    ),
                    title = { Text(topBarTitle) },
                    actions = {
                        if (!isLoggedIn) {
                            TextButton(onClick = { backStack.add(Destination.Login) }) {
                                Text("Connexion", color = Color.White)
                            }
                        } else {
                            IconButton(
                                onClick = {
                                    scope.launch {
                                        try {
                                            RetrofitInstance.api.logout()
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
                        if (canAccessDrawer) {
                            IconButton(onClick = { scope.launch { drawerState.open() } }) {
                                Icon(
                                    imageVector = Icons.Default.Menu,
                                    contentDescription = "Menu"
                                )
                            }
                        }
                    }
                )
            },
            bottomBar = {
                // Simple bottom navigation bar for quick access to Home
                BottomAppBar(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    contentColor = MaterialTheme.colorScheme.primary,
                ) {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        NavigationBar(
                            containerColor = MaterialTheme.colorScheme.primaryContainer,
                            contentColor = MaterialTheme.colorScheme.primary,
                        ) {
                            bottomNavItems.forEach { destination ->
                                NavigationBarItem(
                                    selected = backStack.lastOrNull() == destination,
                                    onClick = {
                                        if (backStack.lastOrNull() != destination) {
                                            backStack.clear()
                                            backStack.add(Destination.Accueil)
                                        }
                                    },
                                    icon = { Icon(destination.icon, contentDescription = destination.label) },
                                    label = { Text(destination.label) }
                                )
                            }
                        }
                    }
                }
            }
        ) { innerPadding ->
            // Container that displays the screen content based on the current backstack
            NavDisplay(
                backStack = backStack,
                onBack = { backStack.removeLastOrNull() },
                modifier = Modifier.padding(innerPadding),
                entryProvider = { key ->
                    when (key) {
                        is Destination.Accueil -> NavEntry(key) {
                            Box(contentAlignment = Alignment.Center) {
                                Text("PLAYLISTS")
                            }
                        }

                        is Destination.EditorList -> NavEntry(key) {
                            if (canAccessEditors) {
                                EditorScreen(
                                    modifier = Modifier.padding(innerPadding),
                                    viewModel = viewModel(factory = EditorViewModel.Factory),
                                    navigateToAddForm = { backStack.add(Destination.EditorEntry()) },
                                    navigateToUpdateForm = {},
                                    navigateToDetails = { id ->
                                        backStack.add(
                                            Destination.EditorDetails(
                                                id
                                            )
                                        )
                                    }
                                )
                            } else {
                                Box(contentAlignment = Alignment.Center) {
                                    Text("Accès refusé")
                                }
                            }
                        }

                        is Destination.EditorEntry -> NavEntry(key) {
                            EditorFormScreen(
                                navigateBack = { backStack.removeLastOrNull() }
                            )
                        }

                        is Destination.EditorDetails -> NavEntry(key) {
                            val editorId = key.id
                            EditorDetailsScreen(
                                editorId = editorId,
                                viewModel = viewModel(factory = EditorDetailsViewModel.Factory)
                            )
                        }

                        is Destination.FestivalList -> NavEntry(key) {
                            if (canAccessFestivals) {
                                FestivalListScreen(
                                    navigateToFestivalEntry = { backStack.add(Destination.FestivalEntry()) },
                                    navigateToFestivalDetails = { id ->
                                        backStack.add(
                                            Destination.FestivalDetails(
                                                id
                                            )
                                        )
                                    },
                                )
                            } else {
                                Box(contentAlignment = Alignment.Center) {
                                    Text("Accès refusé")
                                }
                            }
                        }

                        is Destination.Admin -> NavEntry(key) {
                            val context =
                                LocalContext.current.applicationContext as FestiMobApplication
                            val extras = MutableCreationExtras().apply {
                                set(
                                    ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY,
                                    context
                                )
                            }

                            if (canAccessAdmin) {
                                AdminScreen(
                                    viewModel = viewModel(
                                        factory = AppViewModelProvider.Factory,
                                        extras = extras
                                    )
                                )
                            } else {
                                Box(contentAlignment = Alignment.Center) {
                                    Text("Accès refusé")
                                }
                            }
                        }

                        is Destination.Login -> NavEntry(key) {
                            val context =
                                LocalContext.current.applicationContext as FestiMobApplication
                            val extras = MutableCreationExtras().apply {
                                set(
                                    ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY,
                                    context
                                )
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
                            val context =
                                LocalContext.current.applicationContext as FestiMobApplication
                            val extras = MutableCreationExtras().apply {
                                set(
                                    ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY,
                                    context
                                )
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
                            val context =
                                LocalContext.current.applicationContext as FestiMobApplication
                            val extras = MutableCreationExtras().apply {
                                set(
                                    ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY,
                                    context
                                )
                            }

                            FestivalEntryScreen(
                                navigateBack = { backStack.removeLastOrNull() },
                                viewModel = viewModel(
                                    factory = AppViewModelProvider.Factory,
                                    extras = extras
                                )
                            )
                        }

                        is Destination.Games -> NavEntry(key) {
                            if (canAccessGames) {
                                GamesScreen()
                            } else {
                                Box(contentAlignment = Alignment.Center) {
                                    Text("Accès refusé")
                                }
                            }
                        }

                        is Destination.FestivalDetails -> NavEntry(key) {
                            val context =
                                LocalContext.current.applicationContext as FestiMobApplication
                            val extras = MutableCreationExtras().apply {
                                set(
                                    ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY,
                                    context
                                )
                                set(AppViewModelProvider.FestivalIdKey, key.id)
                            }

                            key(key.id) {
                                val viewModel: FestivalDetailsViewModel = viewModel(
                                    factory = AppViewModelProvider.Factory,
                                    key = "festival_details_${key.id}",
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
                                    viewModel = viewModel
                                )
                            }
                        }

                        is Destination.ZonePlans -> NavEntry(key) {
                            val context =
                                LocalContext.current.applicationContext as FestiMobApplication
                            val extras = MutableCreationExtras().apply {
                                set(
                                    ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY,
                                    context
                                )
                            }

                            val viewModel: ZonePlanListViewModel = viewModel(
                                factory = AppViewModelProvider.Factory,
                                key = "zoneplans_all",
                                extras = extras
                            )

                            ZonePlanListScreen(viewModel = viewModel)
                        }

                        is Destination.ReservationList -> NavEntry(key) {
                            val context =
                                LocalContext.current.applicationContext as FestiMobApplication
                            val extras = MutableCreationExtras().apply {
                                set(
                                    ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY,
                                    context
                                )
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
                            val context =
                                LocalContext.current.applicationContext as FestiMobApplication
                            val extras = MutableCreationExtras().apply {
                                set(
                                    ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY,
                                    context
                                )
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
                            val context =
                                LocalContext.current.applicationContext as FestiMobApplication
                            val extras = MutableCreationExtras().apply {
                                set(
                                    ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY,
                                    context
                                )
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
}

/**
 * Sealed class defining all possible screens (destinations) in the app.
 * Each object/data class includes a label, icon, and unique route string.
 */
sealed class Destination(
    val label: String,
    val icon: ImageVector,
    override val route: String,
    override val titleRes: Int
) : NavigationDestination {
    object Accueil : Destination("Accueil", Icons.Default.Home, "accueil", R.string.app_name)
    object EditorList : Destination("Editors", Icons.Default.List, "editors", R.string.editor_title)
    data class EditorEntry(val id: Int = 0) : Destination("Ajout éditeur", Icons.Default.Add, "editor_entry", R.string.editor_entry_title)
    data class EditorDetails(val id: Int) : Destination("Détails éditeur", Icons.Default.AdminPanelSettings, "editor_details", R.string.details_title)
    object FestivalList : Destination("Festivals", Icons.Default.PlaylistAddCircle, "festivals", R.string.festivals_title)
    object Games : Destination("Jeux", Icons.Default.SportsEsports, "games", R.string.details_title)
    data class FestivalEntry(val id: Int = 0) : Destination("Ajout", Icons.Default.Add, "entry", R.string.item_entry_title)
    object Admin : Destination("Admin", Icons.Default.AdminPanelSettings, "admin", R.string.item_entry_title)
    object Login : Destination("Connexion", Icons.Default.AdminPanelSettings, "login", R.string.item_entry_title)
    object Register : Destination("Inscription", Icons.Default.AdminPanelSettings, "register", R.string.item_entry_title)
    data class FestivalDetails(val id: Int) : Destination("Détails", Icons.Default.AdminPanelSettings, "details", R.string.details_title)
    object ZonePlans : Destination("Zones", Icons.Default.AdminPanelSettings, "zone_plans", R.string.details_title)
    data class ReservationList(val festivalId: Int) : Destination("Réservations", Icons.Default.BookOnline, "reservation_list", R.string.details_title)
    data class ReservationDetail(val festivalId: Int, val reservationId: Int) : Destination("Détail réservation", Icons.Default.BookOnline, "reservation_detail", R.string.details_title)
    data class ReservationForm(val festivalId: Int, val reservationId: Int? = null) : Destination("Formulaire réservation", Icons.Default.BookOnline, "reservation_form", R.string.details_title)

}