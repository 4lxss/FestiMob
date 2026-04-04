package com.example.festimob.ui.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Album
import androidx.compose.material.icons.filled.Details
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.MusicNote
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
import com.example.festimob.ui.editor.EditorAddForm
import com.example.festimob.ui.editor.EditorDetails
import com.example.festimob.ui.editor.EditorDetailsScreen
import com.example.festimob.ui.editor.EditorDetailsViewModel
import com.example.festimob.ui.editor.EditorFormScreen
import com.example.festimob.ui.editor.EditorFormUiState
import com.example.festimob.ui.editor.EditorScreen
import com.example.festimob.ui.editor.EditorViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SmallNavigation() {
    val backStack = rememberSaveable { mutableStateListOf<Destination>(Destination.Accueil) }
    val bottomNavItems = listOf(Destination.Accueil, Destination.EditorList, Destination.Album)
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
                    is Destination.EditorList -> NavEntry(key) {
                        EditorScreen(
                            modifier = Modifier.padding(innerPadding),
                            viewModel = viewModel(factory = EditorViewModel.Factory),
                            navigateToAddForm = { backStack.add(Destination.EditorEntry())},
                            navigateToUpdateForm = {},
                            navigateToDetails = {backStack.add(Destination.EditorDetails(1))}
                        )
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
                    is Destination.Album -> NavEntry(key) {
                        Box(contentAlignment = Alignment.Center) {
                            Text("ALBUM")
                        }
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
    object EditorList : Destination("Editors", Icons.Default.List, "editors", R.string.editor_title)
    data class EditorEntry(val id: Int = 0) : Destination("Add Editor", Icons.Default.Add, "entry", R.string.editor_entry_title)
    object Album : Destination("Album", Icons.Default.Album, "album", R.string.editor_entry_title)

    data class EditorDetails(val id: Int) : Destination("Editor Details", Icons.Default.Details, "details", R.string.details_title)
}