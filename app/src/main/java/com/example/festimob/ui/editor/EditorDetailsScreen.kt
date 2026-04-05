package com.example.festimob.ui.editor

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PrimaryScrollableTabRow
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.room.util.TableInfo
import com.example.festimob.R
import com.example.festimob.ui.theme.AccentTurquoise
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
                    /*
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
                    */
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
                    Tab(
                        selected = selectedTabIndex == index,
                        onClick = {
                            selectedTabIndex = index
                        },
                        text = {
                            Text(
                                text = tab.title,
                                style = MaterialTheme.typography.titleSmall
                            )
                        },
                    )
                }
            }
            Column(modifier = Modifier.padding(dimensionResource(R.dimen.padding_medium))) {
                Text(
                    text = "Current Tab: ${tabs[selectedTabIndex].title}",
                    color = TextWhite
                )
            }
            Text("Congrats on reaching Editor ${viewModel.uiState.editor?.id} Details screen", color = TextWhite)
        }
    }

}

enum class EditorTab(val title: String) {
    Contacts("Contacts"),
    Games("Games"),
    Reservations("Reservations"),
    Other("Other")
}