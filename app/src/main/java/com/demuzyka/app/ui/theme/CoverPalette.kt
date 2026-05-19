package com.demuzyka.app.ui.theme

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.lerp

/**
 * Procedural cover colours.  Given any stable id (track id, playlist id,
 * mood id, film id) we deterministically derive a 3-stop gradient so
 * placeholder covers don't all look the same — without bundling artwork.
 *
 * When a real `coverUrl` is supplied by your MusicProvider, just stop
 * using this and feed `AsyncImage` from Coil.
 */
data class CoverGradient(
    val start: Color,
    val mid: Color,
    val end: Color,
) {
    val edge: Color get() = lerp(end, Color.Black, 0.5f)
}

private val PALETTES = listOf(
    CoverGradient(DePink, DeMagenta, Color(0xFF1A0033)),
    CoverGradient(DeOrange, DePink, Color(0xFF1A0014)),
    CoverGradient(DeYellow, DeOrange, Color(0xFF1A0700)),
    CoverGradient(DeIndigo, DeMagenta, Color(0xFF080020)),
    CoverGradient(DeBlue, DeIndigo, Color(0xFF000820)),
    CoverGradient(DeCyan, DeBlue, Color(0xFF001820)),
    CoverGradient(DeGreen, DeCyan, Color(0xFF001A14)),
    CoverGradient(DeRed, DeOrange, Color(0xFF200300)),
    CoverGradient(DePink, DeOrange, Color(0xFF1F0010)),
    CoverGradient(DeYellow, DeRed, Color(0xFF1F0006)),
    CoverGradient(DeMagenta, DeIndigo, Color(0xFF10001F)),
    CoverGradient(DeOrange, DeRed, Color(0xFF200006)),
)

/** Stable palette pick: same id → same gradient, every cold start. */
fun coverPaletteFor(id: String?): CoverGradient {
    if (id.isNullOrEmpty()) return PALETTES[0]
    // FNV-1a-ish: deterministic across JVM versions.
    var h = 2166136261u.toInt()
    for (c in id) {
        h = h xor c.code
        h *= 16777619.toInt()
    }
    val idx = (h.toUInt() % PALETTES.size.toUInt()).toInt()
    return PALETTES[idx]
}
