package com.demuzyka.app.ui.theme

import androidx.compose.ui.graphics.Color

/**
 * Brand palette — Kinopoisk-flavoured orange for "Поиск", Yandex.Music
 * yellow for the play affordance, plus a saturated magenta/pink that
 * shows up on the "Моя волна" hero and the player backdrop.
 *
 * Everything else is true black so the OLED look of Y.Music holds up.
 */

// — Brand accents —
internal val DeOrange = Color(0xFFFF6A00)
internal val DeOrangeDim = Color(0xFFB54B00)
internal val DeYellow = Color(0xFFFFCC00)
internal val DeYellowDim = Color(0xFFB89200)
internal val DePink = Color(0xFFFF2D7F)
internal val DeMagenta = Color(0xFFB200B2)

// — Cover-palette anchors — used by CoverPalette.kt to derive a stable
// gradient from a track / album id so placeholder covers don't all look
// the same.
internal val DeIndigo = Color(0xFF5B33FF)
internal val DeBlue = Color(0xFF1F5BFF)
internal val DeCyan = Color(0xFF27B4A2)
internal val DeGreen = Color(0xFF1FB559)
internal val DeRed = Color(0xFFFF3B57)

// — Surfaces — pure black like Y.Music's OLED palette.
internal val DeBlack = Color(0xFF000000)
internal val DeNearBlack = Color(0xFF000000)
internal val DeSurface = Color(0xFF0A0A0A)
internal val DeSurfaceElev = Color(0xFF121212)
internal val DeSurfaceHi = Color(0xFF1A1A1A)
internal val DeOutline = Color(0xFF1F1F1F)

// — Text —
internal val DeOnBackground = Color(0xFFFFFFFF)
internal val DeOnBackgroundDim = Color(0xFFAAAAAE)
internal val DeOnBackgroundFaint = Color(0xFF6B6B70)

// — Rating chip backgrounds used on poster tiles (Kinopoisk-style).
internal val DeRatingGood = Color(0xFF3BB45F)    // 7.0+
internal val DeRatingMid = Color(0xFF7C7C7E)     // <7
