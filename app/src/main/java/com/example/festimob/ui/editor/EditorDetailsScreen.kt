package com.example.festimob.ui.editor

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun EditorDetailsScreen(
    editorId : Int,
    viewModel: EditorDetailsViewModel
) {
    viewModel.setUiState(editorId)
    var selectedTabIndex by rememberSaveable { mutableIntStateOf(0) }


    Spacer(Modifier.padding(4.dp))
    Text("Congrats on reaching Editor ${viewModel.uiState.editor?.id} Details screen")
}

enum class EditorTab(val title: String) {
    Contacts("Contacts"),
    Games("Games"),
    Reservations("Reservations"),
    Other("Other")
}