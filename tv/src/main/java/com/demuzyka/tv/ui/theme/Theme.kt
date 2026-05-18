package com.demuzyka.tv.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.darkColorScheme

private val Dark = darkColorScheme(
    primary = Color(0xFFFF6A00),
    onPrimary = Color.White,
    secondary = Color(0xFFFFCC00),
    background = Color.Black,
    onBackground = Color.White,
    surface = Color(0xFF141416),
    onSurface = Color.White,
    surfaceVariant = Color(0xFF1B1B1F),
)

@Composable
fun DeMuzykaTvTheme(content: @Composable () -> Unit) {
    MaterialTheme(colorScheme = Dark, content = content)
}
