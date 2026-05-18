package com.demuzyka.app.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColors = darkColorScheme(
    primary = DeOrange,
    onPrimary = Color.White,
    primaryContainer = DeOrangeDim,
    onPrimaryContainer = Color.White,
    secondary = DeYellow,
    onSecondary = Color.Black,
    secondaryContainer = DeYellowDim,
    background = DeBlack,
    onBackground = DeOnBackground,
    surface = DeSurface,
    onSurface = DeOnBackground,
    surfaceVariant = DeSurfaceElev,
    onSurfaceVariant = DeOnBackgroundDim,
    outline = DeOutline,
)

// Light scheme is intentionally minimal — both reference apps ship dark-first.
// Kept here so system theme switches don't break colours, but visual design
// is tuned for dark only (see screenshots in screens/).
private val LightColors = lightColorScheme(
    primary = DeOrange,
    onPrimary = Color.White,
    secondary = DeYellow,
    onSecondary = Color.Black,
)

@Composable
fun DeMuzykaTheme(
    forceDark: Boolean = true,
    content: @Composable () -> Unit,
) {
    val dark = if (forceDark) true else isSystemInDarkTheme()
    MaterialTheme(
        colorScheme = if (dark) DarkColors else LightColors,
        typography = DeTypography,
        content = content,
    )
}
