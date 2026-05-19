package com.demuzyka.tv.ui.parts

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.lerp

/**
 * Procedural cover colours.  Given any stable id we deterministically
 * derive a 3-stop gradient so placeholder covers don't all look the same.
 */
data class CoverGradient(
    val start: Color,
    val mid: Color,
    val end: Color,
) {
    val edge: Color get() = lerp(end, Color.Black, 0.5f)
}

private val PINK = Color(0xFFFF2D7F)
private val MAGENTA = Color(0xFFB200B2)
private val ORANGE = Color(0xFFFF6A00)
private val YELLOW = Color(0xFFFFCC00)
private val INDIGO = Color(0xFF5B33FF)
private val BLUE = Color(0xFF1F5BFF)
private val CYAN = Color(0xFF27B4A2)
private val GREEN = Color(0xFF1FB559)
private val RED = Color(0xFFFF3B57)

private val PALETTES = listOf(
    CoverGradient(PINK, MAGENTA, Color(0xFF1A0033)),
    CoverGradient(ORANGE, PINK, Color(0xFF1A0014)),
    CoverGradient(YELLOW, ORANGE, Color(0xFF1A0700)),
    CoverGradient(INDIGO, MAGENTA, Color(0xFF080020)),
    CoverGradient(BLUE, INDIGO, Color(0xFF000820)),
    CoverGradient(CYAN, BLUE, Color(0xFF001820)),
    CoverGradient(GREEN, CYAN, Color(0xFF001A14)),
    CoverGradient(RED, ORANGE, Color(0xFF200300)),
    CoverGradient(PINK, ORANGE, Color(0xFF1F0010)),
    CoverGradient(YELLOW, RED, Color(0xFF1F0006)),
    CoverGradient(MAGENTA, INDIGO, Color(0xFF10001F)),
    CoverGradient(ORANGE, RED, Color(0xFF200006)),
)

fun coverPaletteFor(id: String?): CoverGradient {
    if (id.isNullOrEmpty()) return PALETTES[0]
    var h = 2166136261u.toInt()
    for (c in id) {
        h = h xor c.code
        h *= 16777619.toInt()
    }
    val idx = (h.toUInt() % PALETTES.size.toUInt()).toInt()
    return PALETTES[idx]
}
