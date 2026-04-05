package com.example.festimob

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.festimob.games.GamesScreen
import com.example.festimob.ui.theme.FestiMobTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FestiMobTheme {
                GamesScreen()
            }
        }
    }
}
