package com.example.festimob.ui.admin

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.TextButton
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.festimob.data.api.User
import com.example.festimob.ui.AppViewModelProvider
import com.example.festimob.ui.festival.ErrorView
import com.example.festimob.ui.festival.LoadingView

@Composable
fun AdminScreen(
    viewModel: AdminScreenViewModel? = viewModel(factory = AppViewModelProvider.Factory)
) {
    if (viewModel == null) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text("Vous n'avez pas les droits nécessaires")
        }
        return
    }

    val state by viewModel.state.collectAsState()
    var selectedUserForRole by remember { mutableStateOf<User?>(null) }
    var selectedUserForDelete by remember { mutableStateOf<User?>(null) }
    LaunchedEffect(Unit) {
        viewModel.refreshUsers()
    }

    when (state) {
        is AdminScreenState.Loading -> LoadingView()
        is AdminScreenState.Error -> ErrorView(message = (state as AdminScreenState.Error).message)
        is AdminScreenState.Success -> AdminUsersList(
            users = (state as AdminScreenState.Success).users,
            onEditRoleClick = { user ->
                selectedUserForRole = user
            },
            onDeleteClick = { user ->
                selectedUserForDelete = user
            }
        )
    }

    selectedUserForRole?.let { user ->
        AlertDialog(
            onDismissRequest = { selectedUserForRole = null },
            title = { Text("Changer le rôle") },
            text = {
                Column {
                    Text("Utilisateur: ${user.username}")
                    viewModel.allowedRoles.forEach { role ->
                        TextButton(
                            onClick = {
                                selectedUserForRole = null
                                viewModel.updateUserRole(user.id_u, role)
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(role)
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { selectedUserForRole = null }) {
                    Text("Fermer")
                }
            }
        )
    }

    selectedUserForDelete?.let { user ->
        AlertDialog(
            onDismissRequest = { selectedUserForDelete = null },
            title = { Text("Supprimer l'utilisateur") },
            text = { Text("Voulez-vous vraiment supprimer ${user.username} ?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        selectedUserForDelete = null
                        viewModel.deleteUser(user.id_u)
                    }
                ) {
                    Text("Supprimer")
                }
            },
            dismissButton = {
                TextButton(onClick = { selectedUserForDelete = null }) {
                    Text("Annuler")
                }
            }
        )
    }
}

@Composable
private fun AdminUsersList(
    users: List<User>,
    onEditRoleClick: (User) -> Unit,
    onDeleteClick: (User) -> Unit
) {
    LazyColumn {
        items(items = users, key = { user -> user.id_u }) { user ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 6.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(Modifier.weight(1f)) {
                        Text(
                            text = user.username,
                            style = MaterialTheme.typography.titleMedium,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Text(
                            text = user.role,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    Row {
                        IconButton(onClick = { onEditRoleClick(user) }) {
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = "Modifier le rôle"
                            )
                        }
                        IconButton(onClick = { onDeleteClick(user) }) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "Supprimer l'utilisateur"
                            )
                        }
                    }
                }
            }
        }
    }
}
