package com.example.festimob.ui.editor

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.festimob.R
import com.example.festimob.data.models.Editor
/*
@Composable
fun EditorScreen(
    modifier: Modifier = Modifier,
    // Inject the ViewModel using the Factory we defined
    viewModel: EditorViewModel = viewModel(factory = EditorViewModel.Factory)
) {
    // Collect the UI state safely
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    EditorList(
        modifier = modifier,
        editors = uiState.editorsList,
        selectLayout = viewModel::selectLayout
    )
}

@Composable
fun EditorList(
    editors: List<Editor>,
    selectLayout : (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(modifier = modifier) {
        items(items = editors, key = { it.id }) { editor ->
            // This is where you design your individual row
            Text(
                text = editor.name,
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}
*/

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditorScreen(
    modifier: Modifier = Modifier,
    // Inject the ViewModel using the Factory we defined
    viewModel: EditorViewModel = viewModel(factory = EditorViewModel.Factory),
    uiState: EditorUiState,
    selectLayout : (Boolean) -> Unit,
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
                        Icon(
                            painter = painterResource(uiState.toggleIcon),
                            contentDescription = stringResource(uiState.toggleContentDescription),
                            tint = MaterialTheme.colorScheme.onBackground
                        )
                    }
                },
                colors = TopAppBarDefaults.largeTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.inversePrimary
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
fun EditorListGridLayout(modifier: Modifier, contentPadding: PaddingValues) {
    TODO("Not yet implemented")
}

@Composable
fun EditorListLinearLayout(modifier: Modifier, contentPadding: PaddingValues) {
    TODO("Not yet implemented")
}