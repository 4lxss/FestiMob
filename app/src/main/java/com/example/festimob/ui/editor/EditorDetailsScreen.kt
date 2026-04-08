package com.example.festimob.ui.editor

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.festimob.R
import com.example.festimob.data.models.Editor
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
    LaunchedEffect(editorId) {
        viewModel.setUiState(editorId)
    }

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
            Box(modifier = Modifier.weight(1f)) {
                when (tabs[selectedTabIndex]) {
                    EditorTab.Contacts -> ContactsTab(editor)
                    EditorTab.Games -> GamesTab()
                    EditorTab.Reservations -> ResTab()
                    EditorTab.Other -> OtherTab(
                        uiState = viewModel.uiState,
                        onEditClick = { viewModel.toggleEditAddress() },
                        onSaveClick = { /* TODO: Appel API Update */ viewModel.toggleEditAddress() }
                    )
                }
            }
            Text("Congrats on reaching Editor ${viewModel.uiState.editor?.id} Details screen", color = TextWhite)
        }
    }

}

// TODO : make 1 function for each specific tab like this

// Contacts tab
@Composable
fun ContactsTab(editor: Editor?) {
    // TODO : renvoyer liste de contacts dans EditorResponse via API
    // Structure based on AddEditorContactPayload
    Column(modifier = Modifier.fillMaxSize()) {
        Card(
            modifier = Modifier.fillMaxWidth().padding(8.dp),
            colors = CardDefaults.cardColors(containerColor = CardTeal),
            shape = RoundedCornerShape(12.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Contact Principal",
                    style = MaterialTheme.typography.titleMedium,
                    color = AccentTurquoise
                )
                Spacer(modifier = Modifier.height(8.dp))
                ContactInfoRow(Icons.Default.Person, "Nom Prénom") // À lier au ViewModel
                ContactInfoRow(Icons.Default.Email, "email@exemple.com")
                ContactInfoRow(Icons.Default.Phone, "06 00 00 00 00")
            }
        }
    }
}

@Composable
fun ContactInfoRow(icon: ImageVector, detail: String) {
    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(vertical = 4.dp)) {
        Icon(icon, contentDescription = null, tint = AccentTurquoise, modifier = Modifier.size(20.dp))
        Spacer(modifier = Modifier.width(12.dp))
        Text(text = detail, color = TextWhite, style = MaterialTheme.typography.bodyMedium)
    }
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

// Other tab : image and billing address update
@Composable
fun OtherTab(
    uiState: EditorDetailsUiState,
    onEditClick: () -> Unit,
    onSaveClick: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Section Image
        Surface(
            modifier = Modifier.size(120.dp),
            shape = RoundedCornerShape(16.dp),
            color = CardTeal
        ) {
            if (uiState.editor?.imageUrl != null) {
                // AsyncImage (Coil) recommandé ici
                Text("Image Editor", color = TextWhite, modifier = Modifier.padding(8.dp))
            } else {
                Icon(Icons.Default.Image, contentDescription = null, tint = TextWhite.copy(alpha = 0.3f))
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Section Adresse de facturation
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("Adresse de facturation", style = MaterialTheme.typography.titleMedium, color = AccentTurquoise)
            Spacer(modifier = Modifier.weight(1f))
            IconButton(onClick = if (uiState.isEditingAddress) onSaveClick else onEditClick) {
                Icon(
                    imageVector = if (uiState.isEditingAddress) Icons.Default.Check else Icons.Default.Edit,
                    contentDescription = null,
                    tint = AccentTurquoise
                )
            }
        }

        if (uiState.isEditingAddress) {
            // Champs de texte modifiables (OutlinedTextField stylisé)
            AddressEditFields(uiState)
        } else {
            // Affichage simple
            Text(uiState.editor?.address?.street ?: "Rue non renseignée", color = TextWhite)
            Text("${uiState.editor?.address?.postalCode} ${uiState.editor?.address?.city}", color = TextWhite)
        }
    }
}

@Composable
fun AddressEditFields(uiState: EditorDetailsUiState) {
    TODO("Not yet implemented")
}

enum class EditorTab(val title: String) {
    Contacts("Contacts"),
    Games("Games"),
    Reservations("Reservations"),
    Other("Other")
}

// TODO : find way to add count to pass to tabs ?
