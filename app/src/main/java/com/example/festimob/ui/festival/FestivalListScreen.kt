package com.example.festimob.ui.festival

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.datastore.preferences.protobuf.LazyStringArrayList.emptyList
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.festimob.R
import com.example.festimob.data.api.Festival
import com.example.festimob.ui.AppViewModelProvider
import com.example.festimob.ui.theme.FestiMobTheme
import com.example.festimob.ui.viewmodels.UiState

@Composable
fun FestivalListScreen(
    navigateToFestivalEntry: () -> Unit,
    navigateToFestivalDetails: (Int) -> Unit,
    viewModel: FestivalListViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val uiState by viewModel.uiState.collectAsState()
    val state by viewModel.state

    FestivalListContent(
        state = state,
        uiState = uiState,
        selectLayout = viewModel::selectLayout,
        onFestivalClick = navigateToFestivalDetails,
        onAddClick = navigateToFestivalEntry
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun FestivalListContent(
    state: UiState,
    uiState: FestivalListUiState,
    selectLayout: (Boolean) -> Unit,
    onFestivalClick: (Int) -> Unit,
    onAddClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.top_bar_name)) },
                actions = {
                    // On vérifie si on est en succès pour afficher le bouton de switch
                    if (state is UiState.Success) {
                        IconButton(
                            onClick = { selectLayout(!uiState.isLinearLayout) }
                        ) {
                            Icon(
                                painter = painterResource(uiState.toggleIcon),
                                contentDescription = stringResource(uiState.toggleContentDescription)
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.largeTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.inversePrimary
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddClick, // Correction : on utilise le paramètre onAddClick
                shape = MaterialTheme.shapes.medium,
                modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_large))
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = stringResource(R.string.item_entry_title)
                )
            }
        },
    ) { innerPadding ->
        when (state) {
            is UiState.Loading -> LoadingView()
            is UiState.Error -> ErrorView(message = state.message)
            is UiState.Success -> {
                val festivals = state.festivals

                // On choisit le bon composant selon uiState.isLinearLayout
                if (uiState.isLinearLayout) {
                    FestivalListLinearLayout(
                        festivals = festivals,
                        onFestivalClick = onFestivalClick,
                        modifier = Modifier.padding(innerPadding)
                    )
                } else {
                    FestivalListGridLayout(
                        festivals = festivals,
                        onFestivalClick = onFestivalClick,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }

}

@Composable
fun FestivalListLinearLayout(
    festivals: List<Festival>,
    onFestivalClick: (Int) -> Unit,
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
                onClick = { onFestivalClick(festival.id_f) },
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
    onFestivalClick: (Int) -> Unit,
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
                onClick = { onFestivalClick(festival.id_f) },
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primary
                ),
                modifier = Modifier.height(110.dp),
                shape = MaterialTheme.shapes.medium
            ) {
                Column() {
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
                    Row() {
                        Text(
                            text = formatIsoNative(festival.start_date) + "-" ,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier
                                .fillMaxHeight()
                                .wrapContentHeight(Alignment.CenterVertically)
                                .padding(dimensionResource(R.dimen.padding_small)),
                            textAlign = TextAlign.Center
                        )
                        Text(
                            text = formatIsoNative(festival.end_date),
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier
                                .fillMaxHeight()
                                .wrapContentHeight(Alignment.CenterVertically)
                                .padding(dimensionResource(R.dimen.padding_small)),
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun FestivalListScreenPreview() {
    FestiMobTheme() {
        FestivalListLinearLayout(
            festivals = listOf(
                Festival(
                    id_f = 14,
                    name = "Festival du jeu 2026",
                    start_date = "2026-04-18T00:00:00.000Z",
                    end_date = "2026-04-19T00:00:00.000Z",
                    nb_table_big = 30,
                    nb_table_small = 30,
                    nb_table_mairie = 25,
                    nb_chair = 40,
                    nb_chair_mairie = 18,
                    public = true,
                    price_multi_socket = 20
                )
            ),
            onFestivalClick = {}
        )
    }
}

