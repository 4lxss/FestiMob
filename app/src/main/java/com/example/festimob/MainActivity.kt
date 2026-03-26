package com.example.festimob

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.festimob.ui.theme.FestiMobTheme
import com.example.festimob.games.Game
import com.example.festimob.games.GamesViewModel
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Icon

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        // Always call parent implementation first in Activity lifecycle methods.
        super.onCreate(savedInstanceState)
        // Let app content draw edge-to-edge behind system bars.
        enableEdgeToEdge()
        // Start Compose UI content tree for this Activity.
        setContent {
            // We wrap the whole screen in the app theme so colors, typography, etc. are consistent
            FestiMobTheme {
                // Show our custom games screen as the main content
                GamesScreen()
            }
        }
    }
}

@Composable
fun GamesScreen(
    // Get/create the ViewModel instance for this screen.
    viewModel: GamesViewModel = viewModel()
) {
    // Which tab is currently selected in the top tab row
    var selectedTabIndex by remember { mutableIntStateOf(1) } // 0 = Contact, 1 = Games, 2 = Resa, 3 = Other
    // What text is shown in the "Category" filter box
    var selectedCategory by remember { mutableStateOf("All") }
    // What text is shown in the "Mechanisms" filter box
    var selectedMechanism by remember { mutableStateOf("All") }

    // Labels for the tab row at the top
    val tabs = listOf("Contact", "Games", "Resa", "Other")
    val categoryOptions = listOf("All", "Strategy", "Party", "Family", "Cooperative")
    val mechanismOptions = listOf("All", "Deck Building", "Dice", "Drafting", "Worker Placement")

    // No editions for now: use one default scope id for this screen.
    val gamesScopeId = "default"

    // Load fake games once when the screen opens.
    LaunchedEffect(gamesScopeId) {
        // Ask ViewModel to fetch games for the current screen scope.
        viewModel.loadGames(editionId = gamesScopeId)
    }

    // Scaffold gives us a standard screen skeleton: content + FAB + bottom bar.
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        floatingActionButton = {
            // Circular + button in the bottom‑right corner
            FloatingActionButton(onClick = { /* TODO: open add game screen */ }) {
                Text(text = "+", fontSize = 24.sp)
            }
        },
        bottomBar = {
            // Bottom navigation menu (Home / Editions / Profile)
            BottomMenuBar()
        }
    ) { innerPadding ->
        // Main vertical layout for the screen sections.
        Column(
            modifier = Modifier
                .fillMaxSize()
                // Apply system/top/bottom insets from Scaffold.
                .padding(innerPadding)
        ) {
            // Row with "← back" and the screen title at the top
            TopBar()

            // Tabs row: Contact | Games | Resa | Other
            TabRow(
                selectedTabIndex = selectedTabIndex,
                modifier = Modifier.fillMaxWidth()
            ) {
                // Build one tab per label in the list.
                tabs.forEachIndexed { index, title ->
                    Tab(
                        // Tab is selected if its index matches current selected index.
                        selected = selectedTabIndex == index,
                        // Update selected tab when user taps.
                        onClick = { selectedTabIndex = index },
                        text = {
                            Text(
                                text = title,
                                // Make active tab label bold for visual feedback.
                                fontWeight = if (selectedTabIndex == index) FontWeight.Bold else FontWeight.Normal
                            )
                        }
                    )
                }
            }

            // Filters section container.
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                // Section title "Games"
                Text(
                    text = "Games",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                    // Vertically align dropdowns and button in the same center line.
                    verticalAlignment = Alignment.CenterVertically,
                    // Push controls to use all horizontal width with spacing behavior.
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    // Left: Category real dropdown
                    Column(modifier = Modifier.weight(1f)) {
                        Text(text = "Category", fontSize = 14.sp)
                        RealDropdownFilterBox(
                            label = selectedCategory,
                            options = categoryOptions,
                            onSelected = { newCategory ->
                                // Save user selection locally for UI state.
                                selectedCategory = newCategory
                                // Reload list with selected filters.
                                viewModel.loadGames(
                                    editionId = gamesScopeId,
                                    // Send null when "All" is selected (means no filter).
                                    category = newCategory.takeIf { it != "All" },
                                    mechanism = selectedMechanism.takeIf { it != "All" }
                                )
                            }
                        )
                    }

                    // Middle: Mechanisms real dropdown
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .padding(start = 16.dp)
                    ) {
                        Text(text = "Mechanisms", fontSize = 14.sp)
                        RealDropdownFilterBox(
                            label = selectedMechanism,
                            options = mechanismOptions,
                            onSelected = { newMechanism ->
                                // Save user selection locally for UI state.
                                selectedMechanism = newMechanism
                                // Reload list with selected filters.
                                viewModel.loadGames(
                                    editionId = gamesScopeId,
                                    category = selectedCategory.takeIf { it != "All" },
                                    // Send null when "All" is selected (means no filter).
                                    mechanism = newMechanism.takeIf { it != "All" }
                                )
                            }
                        )
                    }

                    // Real Material refresh button to manually re-fetch.
                    FilledTonalIconButton(
                        onClick = {
                            // Reload using current selected filters.
                            viewModel.loadGames(
                                editionId = gamesScopeId,
                                category = selectedCategory.takeIf { it != "All" },
                                mechanism = selectedMechanism.takeIf { it != "All" }
                            )
                        },
                        modifier = Modifier
                            .padding(start = 16.dp)
                            .size(40.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = "Refresh games"
                        )
                    }
                }
            }

            Box(
                modifier = Modifier
                    .weight(1f)
                    // Keep space around the games grid.
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                // Main area:
                // - shows a loading spinner when we're fetching games
                // - shows an error message if something fails
                // - shows a scrollable grid of cards when we have data
                when {
                    viewModel.isLoading -> {
                        // Loading state UI.
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            CircularProgressIndicator()
                        }
                    }

                    viewModel.errorMessage != null -> {
                        // Error state UI.
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Text(text = viewModel.errorMessage ?: "Error")
                        }
                    }

                    else -> {
                        // Success state UI: render games in a 4-column grid.
                        LazyVerticalGrid(
                            columns = GridCells.Fixed(4),
                            contentPadding = PaddingValues(bottom = 16.dp),
                            // Small space between cells.
                            horizontalArrangement = Arrangement.spacedBy(16.dp),
                            verticalArrangement = Arrangement.spacedBy(16.dp),
                            modifier = Modifier.fillMaxSize()
                        ) {
                            // Render one GameCard for each game in the list.
                            items(viewModel.games) { game ->
                                GameCard(game = game)
                            }
                        }
                    }
                }
            }
        }
    }
}
