package com.example.festimob.ui.reservation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.festimob.data.api.reservation.Reservation
import com.example.festimob.data.api.reservation.ReservationZoneRequest
import com.example.festimob.data.api.zone.ZoneTarif

data class ZoneFormEntry(
    val id_zt: Int,
    val zoneName: String,
    var nbTable: Int
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReservationFormScreen(
    viewModel: ReservationViewModel,
    zones: List<ZoneTarif>,           // zones du festival courant
    reservationId: Int? = null,       // null = ajout, non-null = modification
    navigateBack: () -> Unit
) {
    val uiState = viewModel.uiState
    val existingReservation: Reservation? = reservationId?.let { id ->
        uiState.reservations.find { it.id_r == id }
    }
    val reservation = uiState.reservations.find { it.id_r == reservationId }
    val isEditMode = existingReservation != null
    val snackbarHostState = remember { SnackbarHostState() }

    // ─── Champs du formulaire ──────────────────────────────────
    var nameR by remember { mutableStateOf(existingReservation?.name_r ?: "") }
    var typeReservation by remember { mutableStateOf(existingReservation?.type_reservation ?: "Visiteur") }
    var nbChair by remember { mutableStateOf(existingReservation?.nb_chair?.toString() ?: "0") }
    var multiSocket by remember { mutableStateOf(existingReservation?.multi_socket?.toString() ?: "0") }
    var state by remember { mutableStateOf(existingReservation?.state ?: "reserved") }
    var selectedEditeurId by remember { mutableStateOf(existingReservation?.id_reservant) }
    var editeurExpanded by remember { mutableStateOf(false) }
    var typeExpanded by remember { mutableStateOf(false) }
    var stateExpanded by remember { mutableStateOf(false) }

    val zoneEntries = remember {
        mutableStateListOf<ZoneFormEntry>().also { list ->
            existingReservation?.zones?.forEach { rz ->
                val zoneName = zones.find { it.id_zt == rz.id_zt }?.name ?: "Zone #${rz.id_zt}"
                list.add(ZoneFormEntry(rz.id_zt, zoneName, rz.nb_table))
            }
        }
    }

    val totalPrice = remember(zoneEntries.toList(), multiSocket) {
        val zonesTotal = zoneEntries.sumOf { entry ->
            val zone = zones.find { it.id_zt == entry.id_zt }
            (zone?.price_table ?: 0.0) * entry.nbTable
        }
        val socketTotal = (multiSocket.toIntOrNull() ?: 0) * 0.0
        zonesTotal + socketTotal
    }

    val typeOptions = listOf("Visiteur", "Editeur", "Prestataire", "Association")
    val stateOptions = listOf("reserved" to "Réservée", "facture" to "Facturée", "paid" to "Payée")

    LaunchedEffect(uiState.errorMessage) {
        uiState.errorMessage?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.resetMessages()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (isEditMode) "Modifier la réservation" else "Nouvelle réservation") },
                navigationIcon = {
                    IconButton(onClick = navigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Retour")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            // ─── Nom ───────────────────────────────────────────
            OutlinedTextField(
                value = nameR,
                onValueChange = { nameR = it },
                label = { Text("Nom du réservant") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                enabled = !isEditMode  // en édition le nom ne change pas
            )

            // ─── Type ──────────────────────────────────────────
            ExposedDropdownMenuBox(
                expanded = typeExpanded,
                onExpandedChange = { typeExpanded = it }
            ) {
                OutlinedTextField(
                    value = typeReservation,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Type") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(typeExpanded) },
                    modifier = Modifier.fillMaxWidth().menuAnchor()
                )
                ExposedDropdownMenu(expanded = typeExpanded, onDismissRequest = { typeExpanded = false }) {
                    typeOptions.forEach { option ->
                        DropdownMenuItem(
                            text = { Text(option) },
                            onClick = { typeReservation = option; typeExpanded = false }
                        )
                    }
                }
            }

            // ─── Éditeur (optionnel) ───────────────────────────
            ExposedDropdownMenuBox(
                expanded = editeurExpanded,
                onExpandedChange = { editeurExpanded = it }
            ) {
                OutlinedTextField(
                    value = uiState.editeurs.find { it.id_e == selectedEditeurId }?.name ?: "Aucun",
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Éditeur associé (optionnel)") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(editeurExpanded) },
                    modifier = Modifier.fillMaxWidth().menuAnchor()
                )
                ExposedDropdownMenu(expanded = editeurExpanded, onDismissRequest = { editeurExpanded = false }) {
                    DropdownMenuItem(
                        text = { Text("Aucun") },
                        onClick = { selectedEditeurId = null; editeurExpanded = false }
                    )
                    uiState.editeurs.forEach { editeur ->
                        DropdownMenuItem(
                            text = { Text(editeur.name) },
                            onClick = { selectedEditeurId = editeur.id_e; editeurExpanded = false }
                        )
                    }
                }
            }

            // ─── État ──────────────────────────────────────────
            ExposedDropdownMenuBox(
                expanded = stateExpanded,
                onExpandedChange = { stateExpanded = it }
            ) {
                OutlinedTextField(
                    value = stateOptions.find { it.first == state }?.second ?: state,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("État") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(stateExpanded) },
                    modifier = Modifier.fillMaxWidth().menuAnchor()
                )
                ExposedDropdownMenu(expanded = stateExpanded, onDismissRequest = { stateExpanded = false }) {
                    stateOptions.forEach { (value, label) ->
                        DropdownMenuItem(
                            text = { Text(label) },
                            onClick = { state = value; stateExpanded = false }
                        )
                    }
                }
            }

            // ─── Chaises & Multiprises ─────────────────────────
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = nbChair,
                    onValueChange = { nbChair = it },
                    label = { Text("Chaises") },
                    modifier = Modifier.weight(1f),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true
                )
                OutlinedTextField(
                    value = multiSocket,
                    onValueChange = { multiSocket = it },
                    label = { Text("Multiprises") },
                    modifier = Modifier.weight(1f),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true
                )
            }

            // ─── Zones (seulement en mode ajout) ──────────────
            if (!isEditMode) {
                Text("Zones et tables", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)

                zoneEntries.forEachIndexed { index, entry ->
                    ZoneEntryRow(
                        entry = entry,
                        availableZones = zones.filter { z ->
                            zoneEntries.none { it.id_zt == z.id_zt && it != entry }
                        },
                        onZoneChange = { newIdZt ->
                            val zone = zones.find { it.id_zt == newIdZt }
                            zoneEntries[index] = entry.copy(
                                id_zt = newIdZt,
                                zoneName = zone?.name ?: "Zone #$newIdZt"
                            )
                        },
                        onNbTableChange = { nb ->
                            zoneEntries[index] = entry.copy(nbTable = nb)
                        },
                        onRemove = { zoneEntries.removeAt(index) }
                    )
                }

                TextButton(
                    onClick = {
                        val firstAvailable = zones.firstOrNull { z ->
                            zoneEntries.none { it.id_zt == z.id_zt }
                        }
                        firstAvailable?.let {
                            zoneEntries.add(ZoneFormEntry(it.id_zt, it.name, 1))
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = zoneEntries.size < zones.size
                ) {
                    Icon(Icons.Default.Add, contentDescription = null)
                    Text("Ajouter une zone")
                }

                // Total calculé
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.secondaryContainer
                    )
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Total estimé", fontWeight = FontWeight.Bold)
                        Text("${"%.2f".format(totalPrice)} €", fontWeight = FontWeight.Bold)
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // ─── Bouton sauvegarder ────────────────────────────
            val isValid = nameR.isNotBlank() && typeReservation.isNotBlank() &&
                    (isEditMode || zoneEntries.isNotEmpty())

            Button(
                onClick = {
                    if (isEditMode && existingReservation != null) {
                        viewModel.editReservation(
                            reservationId = existingReservation.id_r,
                            nbChair = nbChair.toIntOrNull() ?: 0,
                            multiSocket = multiSocket.toDoubleOrNull() ?: 0.00,
                            totalPrice = existingReservation.total_price,
                            state = state,
                            idReservant = selectedEditeurId,
                            typeReservation = typeReservation,
                            onSuccess = navigateBack
                        )
                    } else {
                        viewModel.addReservation(
                            id_u = reservation?.id_u,
                            nameR = nameR,
                            typeReservation = typeReservation,
                            nbChair = nbChair.toIntOrNull() ?: 0,
                            multiSocket = multiSocket.toDoubleOrNull() ?: 0.00,
                            state = state,
                            totalPrice = totalPrice,
                            idReservant = selectedEditeurId,
                            zones = zoneEntries.map { ReservationZoneRequest(it.id_zt, it.nbTable) },
                            onSuccess = navigateBack
                        )
                    }
                },
                enabled = isValid && !uiState.isLoading,
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.small
            ) {
                Text(if (isEditMode) "Enregistrer les modifications" else "Créer la réservation")
            }

            Spacer(modifier = Modifier.height(80.dp))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ZoneEntryRow(
    entry: ZoneFormEntry,
    availableZones: List<ZoneTarif>,
    onZoneChange: (Int) -> Unit,
    onNbTableChange: (Int) -> Unit,
    onRemove: () -> Unit
) {
    var zoneExpanded by remember { mutableStateOf(false) }
    var nbTableText by remember { mutableStateOf(entry.nbTable.toString()) }

    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
        )
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Sélecteur de zone
            ExposedDropdownMenuBox(
                expanded = zoneExpanded,
                onExpandedChange = { zoneExpanded = it },
                modifier = Modifier.weight(1f)
            ) {
                OutlinedTextField(
                    value = entry.zoneName,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Zone") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(zoneExpanded) },
                    modifier = Modifier.fillMaxWidth().menuAnchor(),
                    singleLine = true
                )
                ExposedDropdownMenu(expanded = zoneExpanded, onDismissRequest = { zoneExpanded = false }) {
                    availableZones.forEach { zone ->
                        DropdownMenuItem(
                            text = { Text("${zone.name} (${zone.price_table}€/table)") },
                            onClick = { onZoneChange(zone.id_zt!!); zoneExpanded = false }
                        )
                    }
                }
            }

            // Nb tables
            OutlinedTextField(
                value = nbTableText,
                onValueChange = {
                    nbTableText = it
                    onNbTableChange(it.toIntOrNull() ?: 1)
                },
                label = { Text("Tables") },
                modifier = Modifier.weight(0.4f),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true
            )

            IconButton(onClick = onRemove) {
                Icon(
                    Icons.Default.Delete,
                    contentDescription = "Supprimer",
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}