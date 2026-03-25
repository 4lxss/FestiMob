package com.example.festimob.ui.festival

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.festimob.R
import com.example.festimob.data.api.Festival
import com.example.festimob.ui.AppViewModelProvider
import com.example.festimob.ui.viewmodels.UiState

@Composable
fun FestivalListScreen(
    navigateToFestivalEntry: () -> Unit,
    navigateToFestivalDetails: (Int) -> Unit,
    festivalListViewModel: FestivalListViewModel = viewModel(
        factory = AppViewModelProvider.Factory
    )
) {
    FestivalListScreen(
        festivalListViewModel = festivalListViewModel,
        uiState = festivalListViewModel.uiState.collectAsState().value,
        selectLayout = festivalListViewModel::selectLayout
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun FestivalListScreen(
    festivalListViewModel: FestivalListViewModel,
    uiState: FestivalListUiState,
    selectLayout: (Boolean) -> Unit
) {
    val isLinearLayout = uiState.isLinearLayout
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.top_bar_name)) },
                actions = {
                    IconButton(
                        onClick = {
                            selectLayout(!isLinearLayout)
                        }
                    ) {
                        if (festivalListViewModel.state.value is UiState.Success) {
                            IconButton(onClick = { selectLayout(!isLinearLayout) }) {
                                Icon(
                                    painter = painterResource(uiState.toggleIcon),
                                    contentDescription = stringResource(uiState.toggleContentDescription)
                                )
                            }
                        }
                    }
                },
                colors = TopAppBarDefaults.largeTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.inversePrimary
                )
            )
        }
    ) { innerPadding ->
        when (val dataState = festivalListViewModel.state.value) {
            is UiState.Loading -> {
                println("TestingA: A")
                LoadingView()
            }
            is UiState.Error -> {
                println("TestingB: B")
                    ErrorView(message = dataState.message)
            }
            is UiState.Success -> {
                println("TestingC: C")
                println("Testing" + dataState.festivals)
                // Si succès, on utilise le layout choisi par l'utilisateur
                val modifier = Modifier.padding(innerPadding)
                if (isLinearLayout) {
                    FestivalListLinearLayout(
                        festivals = dataState.festivals, // On passe les vraies données
                        modifier = modifier.fillMaxWidth()
                    )
                } else {
                    FestivalListGridLayout(
                        festivals = dataState.festivals, // On passe les vraies données
                        modifier = modifier
                    )
                }
            }
        }
    }
}

@Composable
fun FestivalListLinearLayout(
    festivals: List<Festival>,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp)
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = contentPadding,
        verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_small)),
    ) {
        items(
            items = festivals,
            key = { festival -> festival.id_f }
        ) { festival ->
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primary
                ),
                shape = MaterialTheme.shapes.medium
            ) {
                Text(
                    text = festival.name,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(dimensionResource(R.dimen.padding_medium)),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
fun FestivalListGridLayout(
    festivals: List<Festival>,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp)
) {
    LazyVerticalGrid(
        modifier = modifier,
        columns = GridCells.Fixed(3),
        contentPadding = contentPadding,
        verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_medium)),
        horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_medium))
    ) {
        items(
            items = festivals,
            key = { festival -> festival.id_f }
        ) { festival ->
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primary
                ),
                modifier = Modifier.height(110.dp),
                shape = MaterialTheme.shapes.medium
            ) {
                Text(
                    text = festival.name,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier
                        .fillMaxHeight()
                        .wrapContentHeight(Alignment.CenterVertically)
                        .padding(dimensionResource(R.dimen.padding_small))
                        .align(Alignment.CenterHorizontally),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

