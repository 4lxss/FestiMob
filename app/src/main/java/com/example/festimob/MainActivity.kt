package com.example.festimob

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.festimob.data.UserPreferencesRepository
import com.example.festimob.data.local.LocalDataPlaceholder
import com.example.festimob.data.repositories.OfflineEditorsRepository
import com.example.festimob.ui.editor.EditorScreen
import com.example.festimob.ui.editor.EditorUiState
import com.example.festimob.ui.editor.EditorViewModel
import com.example.festimob.ui.theme.FestiMobTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FestiMobTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    EditorScreen(
                        modifier = Modifier.padding(innerPadding),
                        viewModel(factory = EditorViewModel.Factory)
                    )
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    FestiMobTheme {
        Greeting("Android")
    }
}