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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditorAddForm(
    navigateBack: () -> Unit,
    viewModel: EditorViewModel,
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
        CustomTextField(label = "Editor name", placeholder = "")

        Spacer(modifier = Modifier.height(24.dp))

        // --- EDITOR CONTACT ---
        FormSectionTitle("Main Contact")
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            CustomTextField(modifier = Modifier.weight(1f), label = "Name", placeholder = "Add Name")
            CustomTextField(modifier = Modifier.weight(1f), label = "Firstname", placeholder = "Add Firstname")
        }
        CustomTextField(label = "Job", placeholder = "Add Job")
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            CustomTextField(modifier = Modifier.weight(1f), label = "Email", placeholder = "Add Email")
            CustomTextField(modifier = Modifier.weight(1f), label = "Phone", placeholder = "Add Phone number")
        }

        Spacer(modifier = Modifier.height(24.dp))

        // --- Editor ADDRESS ---
        FormSectionTitle("Billing Address")
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            CustomTextField(modifier = Modifier.weight(2f), label = "Street", placeholder = "Number & Street Name")
            CustomTextField(modifier = Modifier.weight(1f), label = "Postcode", placeholder = "Add Postcode")
        }
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            CustomTextField(modifier = Modifier.weight(1f), label = "City", placeholder = "Add City")
            CustomTextField(modifier = Modifier.weight(1f), label = "Country", placeholder = "Add Country")
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
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.padding(bottom = 12.dp)) {
        Text(text = label, color = Color.White, style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(4.dp))
        TextField(
            value = "", // Link to your state here
            onValueChange = {},
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