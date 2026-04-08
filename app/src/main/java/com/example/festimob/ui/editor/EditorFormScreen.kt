package com.example.festimob.ui.editor

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.MutableCreationExtras
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.festimob.FestiMobApplication
import com.example.festimob.data.models.Address
import com.example.festimob.data.models.Editor
import com.example.festimob.data.models.EditorState
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

@Composable
fun EditorFormScreen(
    navigateBack: () -> Unit
) {
    val context = LocalContext.current.applicationContext as FestiMobApplication

    // 2. On crée des extras manuellement et on y injecte l'APPLICATION_KEY
    val extras = MutableCreationExtras().apply {
        set(ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY, context)
    }

    var viewModel : EditorFormViewModel = viewModel(
        factory = com.example.festimob.ui.editor.EditorFormViewModel.Factory,
        extras = extras
    )

    val coroutineScope = rememberCoroutineScope()

    EditorAddForm(
        navigateBack = navigateBack,
        viewModel = viewModel,
        onSave = {
            coroutineScope.launch {
                viewModel.saveForm()
                navigateBack()
            }
        },
        editorDetails = viewModel.uiState.editorDetails,
        onValueChange = viewModel::updateUiState
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditorAddForm(
    editorDetails : EditorDetails,
    onValueChange: (EditorDetails) -> Unit,
    navigateBack: () -> Unit,
    viewModel: EditorFormViewModel,
    onSave: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF009688)) // The teal background from the website
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        // --- HEADER ---
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Add new Editor",
                style = MaterialTheme.typography.headlineMedium,
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
            IconButton(onClick = navigateBack) {
                Icon(Icons.Default.Close, contentDescription = "Close", tint = Color.White)
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // --- EDITOR INFO ---
        FormSectionTitle("Information")
        CustomTextField(
            label = "Editor name",
            placeholder = "",
            value = editorDetails.name,
            onValueChange = {onValueChange(editorDetails.copy(name = it))}
        )

        Spacer(modifier = Modifier.height(24.dp))

        // --- EDITOR CONTACT ---
        FormSectionTitle("Main Contact")
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            CustomTextField(
                modifier = Modifier.weight(1f),
                label = "Name",
                placeholder = "Add Name",
                value = editorDetails.contactName,
                onValueChange = {onValueChange(editorDetails.copy(contactName = it))}
            )
            CustomTextField(
                modifier = Modifier.weight(1f),
                label = "Firstname",
                placeholder = "Add Firstname",
                value = editorDetails.contactFirstname,
                onValueChange = {onValueChange(editorDetails.copy(contactFirstname = it))}
                )
        }
        CustomTextField(
            label = "Job",
            placeholder = "Add Job",
            value = editorDetails.contactJob,
            onValueChange = {onValueChange(editorDetails.copy(contactJob = it))}
        )
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            CustomTextField(
                modifier = Modifier.weight(1f),
                label = "Email",
                placeholder = "Add Email",
                value = editorDetails.contactEmail,
                onValueChange = {onValueChange(editorDetails.copy(contactEmail = it))}
                )
            CustomTextField(
                modifier = Modifier.weight(1f),
                label = "Phone",
                placeholder = "Add Phone number",
                value = editorDetails.contactPhone,
                onValueChange = {onValueChange(editorDetails.copy(contactPhone = it))}
                )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // --- Editor ADDRESS ---
        FormSectionTitle("Billing Address")
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            CustomTextField(
                modifier = Modifier.weight(2f),
                label = "Street",
                placeholder = "Number & Street Name",
                value = editorDetails.billingStreet,
                onValueChange = {onValueChange(editorDetails.copy(billingStreet = it))}
            )
            CustomTextField(
                modifier = Modifier.weight(1f),
                label = "Postcode",
                placeholder = "Add Postcode",
                value = editorDetails.billingPostcode,
                onValueChange = {onValueChange(editorDetails.copy(billingPostcode = it))}
            )
        }
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            CustomTextField(
                modifier = Modifier.weight(1f),
                label = "City",
                placeholder = "Add City",
                value = editorDetails.billingCity,
                onValueChange = {onValueChange(editorDetails.copy(billingCity = it))}
            )
            CustomTextField(
                modifier = Modifier.weight(1f),
                label = "Country",
                placeholder = "Add Country",
                value = editorDetails.billingCountry,
                onValueChange = {onValueChange(editorDetails.copy(billingCountry = it))}
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        // --- BUTTONS ---
        OutlinedButton(
            onClick = navigateBack,
            modifier = Modifier.fillMaxWidth().height(56.dp),
            colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White),
            border = BorderStroke(1.dp, Color.White),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text("Cancel", fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(12.dp))

        Button(
            onClick = onSave,
            enabled = viewModel.uiState.isEntryValid,
            modifier = Modifier.fillMaxWidth().height(56.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color.White, contentColor = Color(0xFF009688)),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text("Save", fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun FormSectionTitle(title: String) {
    Text(
        text = title,
        color = Color.White,
        style = MaterialTheme.typography.titleLarge,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(bottom = 8.dp)
    )
}

@Composable
fun CustomTextField(
    label: String,
    placeholder: String,
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.padding(bottom = 12.dp)) {
        Text(text = label, color = Color.White, style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(4.dp))
        TextField(
            value = value, // Link to your state here
            onValueChange = onValueChange,
            placeholder = { Text(placeholder, color = Color.Gray.copy(alpha = 0.6f)) },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color(0xFFF1F8F7),
                unfocusedContainerColor = Color(0xFFF1F8F7),
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            )
        )
    }
}

data class EditorDetails(
    val name : String = "",
    val contactName : String = "",
    val contactFirstname : String = "",
    val contactJob : String = "",
    val contactEmail : String = "",
    val contactPhone : String = "",
    val billingStreet : String = "",
    val billingPostcode : String = "",
    val billingCity : String = "",
    val billingCountry : String = "",
)

fun EditorDetails.toEditor(
    id: Int = 0,
    state: EditorState = EditorState.A,
    isPresent: Boolean = false,
    bill: String = "",
    imageUrl: String? = null
): Editor {
    return Editor(
        id = id,
        name = name,
        state = state,
        present = isPresent,
        bill = bill,
        imageUrl = imageUrl,
        address = Address(
            street = billingStreet,
            city = billingCity,
            country = billingCountry,
            postalCode = billingPostcode
        )
    )
}