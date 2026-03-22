package com.example.festimob.ui.editor

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.festimob.data.models.Editor

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
        editors = uiState.editorsList
    )
}

@Composable
fun EditorList(
    editors: List<Editor>,
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
