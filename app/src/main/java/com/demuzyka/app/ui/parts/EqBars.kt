package com.demuzyka.app.ui.parts

import androidx.compose.animation.core.EaseInOutSine
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Three-bar audio EQ indicator.  When `playing` is true, bars wobble to
 * imply playback; when false they hold at min height.  Shown on top of
 * the now-playing track tile and the mini-player cover (top-left badge).
 */
@Composable
fun EqBars(
    playing: Boolean,
    modifier: Modifier = Modifier,
    tint: Color = Color.White,
    barWidth: Dp = 2.5.dp,
    height: Dp = 12.dp,
    spacing: Dp = 1.5.dp,
) {
    val tx = rememberInfiniteTransition(label = "eq")
    val a by tx.animateFloat(
        initialValue = 0.25f, targetValue = 1f,
        animationSpec = infiniteRepeatable(
            tween(560, easing = EaseInOutSine),
            repeatMode = RepeatMode.Reverse,
        ),
        label = "eq-a",
    )
    val b by tx.animateFloat(
        initialValue = 0.55f, targetValue = 0.15f,
        animationSpec = infiniteRepeatable(
            tween(720, easing = EaseInOutSine),
            repeatMode = RepeatMode.Reverse,
        ),
        label = "eq-b",
    )
    val c by tx.animateFloat(
        initialValue = 0.2f, targetValue = 0.9f,
        animationSpec = infiniteRepeatable(
            tween(620, easing = EaseInOutSine),
            repeatMode = RepeatMode.Reverse,
        ),
        label = "eq-c",
    )

    Canvas(modifier = modifier.size(width = (barWidth.value * 3 + spacing.value * 2).dp, height = height)) {
        val bw = barWidth.toPx()
        val gap = spacing.toPx()
        val totalW = bw * 3 + gap * 2
        val startX = (size.width - totalW) / 2f
        val frac = if (playing) listOf(a, b, c) else listOf(0.15f, 0.15f, 0.15f)
        frac.forEachIndexed { i, f ->
            val x = startX + i * (bw + gap)
            val barH = size.height * f.coerceIn(0.1f, 1f)
            val y = size.height - barH
            drawRect(
                color = tint,
                topLeft = Offset(x, y),
                size = Size(bw, barH),
            )
        }
    }
}
