package com.example.festimob.ui.editor

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.example.festimob.R
import com.example.festimob.data.local.LocalDataPlaceholder
import com.example.festimob.ui.theme.AccentTurquoise
import com.example.festimob.ui.theme.CardTeal
import com.example.festimob.ui.theme.DarkTealBackground
import com.example.festimob.ui.theme.TextWhite

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditorScreen(
    modifier: Modifier = Modifier,
    // Inject the ViewModel using the Factory we defined
    viewModel: EditorViewModel = viewModel(factory = EditorViewModel.Factory),
    navigateToAddForm: () -> Unit,
    navigateToUpdateForm: (Int) -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val isLinearLayout = uiState.isLinearLayout
    Scaffold(
        containerColor = DarkTealBackground,
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.editors_top_bar_name),color = TextWhite) },
                actions = {
                    IconButton(
                        onClick = {
                            viewModel.selectLayout(!isLinearLayout)
                        }
                    ) {
                        Icon(
                            painter = painterResource(uiState.toggleIcon),
                            contentDescription = stringResource(uiState.toggleContentDescription),
                            tint = AccentTurquoise
                        )
                    }
                },
                colors = TopAppBarDefaults.largeTopAppBarColors(
                    containerColor = DarkTealBackground
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navigateToAddForm },
                containerColor = AccentTurquoise,
                contentColor = DarkTealBackground // Contrast color for the icon
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add Editor"
                )
            }
        }
    ) { innerPadding ->
        val hereModifier = Modifier
            .padding(
                top = dimensionResource(R.dimen.padding_medium),
                start = dimensionResource(R.dimen.padding_medium),
                end = dimensionResource(R.dimen.padding_medium),
            )
        if (isLinearLayout) {
            EditorListLinearLayout(
                modifier = hereModifier.fillMaxWidth(),
                contentPadding = innerPadding
            )
        } else {
            EditorListGridLayout(
                modifier = hereModifier,
                contentPadding = innerPadding,
            )
        }
    }
}

@Composable
fun EditorListGridLayout(
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp),
) {
    LazyVerticalGrid(
        modifier = modifier,
        columns = GridCells.Fixed(3),
        contentPadding = contentPadding,
        verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_medium)),
        horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_medium))
    ) {
        items(
            items = LocalDataPlaceholder.editorsPlaceholderData,
            key = { editor -> editor.id }
        ) { editor ->
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = CardTeal
                ),
                modifier = Modifier.height(200.dp),
                shape = MaterialTheme.shapes.medium
            ) {
                Column(modifier = Modifier.fillMaxSize()) {
                    // --- CARD IMAGE ---
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1.3f) // Image takes more space than the text
                    ) {
                        AsyncImage(
                            model = editor.imageUrl,
                            contentDescription = null,
                            contentScale = ContentScale.Crop, // Makes it fill the top area
                            modifier = Modifier.fillMaxSize(),
                            placeholder = painterResource(R.drawable.default_editor_image),
                            error = painterResource(R.drawable.error_loading_image),
                            fallback = painterResource(R.drawable.default_editor_image)
                        )
                    }

                    // --- CARD TEXT ---
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(0.7f) // Takes the remaining space
                            .background(Color(0xFF072924)) // Darker teal for text area
                            .padding(8.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.Start // To match website
                    ) {
                        Text(
                            text = editor.name,
                            color = Color.White,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }

            }
        }
    }
}

@Composable
fun EditorListLinearLayout(
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp)
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = contentPadding,
        verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_small)),
    ) {
        items(
            items = LocalDataPlaceholder.editorsPlaceholderData,
            key = { editor -> editor.id }
        ) { editor ->
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = CardTeal
                ),
                shape = MaterialTheme.shapes.medium
            ) {
                Text(
                    text = editor.name,
                    color = TextWhite,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(dimensionResource(R.dimen.padding_medium)),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}