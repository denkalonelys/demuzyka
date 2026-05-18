package com.demuzyka.app.ui.theme

import androidx.compose.ui.graphics.Color

/**
 * Brand palette — close to Kinopoisk's orange accent and Yandex Music's
 * yellow secondary, but renamed so we don't infringe.
 */
internal val DeOrange = Color(0xFFFF6A00)        // Kinopoisk-flavoured primary
internal val DeOrangeDim = Color(0xFFB54B00)
internal val DeYellow = Color(0xFFFFCC00)        // Yandex Music play button
internal val DeYellowDim = Color(0xFFB89200)
internal val DePink = Color(0xFFFF2D7F)          // wave + concerts banner
internal val DeMagenta = Color(0xFFB200B2)

// Pure-black palette matching Yandex.Music / Kinopoisk reference designs.
internal val DeBlack = Color(0xFF000000)
internal val DeNearBlack = Color(0xFF000000)
internal val DeSurface = Color(0xFF0A0A0A)
internal val DeSurfaceElev = Color(0xFF121212)
internal val DeOutline = Color(0xFF1F1F1F)

internal val DeOnBackground = Color(0xFFFFFFFF)
internal val DeOnBackgroundDim = Color(0xFFAAAAAE)
internal val DeOnBackgroundFaint = Color(0xFF6B6B70)

// Rating chip backgrounds used on poster tiles (Kinopoisk-style).
internal val DeRatingGood = Color(0xFF3BB45F)    // 7.0+
internal val DeRatingMid = Color(0xFF7C7C7E)     // <7
