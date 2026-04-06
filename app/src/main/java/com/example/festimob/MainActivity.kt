package com.example.festimob

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import com.example.festimob.ui.theme.FestiMobTheme
import com.example.festimob.ui.navigation.SmallNavigation

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FestiMobTheme {
                Column {
                    SmallNavigation()
                }
            }
        }
    }
}
