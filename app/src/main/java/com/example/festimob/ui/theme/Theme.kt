package com.example.festimob.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = TealVeryLight,
    onPrimary = TealOnLight,
    primaryContainer = TealDark,
    onPrimaryContainer = TealOnDark,
    secondary = TealLight,
    onSecondary = TealOnLight,
    secondaryContainer = TealMidDark,
    onSecondaryContainer = TealOnDark,
    tertiary = Teal,
    onTertiary = TealOnDark,
    background = TealSurfaceDark,
    onBackground = TealOnDark,
    surface = Color(0xFF102825),
    onSurface = TealOnDark,
    surfaceVariant = TealSurfaceVariantDark,
    onSurfaceVariant = TealOnDark,
    outline = TealLight
)

private val LightColorScheme = lightColorScheme(
    primary = TealMidDark,
    onPrimary = Color.White,
    primaryContainer = TealLight,
    onPrimaryContainer = Color.White,
    secondary = Teal,
    onSecondary = Color.White,
    secondaryContainer = TealSurfaceVariantLight,
    onSecondaryContainer = TealOnLight,
    tertiary = Teal,
    onTertiary = Color.White,
    background = TealSurfaceLight,
    onBackground = TealOnLight,
    surface = Color.White,
    onSurface = TealOnLight,
    surfaceVariant = TealSurfaceVariantLight,
    onSurfaceVariant = TealOnLight,
    outline = TealMidDark
)

@Composable
fun FestiMobTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
