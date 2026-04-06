package com.example.festimob.ui.festival

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Icon
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.material.icons.filled.Delete
import com.example.festimob.R
import com.example.festimob.data.api.ZoneTarif
import com.example.festimob.ui.AppViewModelProvider
import com.example.festimob.ui.theme.FestiMobTheme
import com.example.festimob.ui.utils.DatePickerField
import kotlinx.coroutines.launch
import java.util.Currency
import java.util.Locale

@Composable
fun FestivalEntryScreen(
    navigateBack: () -> Unit,
    isEditMode: Boolean = false,
    festivalId: Int = 0,
    viewModel: FestivalEntryViewModel
) {
    val coroutineScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    val uiState = viewModel.festivalUiState
    LaunchedEffect(uiState.errorMessage) {
        uiState.errorMessage?.let { message ->
            snackbarHostState.showSnackbar(
                message = message,
                actionLabel = "OK"
            )
            viewModel.resetErrorMessage()
        }
    }

    LaunchedEffect(Unit) {
        if (isEditMode && festivalId != 0) {
            viewModel.loadFestivalData(festivalId)
        } else {
            viewModel.resetToDefault()
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { innerPadding ->
        FestivalEntryBody(
            festivalUiState = viewModel.festivalUiState,
            onFestivalValueChange = viewModel::updateUiState,
            onSaveClick = {
                coroutineScope.launch {
                    val isSuccess = viewModel.saveFestival()
                    if (isSuccess) {
                        navigateBack()
                    }
                }
            },
            modifier = Modifier
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .fillMaxWidth(),
            onZoneAdd = { viewModel.addZone() },
            onZoneRemove = { index -> viewModel.removeZone(index) },
            onZoneUpdate = { index, zone -> viewModel.updateZone(index, zone) },
        )
    }
}

@Composable
fun FestivalEntryBody(
    festivalUiState: FestivalUiState,
    onFestivalValueChange: (FestivalDetails) -> Unit,
    onSaveClick: () -> Unit,
    onZoneUpdate: (Int, ZoneTarif) -> Unit,
    onZoneAdd: () -> Unit,
    onZoneRemove: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.padding_large)),
        modifier = modifier.padding(dimensionResource(id = R.dimen.padding_medium))
    ) {
        FestivalInputForm(
            festivalDetails = festivalUiState.festivalDetails,
            onValueChange = onFestivalValueChange,
            modifier = Modifier.fillMaxWidth(),
            onZoneUpdate = onZoneUpdate,
            onZoneAdd = onZoneAdd,
            onZoneRemove = onZoneRemove,
        )
        Button(
            onClick = onSaveClick,
            enabled = festivalUiState.isEntryValid,
            shape = MaterialTheme.shapes.small,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = stringResource(R.string.save_action))
        }
    }
}

@Composable
fun FestivalInputForm(
    festivalDetails: FestivalDetails,
    onZoneUpdate: (Int, ZoneTarif) -> Unit,
    onZoneAdd: () -> Unit,
    onZoneRemove: (Int) -> Unit,
    modifier: Modifier = Modifier,
    onValueChange: (FestivalDetails) -> Unit = {},
    enabled: Boolean = true
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.padding_medium))
    ) {
        OutlinedTextField(
            value = festivalDetails.name,
            onValueChange = { onValueChange(festivalDetails.copy(name = it)) },
            label = { Text(stringResource(R.string.festivals_title)) },
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                unfocusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                disabledContainerColor = MaterialTheme.colorScheme.secondaryContainer,
            ),
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            singleLine = true
        )

        DatePickerField(
            label = "Date de début",
            selectedDate = festivalDetails.start_date,
            onDateSelected = { onValueChange(festivalDetails.copy(start_date = it)) }
        )

        DatePickerField(
            label = "Date de fin",
            selectedDate = festivalDetails.end_date,
            onDateSelected = { onValueChange(festivalDetails.copy(end_date = it)) }
        )

        OutlinedTextField(
            value = festivalDetails.price_multi_socket,
            onValueChange = { onValueChange(festivalDetails.copy(price_multi_socket = it)) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            label = { Text(stringResource(R.string.price_multi_socket)) },
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                unfocusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                disabledContainerColor = MaterialTheme.colorScheme.secondaryContainer,
            ),
            leadingIcon = { Text(Currency.getInstance(Locale.getDefault()).symbol) },
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            singleLine = true
        )
        OutlinedTextField(
            value = festivalDetails.nb_table_big,
            onValueChange = { onValueChange(festivalDetails.copy(nb_table_big = it)) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            label = { Text(stringResource(R.string.nb_table_big)) },
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                unfocusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                disabledContainerColor = MaterialTheme.colorScheme.secondaryContainer,
            ),
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            singleLine = true
        )
        OutlinedTextField(
            value = festivalDetails.nb_table_small,
            onValueChange = { onValueChange(festivalDetails.copy(nb_table_small = it)) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            label = { Text(stringResource(R.string.nb_table_small)) },
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                unfocusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                disabledContainerColor = MaterialTheme.colorScheme.secondaryContainer,
            ),
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            singleLine = true
        )
        OutlinedTextField(
            value = festivalDetails.nb_table_mairie,
            onValueChange = { onValueChange(festivalDetails.copy(nb_table_mairie = it)) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            label = { Text(stringResource(R.string.nb_table_mairie)) },
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                unfocusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                disabledContainerColor = MaterialTheme.colorScheme.secondaryContainer,
            ),
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            singleLine = true
        )
        OutlinedTextField(
            value = festivalDetails.nb_chair,
            onValueChange = { onValueChange(festivalDetails.copy(nb_chair = it)) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            label = { Text(stringResource(R.string.nb_chair)) },
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                unfocusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                disabledContainerColor = MaterialTheme.colorScheme.secondaryContainer,
            ),
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            singleLine = true
        )
        OutlinedTextField(
            value = festivalDetails.nb_chair_mairie,
            onValueChange = { onValueChange(festivalDetails.copy(nb_chair_mairie = it)) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            label = { Text(stringResource(R.string.nb_chair_mairie)) },
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                unfocusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                disabledContainerColor = MaterialTheme.colorScheme.secondaryContainer,
            ),
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            singleLine = true
        )
        androidx.compose.material3.HorizontalDivider(
            modifier = Modifier.padding(vertical = dimensionResource(id = R.dimen.padding_medium)),
            color = MaterialTheme.colorScheme.outlineVariant
        )

        Text(
            text = "Zones et Tarifs",
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.primary
        )

        festivalDetails.zones.forEachIndexed { index, zone ->
            ZoneItem(
                index = index,
                zone = zone,
                onZoneChange = { updatedZone -> onZoneUpdate(index, updatedZone) },
                onRemove = { onZoneRemove(index) }
            )
        }

        TextButton(
            onClick = onZoneAdd,
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled
        ) {
            Icon(Icons.Default.Add, contentDescription = null)
            Text("Ajouter une zone tarifaire")
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun FestivalEntryScreenPreview() {
    FestiMobTheme() {
        FestivalEntryBody(festivalUiState = FestivalUiState(
            FestivalDetails()
        ), onFestivalValueChange = {},
            onSaveClick = {},
            onZoneUpdate = { i: Int, tarif: ZoneTarif -> },
            onZoneRemove = {},
            onZoneAdd = {})
    }
}

@Composable
fun ZoneItem(
    index: Int,
    zone: ZoneTarif,
    onZoneChange: (ZoneTarif) -> Unit,
    onRemove: () -> Unit,
    modifier: Modifier = Modifier
) {
    androidx.compose.material3.Card(
        modifier = modifier.fillMaxWidth(),
        colors = androidx.compose.material3.CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
        )
    ) {
        Column(
            modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_medium)),
            verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.padding_small))
        ) {
            androidx.compose.foundation.layout.Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
            ) {
                Text(text = "Zone ${index + 1}", style = MaterialTheme.typography.titleMedium)
                IconButton(onClick = onRemove) {
                    androidx.compose.material3.Icon(
                        imageVector = Icons.Filled.Delete,
                        contentDescription = "Supprimer",
                        tint = MaterialTheme.colorScheme.error
                    )
                }
            }

            OutlinedTextField(
                value = zone.name,
                onValueChange = { onZoneChange(zone.copy(name = it)) },
                label = { Text("Nom de la zone") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            androidx.compose.foundation.layout.Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.padding_small))
            ) {
                OutlinedTextField(
                    value = zone.nb_table.toString(),
                    onValueChange = { onZoneChange(zone.copy(nb_table = it.toIntOrNull() ?: 0)) },
                    label = { Text("Tables") },
                    modifier = Modifier.weight(1f),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
                OutlinedTextField(
                    value = zone.price_table.toString(),
                    onValueChange = { onZoneChange(zone.copy(price_table = it.toDoubleOrNull() ?: 0.00)) },
                    label = { Text("Prix Table") },
                    modifier = Modifier.weight(1f),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
                OutlinedTextField(
                    value = zone.price_m2.toString(),
                    onValueChange = { onZoneChange(zone.copy(price_m2 = it.toDoubleOrNull() ?: 0.00)) },
                    label = { Text("Prix m²") },
                    modifier = Modifier.weight(1f),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
            }
        }
    }
}
