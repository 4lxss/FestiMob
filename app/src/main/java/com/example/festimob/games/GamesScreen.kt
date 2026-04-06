@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.festimob.games

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Album
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage

@Composable
fun GamesScreen(
    viewModel: GamesViewModel = viewModel()
) {
    var selectedTabIndex by remember { mutableIntStateOf(1) }
    var selectedCategory by remember { mutableStateOf("All") }
    var selectedMechanism by remember { mutableStateOf("All") }

    val tabs = listOf("Contact", "Games", "Resa", "Other")

    val gamesScopeId = "default"

    var detailGame by remember { mutableStateOf<Game?>(null) }
    var showAddGameDialog by remember { mutableStateOf(false) }

    LaunchedEffect(gamesScopeId) {
        viewModel.loadMechanismFilterOptions()
        viewModel.loadGames(editionId = gamesScopeId)
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    viewModel.clearError()
                    showAddGameDialog = true
                }
            ) {
                Text(text = "+", fontSize = 24.sp)
            }
        },
        bottomBar = {
            BottomMenuBar()
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            TopBar()

            TabRow(
                selectedTabIndex = selectedTabIndex,
                modifier = Modifier.fillMaxWidth()
            ) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTabIndex == index,
                        onClick = { selectedTabIndex = index },
                        text = {
                            Text(
                                text = title,
                                fontWeight = if (selectedTabIndex == index) FontWeight.Bold else FontWeight.Normal
                            )
                        }
                    )
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    text = "Games",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(text = "Age group", fontSize = 14.sp)
                        RealDropdownFilterBox(
                            label = selectedCategory,
                            options = viewModel.ageCategoryFilterOptions,
                            onSelected = { newCategory ->
                                selectedCategory = newCategory
                                viewModel.loadGames(
                                    editionId = gamesScopeId,
                                    category = newCategory.takeIf { it != "All" },
                                    mechanism = selectedMechanism.takeIf { it != "All" }
                                )
                            }
                        )
                    }

                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .padding(start = 16.dp)
                    ) {
                        Text(text = "Mechanisms", fontSize = 14.sp)
                        RealDropdownFilterBox(
                            label = selectedMechanism,
                            options = viewModel.mechanismFilterOptions,
                            onSelected = { newMechanism ->
                                selectedMechanism = newMechanism
                                viewModel.loadGames(
                                    editionId = gamesScopeId,
                                    category = selectedCategory.takeIf { it != "All" },
                                    mechanism = newMechanism.takeIf { it != "All" }
                                )
                            }
                        )
                    }

                    FilledTonalIconButton(
                        onClick = {
                            viewModel.loadGames(
                                editionId = gamesScopeId,
                                category = selectedCategory.takeIf { it != "All" },
                                mechanism = selectedMechanism.takeIf { it != "All" }
                            )
                        },
                        modifier = Modifier
                            .padding(start = 16.dp)
                            .size(40.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = "Refresh games"
                        )
                    }
                }
            }

            Box(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                when {
                    viewModel.isLoading -> {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            CircularProgressIndicator()
                        }
                    }

                    viewModel.errorMessage != null -> {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Text(text = viewModel.errorMessage ?: "Error")
                        }
                    }

                    else -> {
                        LazyVerticalGrid(
                            columns = GridCells.Fixed(4),
                            contentPadding = PaddingValues(bottom = 16.dp),
                            horizontalArrangement = Arrangement.spacedBy(16.dp),
                            verticalArrangement = Arrangement.spacedBy(16.dp),
                            modifier = Modifier.fillMaxSize()
                        ) {
                            items(viewModel.games) { game ->
                                GameCard(
                                    game = game,
                                    onClick = { detailGame = game }
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    detailGame?.let { game ->
        GameDetailDialog(
            game = game,
            onDismiss = { detailGame = null }
        )
    }

    if (showAddGameDialog) {
        AddGameDialog(
            isSaving = viewModel.isSavingGame,
            errorMessage = viewModel.errorMessage,
            onDismiss = {
                showAddGameDialog = false
                viewModel.clearError()
            },
            onSubmit = { name, description, editeurIdRaw, idERaw ->
                val editeurId = editeurIdRaw.trim().toIntOrNull() ?: 0
                val idE = idERaw.trim().toIntOrNull()
                viewModel.createGame(
                    name = name,
                    description = description.ifBlank { null },
                    editeurId = editeurId,
                    idE = idE,
                    listingEditionId = gamesScopeId,
                    category = selectedCategory.takeIf { it != "All" },
                    mechanism = selectedMechanism.takeIf { it != "All" },
                    onSuccess = {
                        showAddGameDialog = false
                        viewModel.clearError()
                    }
                )
            }
        )
    }
}

@Composable
private fun AddGameDialog(
    isSaving: Boolean,
    errorMessage: String?,
    onDismiss: () -> Unit,
    onSubmit: (name: String, description: String, editeurIdRaw: String, idERaw: String) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var editeurIdRaw by remember { mutableStateOf("") }
    var idERaw by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = {
            if (!isSaving) onDismiss()
        },
        title = {
            Text(
                text = "Add game",
                style = MaterialTheme.typography.titleLarge
            )
        },
        text = {
            Column(
                modifier = Modifier.verticalScroll(rememberScrollState())
            ) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Name") },
                    singleLine = true,
                    enabled = !isSaving,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(12.dp))
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Description (optional)") },
                    minLines = 3,
                    enabled = !isSaving,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(12.dp))
                OutlinedTextField(
                    value = editeurIdRaw,
                    onValueChange = { editeurIdRaw = it },
                    label = { Text("Éditeur id (required)") },
                    placeholder = { Text("Publisher id for POST …/editeurs/{id}/jeux") },
                    singleLine = true,
                    enabled = !isSaving,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(12.dp))
                OutlinedTextField(
                    value = idERaw,
                    onValueChange = { idERaw = it },
                    label = { Text("id_e on jeu (optional)") },
                    placeholder = { Text("Only if API stores id_e — e.g. 138") },
                    singleLine = true,
                    enabled = !isSaving,
                    modifier = Modifier.fillMaxWidth()
                )
                errorMessage?.let { msg ->
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = msg,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    val trimmed = name.trim()
                    val edOk = editeurIdRaw.trim().toIntOrNull()?.let { it > 0 } == true
                    if (trimmed.isNotEmpty() && edOk) {
                        onSubmit(trimmed, description, editeurIdRaw, idERaw)
                    }
                },
                enabled = !isSaving &&
                    name.trim().isNotEmpty() &&
                    editeurIdRaw.trim().toIntOrNull()?.let { it > 0 } == true
            ) {
                if (isSaving) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(18.dp),
                        strokeWidth = 2.dp
                    )
                } else {
                    Text("Add")
                }
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss,
                enabled = !isSaving
            ) {
                Text("Cancel")
            }
        }
    )
}

@Composable
private fun GameDetailDialog(
    game: Game,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = game.name,
                style = MaterialTheme.typography.titleLarge
            )
        },
        text = {
            Column(
                modifier = Modifier.verticalScroll(rememberScrollState())
            ) {
                Text(
                    text = game.description,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Close")
            }
        }
    )
}

@Composable
private fun TopBar() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "←",
            modifier = Modifier.clickable { },
            fontSize = 20.sp
        )
        Text(
            text = "FestiMob",
            modifier = Modifier.padding(start = 16.dp),
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun BottomMenuBar() {
    var selected by remember { mutableIntStateOf(0) }
    val items = listOf(
        Triple("Home", Icons.Default.Home, 0),
        Triple("Editions", Icons.Default.Album, 1),
        Triple("Profile", Icons.Default.Person, 2)
    )
    NavigationBar(modifier = Modifier.fillMaxWidth()) {
        items.forEach { (label, icon, index) ->
            NavigationBarItem(
                selected = selected == index,
                onClick = { selected = index },
                icon = { Icon(icon, contentDescription = label) },
                label = { Text(label) }
            )
        }
    }
}

@Composable
private fun RealDropdownFilterBox(
    label: String,
    options: List<String>,
    onSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {
        OutlinedTextField(
            value = label,
            onValueChange = {},
            readOnly = true,
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth(),
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) }
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option) },
                    onClick = {
                        onSelected(option)
                        expanded = false
                    }
                )
            }
        }
    }
}

@Composable
private fun GameCard(
    game: Game,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f),
            shape = MaterialTheme.shapes.medium,
            color = MaterialTheme.colorScheme.surfaceVariant
        ) {
            if (game.imageUrl.isNotBlank()) {
                AsyncImage(
                    model = game.imageUrl,
                    contentDescription = game.name,
                    modifier = Modifier.fillMaxSize()
                )
            } else {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "—",
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
        Text(
            text = game.name,
            modifier = Modifier.padding(top = 4.dp),
            style = MaterialTheme.typography.labelSmall,
            maxLines = 2
        )
    }
}
