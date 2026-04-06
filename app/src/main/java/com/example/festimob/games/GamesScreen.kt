@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.festimob.games

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
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

// Main Games tab: loads games from the API, filters by age + mechanism, tap a card for details, + to add a game.
@Composable
fun GamesScreen(
    viewModel: GamesViewModel = viewModel()
) {
    var selectedCategory by remember { mutableStateOf("All") }
    var selectedMechanism by remember { mutableStateOf("All") }

    // Which edition we filter on: "default" = show all; a number string = only games with that id_e.
    val gamesScopeId = "default"

    var detailGame by remember { mutableStateOf<Game?>(null) }
    var showAddGameDialog by remember { mutableStateOf(false) }

    // First time (or when scope changes): fetch mechanism names for the dropdown, then load the grid.
    LaunchedEffect(gamesScopeId) {
        viewModel.loadMechanismFilterOptions()
        viewModel.loadGames(editionId = gamesScopeId)
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        floatingActionButton = {
            // Opens the form that POSTs a new jeu under an éditeur id (API requirement).
            FloatingActionButton(
                onClick = {
                    viewModel.clearError()
                    showAddGameDialog = true
                }
            ) {
                Text(text = "+", fontSize = 24.sp)
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
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

                    // Same filters, just re-fetch from the network (handy if data changed).
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
                            columns = GridCells.Fixed(3),
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

    // Popup with full description when you tap a game card.
    detailGame?.let { game ->
        GameDetailDialog(
            game = game,
            onDismiss = { detailGame = null }
        )
    }

    if (showAddGameDialog) {
        // Form dialog; calls the ViewModel which hits POST …/editeurs/{id}/jeux.
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

// Popup to create a game: name + optional text, must type a real éditeur id, optional id_e for the API row.
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

// Read-only popup: title + description (and whatever we stuffed into description from the DTO).
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

// A read-only text field that opens a dropdown; used for Age group and Mechanisms filters.
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

// One square in the grid: cover image (or placeholder) + game name; click opens a detail dialog.
@Composable
private fun GameCard(
    game: Game,
    onClick: () -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer
        ),
        shape = MaterialTheme.shapes.medium,
        onClick = onClick
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
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
                            text = "🎲",
                            style = MaterialTheme.typography.titleLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
            Text(
                text = game.name,
                modifier = Modifier.padding(top = 8.dp),
                style = MaterialTheme.typography.labelMedium,
                maxLines = 2
            )
        }
    }
}
