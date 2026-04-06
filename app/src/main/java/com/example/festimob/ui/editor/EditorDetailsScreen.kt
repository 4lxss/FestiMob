package com.example.festimob.ui.editor

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.PrimaryScrollableTabRow
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.festimob.R
import com.example.festimob.ui.theme.AccentTurquoise
import com.example.festimob.ui.theme.BrandTeal
import com.example.festimob.ui.theme.CardTeal
import com.example.festimob.ui.theme.DarkTealBackground
import com.example.festimob.ui.theme.TextWhite

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditorDetailsScreen(
    editorId : Int,
    viewModel: EditorDetailsViewModel
) {
    viewModel.setUiState(editorId)

    val editor = viewModel.uiState.editor
    val title = editor?.name ?: "Unknown editor"

    var selectedTabIndex by rememberSaveable { mutableIntStateOf(0) }
    val tabs = EditorTab.entries

    Scaffold(
        containerColor = DarkTealBackground,
        topBar = {
            TopAppBar(
                title = { Text(title,color = TextWhite) },
                actions = {
                    IconButton(
                        onClick = {
                            // TODO : add popup with buttons here
                            // update form (much smaller, see website)
                            // delete editor button
                        }
                    ) {

                        Icon(
                            imageVector = Icons.Default.MoreVert,
                            contentDescription = "More action button",
                            tint = AccentTurquoise
                        )

                    }
                },
                colors = TopAppBarDefaults.largeTopAppBarColors(
                    containerColor = DarkTealBackground
                )
            )
        },
    ) { innerPadding ->
        val hereModifier = Modifier
            .padding(
                top = dimensionResource(R.dimen.padding_medium),
                start = dimensionResource(R.dimen.padding_medium),
                end = dimensionResource(R.dimen.padding_medium),
            )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {

            PrimaryScrollableTabRow(
                selectedTabIndex,
                containerColor = DarkTealBackground,
                contentColor = AccentTurquoise
            ) { 
                tabs.forEachIndexed { index, tab ->
                    val isSelected = (selectedTabIndex == index)
                    Tab(
                        selected = isSelected,
                        onClick = {
                            selectedTabIndex = index
                        },
                        text = {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(vertical = 8.dp)
                            ) {
                                Text(
                                    text = tab.title,
                                    style = MaterialTheme.typography.titleSmall,
                                    color = if (isSelected) AccentTurquoise else TextWhite.copy(alpha = 0.7f)
                                )

                                Spacer(modifier = Modifier.width(8.dp))

                                // Badge
                                Surface(
                                    shape = CircleShape,
                                    color = if (isSelected) AccentTurquoise.copy(alpha = 0.15f) else CardTeal.copy(alpha = 0.5f),
                                    modifier = Modifier.size(24.dp)
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Text(
                                            text = "0", // Placeholder TODO : link to real viewmodel data
                                            style = MaterialTheme.typography.labelSmall,
                                            color = if (isSelected) AccentTurquoise else TextWhite.copy(alpha = 0.5f)
                                        )
                                    }
                                }
                            }
                        },
                    )
                }
            }
            Column(modifier = Modifier.padding(dimensionResource(R.dimen.padding_medium))) {
                Row() {
                    Text(
                        text = "Current Tab: ${tabs[selectedTabIndex].title}",
                        color = TextWhite,
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    when(tabs[selectedTabIndex]) {
                        EditorTab.Contacts -> {
                            OutlinedButton(
                                onClick = {
                                    // TODO : add contact add form nav here
                                },
                                modifier = Modifier
                                    .height(34.dp),
                                shape = RoundedCornerShape(10.dp),
                                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 0.dp),
                                border = BorderStroke(1.dp, BrandTeal),
                                colors = ButtonDefaults.outlinedButtonColors(
                                    contentColor = BrandTeal,
                                )
                            ) {
                                Icon(
                                    Icons.Default.Add,
                                    contentDescription = "Add Contact",
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(Modifier.width(4.dp))
                                Text(
                                    text = "Add",
                                    fontSize = 14.sp,
                                    style = MaterialTheme.typography.labelLarge
                                )
                            }
                        }
                        EditorTab.Games -> {
                            OutlinedButton(
                                onClick = {
                                    // TODO : add game add form nav here
                                },
                                modifier = Modifier
                                    .height(34.dp),
                                shape = RoundedCornerShape(10.dp),
                                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 0.dp),
                                border = BorderStroke(1.dp, BrandTeal),
                                colors = ButtonDefaults.outlinedButtonColors(
                                    contentColor = BrandTeal,
                                )
                            ) {
                                Icon(
                                    Icons.Default.Add,
                                    contentDescription = "Add Game",
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(Modifier.width(4.dp))
                                Text(
                                    text = "Add",
                                    fontSize = 14.sp,
                                    style = MaterialTheme.typography.labelLarge
                                )
                            }
                        }
                        EditorTab.Reservations -> {}
                        EditorTab.Other -> {}
                    }

                }

            }
            Text("Congrats on reaching Editor ${viewModel.uiState.editor?.id} Details screen", color = TextWhite)
        }
    }

}

// TODO : make 1 function for each specific tab like this

@Composable
fun ContactsTab() {
    // TODO : A LazyColumn containing cards or rows for contact info.
}

@Composable
fun GamesTab() {
    // TODO : A VerticalGrid or FlowRow to show the game icons/tiles, including the category and mechanism filters.
}

@Composable
fun ResTab() {
    // TODO : A structured list or card showing placement, zones, and pricing details.
    //go see the website for that
}

@Composable
fun OtherTab() {
    // TODO : A screen with an image uploader (URL/Save) and the paying address section.
}

enum class EditorTab(val title: String) {
    Contacts("Contacts"),
    Games("Games"),
    Reservations("Reservations"),
    Other("Other")
}

// TODO : find way to add count to pass to tabs ?