package com.example.festimob.ui.festival

import com.example.festimob.R
import com.example.festimob.ui.AppViewModelProvider
import com.example.festimob.ui.theme.FestiMobTheme

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.festimob.data.api.Festival
import com.example.festimob.ui.utils.formatIsoNative
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FestivalDetailsScreen(
    navigateToEditItem: (Int) -> Unit,
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: FestivalDetailsViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val uiState = viewModel.uiState.collectAsState()
    val coroutineScope = rememberCoroutineScope()

    androidx.compose.runtime.LaunchedEffect(key1 = true) {
        viewModel.checkConnection()
    }

    val isOnline by viewModel.isOnline

    Scaffold(
        topBar = {
            Column(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.inversePrimary)
                    .padding(8.dp)
            ) {
                CenterAlignedTopAppBar(
                    title = {
                        Text(
                            text = "Détails",
                            style = MaterialTheme.typography.titleLarge
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = navigateBack) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Retour"
                            )
                        }
                    },
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                        containerColor = Color.Transparent, // Fond transparent pour voir l'inversePrimary derrière
                        titleContentColor = MaterialTheme.colorScheme.onSurfaceVariant, // Couleur du titre
                        navigationIconContentColor = MaterialTheme.colorScheme.onSurfaceVariant, // Couleur flèche
                        actionIconContentColor = MaterialTheme.colorScheme.onSurfaceVariant // Couleur icônes droite
                    )
                )
            }
        },
    ) { innerPadding ->
        FestivalDetailsBody(
            festivalDetailsUiState = uiState.value,
            onModify = {
                navigateToEditItem(uiState.value.festivalDetails.id_f)
            },
            onDelete = {
                coroutineScope.launch {
                    viewModel.deleteFestival()
                    navigateBack()
                }
            },
            modifier = Modifier
                .padding(
                    start = innerPadding.calculateStartPadding(LocalLayoutDirection.current),
                    end = innerPadding.calculateEndPadding(LocalLayoutDirection.current),
                    top = innerPadding.calculateTopPadding()
                )
                .verticalScroll(rememberScrollState()),
            isOnline = isOnline
        )
    }
}

@Composable
private fun FestivalDetailsBody(
    festivalDetailsUiState: FestivalDetailsUiState,
    onModify: () -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier,
    isOnline: Boolean,
) {
    Column(
        modifier = modifier.padding(dimensionResource(id = R.dimen.padding_medium)),
        verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.padding_medium))
    ) {
        var deleteConfirmationRequired by rememberSaveable { mutableStateOf(false) }

        FestivalDetails(
            festival = festivalDetailsUiState.festivalDetails.toFestival(),
            modifier = Modifier.fillMaxWidth()
        )
        if (isOnline) {
            Button(
                onClick = onModify,
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.small,
                enabled = true
            ) {
                Text(stringResource(R.string.modify))
            }

            OutlinedButton(
                onClick = { deleteConfirmationRequired = true },
                shape = MaterialTheme.shapes.small,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(stringResource(R.string.delete))
            }
        }
        if (deleteConfirmationRequired) {
            DeleteConfirmationDialog(
                onDeleteConfirm = {
                    deleteConfirmationRequired = false
                    onDelete()
                },
                onDeleteCancel = { deleteConfirmationRequired = false },
                modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_medium))
            )
        }
    }
}

@Composable
fun FestivalDetails(
    festival: Festival, modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            contentColor = MaterialTheme.colorScheme.onPrimaryContainer
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(dimensionResource(id = R.dimen.padding_medium)),
            verticalArrangement = Arrangement.spacedBy(
                dimensionResource(id = R.dimen.padding_medium)
            )
        ) {
            Text(festival.name)
            FestivalDetailsRow(
                labelResID = R.string.start_date,
                festivalDetail = formatIsoNative(festival.start_date),
                modifier = Modifier.padding(
                    horizontal = dimensionResource(id = R.dimen.padding_medium)
                )
            )
            FestivalDetailsRow(
                labelResID = R.string.end_date,
                festivalDetail = formatIsoNative(festival.end_date),
                modifier = Modifier.padding(
                    horizontal = dimensionResource(id = R.dimen.padding_medium)
                )
            )

            // Meubles
            FestivalDetailsRow(
                labelResID = R.string.nb_table_big,
                festivalDetail = festival.nb_table_big.toString(),
                modifier = Modifier.padding(
                    horizontal = dimensionResource(id = R.dimen.padding_medium)
                )
            )
            FestivalDetailsRow(
                labelResID = R.string.nb_table_small,
                festivalDetail = festival.nb_table_small.toString(),
                modifier = Modifier.padding(
                    horizontal = dimensionResource(id = R.dimen.padding_medium)
                )
            )
            FestivalDetailsRow(
                labelResID = R.string.nb_table_mairie,
                festivalDetail = festival.nb_table_mairie.toString(),
                modifier = Modifier.padding(
                    horizontal = dimensionResource(id = R.dimen.padding_medium)
                )
            )
            FestivalDetailsRow(
                labelResID = R.string.nb_chair,
                festivalDetail = festival.nb_chair.toString(),
                modifier = Modifier.padding(
                    horizontal = dimensionResource(id = R.dimen.padding_medium)
                )
            )
            FestivalDetailsRow(
                labelResID = R.string.nb_chair_mairie,
                festivalDetail = festival.nb_chair_mairie.toString(),
                modifier = Modifier.padding(
                    horizontal = dimensionResource(id = R.dimen.padding_medium)
                )
            )
            FestivalDetailsRow(
                labelResID = R.string.price_multi_socket,
                festivalDetail = festival.formatedPrice(),
                modifier = Modifier.padding(
                    horizontal = dimensionResource(id = R.dimen.padding_medium)
                )
            )
        }
    }
}

@Composable
private fun FestivalDetailsRow(
    @StringRes labelResID: Int, festivalDetail: String, modifier: Modifier = Modifier
) {
    Row(modifier = modifier) {
        Text(stringResource(labelResID))
        Spacer(modifier = Modifier.weight(1f))
        Text(text = festivalDetail, fontWeight = FontWeight.Bold)
    }
}

@Composable
private fun DeleteConfirmationDialog(
    onDeleteConfirm: () -> Unit,
    onDeleteCancel: () -> Unit,
    modifier: Modifier = Modifier
) {
    AlertDialog(onDismissRequest = { /* Do nothing */ },
        title = { Text(stringResource(R.string.attention)) },
        text = { Text(stringResource(R.string.delete_question)) },
        modifier = modifier,
        dismissButton = {
            TextButton(onClick = onDeleteCancel) {
                Text(stringResource(R.string.no))
            }
        },
        confirmButton = {
            TextButton(onClick = onDeleteConfirm) {
                Text(stringResource(R.string.yes))
            }
        })
}

@Preview(showBackground = true)
@Composable
fun FestivalDetailsScreenPreview() {
    FestiMobTheme() {
        FestivalDetailsBody(
            FestivalDetailsUiState(
                outOfStock = true,
                festivalDetails = FestivalDetails()
            ),
            onModify = {},
            onDelete = {},
            isOnline = false
        )
    }
}