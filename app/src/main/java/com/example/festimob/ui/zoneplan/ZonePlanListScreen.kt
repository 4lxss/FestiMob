package com.example.festimob.ui.zoneplan

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.festimob.data.api.zone.ZonePlanCard
import com.example.festimob.ui.AppViewModelProvider
import com.example.festimob.ui.festival.ErrorView
import com.example.festimob.ui.festival.LoadingView

@Composable
fun ZonePlanListScreen(
    viewModel: ZonePlanListViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val state by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.load()
    }

    when (state) {
        is ZonePlanUiState.Loading -> LoadingView()
        is ZonePlanUiState.Error -> ErrorView((state as ZonePlanUiState.Error).message)
        is ZonePlanUiState.Success -> ZonePlanListContent((state as ZonePlanUiState.Success).zones)
    }
}

@Composable
private fun ZonePlanListContent(zones: List<ZonePlanCard>) {
    LazyColumn {
        items(items = zones, key = { it.id }) { zone ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 6.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text(text = zone.name, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Text(text = "Zone tarifaire: ${zone.zoneTarifName}", style = MaterialTheme.typography.bodyMedium)
                    Text(text = "Description: ${zone.description}", style = MaterialTheme.typography.bodyMedium)
                    Text(text = "Jeux: ${if (zone.gameNames.isEmpty()) "Aucun jeu" else zone.gameNames.joinToString(", ")}", style = MaterialTheme.typography.bodyMedium)
                    Text(text = "Nombre de tables: ${zone.tableCount}", style = MaterialTheme.typography.bodyMedium)
                }
            }
        }
    }
}
