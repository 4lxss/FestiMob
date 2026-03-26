package com.example.festimob.ui.editor

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.example.festimob.R
import com.example.festimob.data.local.LocalDataPlaceholder
import com.example.festimob.data.models.Editor
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
        }
    ) { innerPadding ->
        val modifier = Modifier
            .padding(
                top = dimensionResource(R.dimen.padding_medium),
                start = dimensionResource(R.dimen.padding_medium),
                end = dimensionResource(R.dimen.padding_medium),
            )
        if (isLinearLayout) {
            EditorListLinearLayout(
                modifier = modifier.fillMaxWidth(),
                contentPadding = innerPadding
            )
        } else {
            EditorListGridLayout(
                modifier = modifier,
                contentPadding = innerPadding,
            )
        }
    }
}

@Composable
fun EditorListGridLayout(
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
                AsyncImage(
                    model = editor.imageUrl, // Coil handles url
                    contentDescription = "${editor.name}'s logo or picture",
                    modifier = Modifier
                        .size(64.dp)
                        .clip(CircleShape),
                    // Show a placeholder while loading or if URL is null
                    placeholder = painterResource(R.drawable.default_editor_image),
                    error = painterResource(R.drawable.error_loading_image)
                )
                Text(
                    text = editor.name,
                    color = TextWhite,
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