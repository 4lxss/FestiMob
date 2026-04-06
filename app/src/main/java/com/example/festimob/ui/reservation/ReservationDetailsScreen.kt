package com.example.festimob.ui.reservation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReservationDetailScreen(
    reservationId: Int,
    viewModel: ReservationViewModel,
    navigateToEdit: (Int) -> Unit,
    navigateBack: () -> Unit
) {
    val uiState = viewModel.uiState
    val reservation = uiState.reservations.find { it.id_r == reservationId }
    val logsForReservation = uiState.logs.filter { log ->
        reservation?.id_reservant?.let { it == log.id_e } ?: false
    }
    val editeur = reservation?.id_reservant?.let { id ->
        uiState.editeurs.find { it.id_e == id }
    }



    var showDeleteDialog by rememberSaveable { mutableStateOf(false) }
    var showAddLogDialog by remember { mutableStateOf(false) }
    var logContent by remember { mutableStateOf("") }

    if (reservation == null) {
        navigateBack()
        return
    }

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Supprimer la réservation ?") },
            text = { Text("Cette action est irréversible.") },
            confirmButton = {
                TextButton(onClick = {
                    showDeleteDialog = false
                    viewModel.deleteReservation(reservationId) { navigateBack() }
                }) { Text("Supprimer", color = MaterialTheme.colorScheme.error) }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) { Text("Annuler") }
            }
        )
    }

    if (showAddLogDialog) {
        AlertDialog(
            onDismissRequest = { showAddLogDialog = false },
            title = { Text("Ajouter un log") },
            text = {
                OutlinedTextField(
                    value = logContent,
                    onValueChange = { logContent = it },
                    label = { Text("Contenu du log") },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 3
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        reservation.id_reservant?.let { id_e ->
                            viewModel.addLog(id_e, logContent, reservation.id_f ) {
                                logContent = ""
                                showAddLogDialog = false
                            }
                        }
                    },
                    enabled = logContent.isNotBlank() && reservation.id_reservant != null
                ) { Text("Ajouter") }
            },
            dismissButton = {
                TextButton(onClick = { showAddLogDialog = false; logContent = "" }) {
                    Text("Annuler")
                }
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(reservation.name_r) },
                navigationIcon = {
                    IconButton(onClick = navigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Retour")
                    }
                },
                actions = {
                    IconButton(onClick = { navigateToEdit(reservationId) }) {
                        Icon(Icons.Default.Edit, contentDescription = "Modifier")
                    }
                    IconButton(onClick = { showDeleteDialog = true }) {
                        Icon(
                            Icons.Default.Delete,
                            contentDescription = "Supprimer",
                            tint = MaterialTheme.colorScheme.error
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // ─── Infos générales ───────────────────────────────
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text("Informations", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    DetailRow("Type", reservation.type_reservation)
                    DetailRow("État", when (reservation.state) {
                        "paid" -> "Payée"
                        "facture" -> "Facturée"
                        else -> "Réservée"
                    })
                    DetailRow("Total", "${reservation.total_price} €")
                    DetailRow("Chaises", reservation.nb_chair.toString())
                    DetailRow("Multiprises", reservation.multi_socket.toString())
                    editeur?.let { DetailRow("Éditeur", it.name) }
                }
            }

            // ─── Zones ─────────────────────────────────────────
            if (reservation.zones.isNotEmpty()) {
                Card {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text("Zones réservées", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                        reservation.zones.forEach { zone ->
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(zone.nom_zone.ifBlank { "Zone #${zone.id_zt}" })
                                Text("${zone.nb_table} table(s)", fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }

            // ─── Jeux ──────────────────────────────────────────
            if (reservation.jeux.isNotEmpty()) {
                Card {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text("Jeux", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                        reservation.jeux.forEach { jeu ->
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(jeu.name.ifBlank { "Jeu #${jeu.id_j}" }, modifier = Modifier.weight(1f))
                                Text("x${jeu.nb_exemplaires}", fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }

            // ─── Logs ──────────────────────────────────────────
            Card {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Logs de contact", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                        if (reservation.id_reservant != null) {
                            TextButton(onClick = { showAddLogDialog = true }) {
                                Text("+ Ajouter")
                            }
                        }
                    }

                    if (logsForReservation.isEmpty()) {
                        Text(
                            "Aucun log pour cet éditeur",
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            style = MaterialTheme.typography.bodySmall
                        )
                    } else {
                        logsForReservation.forEach { log ->
                            HorizontalDivider()
                            Column(modifier = Modifier.padding(vertical = 4.dp)) {
                                Text(log.content, style = MaterialTheme.typography.bodyMedium)
                                if (log.date.isNotBlank()) {
                                    Text(
                                        log.date,
                                        style = MaterialTheme.typography.labelSmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(80.dp))
        }
    }
}

@Composable
private fun DetailRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Text(value, fontWeight = FontWeight.Bold)
    }
}