package com.example.festimob.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

/** Editor teal palette — used for the whole app in light and dark system modes. */
private val DarkColorScheme = darkColorScheme(
    primary = BrandTeal,
    onPrimary = DarkTealBackground,
    primaryContainer = Color(0xFF00695C),
    onPrimaryContainer = Color(0xFFA7FFEB),
    secondary = PurpleGrey80,
    onSecondary = Color(0xFF1C1B1F),
    secondaryContainer = CardTeal,
    onSecondaryContainer = PurpleGrey80,
    tertiary = Pink80,
    onTertiary = Color(0xFF31111D),
    tertiaryContainer = TagContact,
    onTertiaryContainer = TextWhite,
    error = Color(0xFFFFB4AB),
    onError = Color(0xFF690005),
    errorContainer = Color(0xFF93000A),
    onErrorContainer = Color(0xFFFFDAD6),
    background = DarkTealBackground,
    onBackground = TextWhite,
    surface = CardTeal,
    onSurface = TextWhite,
    surfaceVariant = Color(0xFF0E564C),
    onSurfaceVariant = Color(0xFFB2DFDB),
    outline = AccentTurquoise,
    outlineVariant = Color(0xFF1E5C52)
)

@Composable
fun FestiMobTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = DarkColorScheme,
        typography = Typography,
        content = content
    )
}
