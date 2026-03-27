package com.example.festimob.ui.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Album
import androidx.compose.material.icons.filled.Home
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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.ui.NavDisplay
import com.example.festimob.FestiMobApplication
import com.example.festimob.ui.editor.EditorScreen
import com.example.festimob.ui.editor.EditorViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SmallNavigation() {
    val backStack = rememberSaveable { mutableStateListOf<Destination>(Destination.ACCUEIL) }
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
                    Destination.entries.forEach { destination ->
                        NavigationBarItem(
                            selected = backStack.lastOrNull() == destination,
                            onClick = {
                                if (backStack.lastOrNull() != destination) {
                                    if (destination == Destination.ACCUEIL) {
                                        backStack.clear()
                                        backStack.add(Destination.ACCUEIL)
                                    } else {
                                        if (backStack.isEmpty() || backStack[0] != Destination.ACCUEIL) {
                                            backStack.clear()
                                            backStack.add(Destination.ACCUEIL)
                                        }
                                        backStack.remove(destination)
                                        backStack.add(destination)
                                    }
                                }
                            },
                            icon = { Icon(destination.icon, contentDescription = destination.contentDescription) },
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
                    Destination.ACCUEIL -> NavEntry(key) {
                        Box(contentAlignment = Alignment.Center) {
                            Text("PLAYLISTS")
                        }
                    }
                    Destination.PAGES -> NavEntry(key) {
                        EditorScreen(
                            modifier = Modifier.padding(innerPadding),
                            viewModel = viewModel(factory = EditorViewModel.Factory),
                            //navigateToAddForm = {}
                        )
                    }
                    Destination.ALBUM -> NavEntry(key) {
                        Box(contentAlignment = Alignment.Center) {
                            Text("ALBUM")
                        }
                    }
                }
            }
        )
    }
}

enum class Destination(
    val route: String,
    val label: String,
    val icon: ImageVector,
    val contentDescription: String
) {
    ALBUM("album", "Album", Icons.Default.Album, "Album"),
    ACCUEIL("accueil", "Accueil", Icons.Default.Home, "Accueil"),
    PAGES("pages", "Pages", Icons.Default.PlaylistAddCircle, "Pages")
}