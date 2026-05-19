package com.demuzyka.app.ui.parts

import androidx.compose.animation.core.EaseInOutSine
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import kotlin.math.cos
import kotlin.math.sin

/**
 * Animated mesh-gradient background.  Three radial-blob layers drift
 * around different orbits at different periods so the surface keeps
 * "breathing" without ever repeating obviously.  Used as the background
 * of:
 *   • the "Моя волна" hero on the music home screen
 *   • the full-screen now-playing sheet (cover-tinted variant)
 *
 * Pass your own gradient anchors for cover-tinted backdrops:
 * `MeshHero(stops = listOf(p.start, p.mid, Color.Black))`.
 */
@Composable
fun MeshHero(
    modifier: Modifier = Modifier,
    a: Color,
    b: Color,
    c: Color,
    content: @Composable () -> Unit = {},
) {
    val tx = rememberInfiniteTransition(label = "mesh")
    val t1 by tx.animateFloat(
        initialValue = 0f, targetValue = 1f,
        animationSpec = infiniteRepeatable(tween(9000, easing = LinearEasing)),
        label = "mesh-t1",
    )
    val t2 by tx.animateFloat(
        initialValue = 0f, targetValue = 1f,
        animationSpec = infiniteRepeatable(tween(13000, easing = LinearEasing)),
        label = "mesh-t2",
    )
    val pulse by tx.animateFloat(
        initialValue = 0.85f, targetValue = 1.15f,
        animationSpec = infiniteRepeatable(
            tween(4500, easing = EaseInOutSine),
            repeatMode = RepeatMode.Reverse,
        ),
        label = "mesh-pulse",
    )

    Box(modifier = modifier) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            // Base wash so edges fade into black even before the blobs.
            drawRect(Color.Black)

            val w = size.width
            val h = size.height
            val r = size.minDimension

            // Blob A — slow orbit
            val ax = w * (0.5f + 0.32f * cos(t1 * 2f * Math.PI.toFloat()))
            val ay = h * (0.5f + 0.30f * sin(t1 * 2f * Math.PI.toFloat()))
            val ar = r * 0.95f * pulse
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(a.copy(alpha = 0.95f), Color.Transparent),
                    center = Offset(ax, ay),
                    radius = ar,
                ),
                radius = ar,
                center = Offset(ax, ay),
            )

            // Blob B — opposite orbit, slower
            val bx = w * (0.5f - 0.34f * cos(t2 * 2f * Math.PI.toFloat() + 1.1f))
            val by_ = h * (0.5f - 0.28f * sin(t2 * 2f * Math.PI.toFloat() + 1.1f))
            val br = r * 0.85f * (2.0f - pulse)
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(b.copy(alpha = 0.85f), Color.Transparent),
                    center = Offset(bx, by_),
                    radius = br,
                ),
                radius = br,
                center = Offset(bx, by_),
            )

            // Blob C — small fast bright accent
            val cx = w * (0.5f + 0.18f * cos(t1 * 2f * Math.PI.toFloat() * 2.3f + 0.4f))
            val cy = h * (0.5f + 0.22f * sin(t2 * 2f * Math.PI.toFloat() * 1.7f + 2.0f))
            val cr = r * 0.45f
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(c.copy(alpha = 0.7f), Color.Transparent),
                    center = Offset(cx, cy),
                    radius = cr,
                ),
                radius = cr,
                center = Offset(cx, cy),
            )

            // Vignette so the hero never blows the OLED black around it.
            drawRect(
                brush = Brush.radialGradient(
                    colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.45f)),
                    center = Offset(w / 2f, h / 2f),
                    radius = r * 0.9f,
                ),
            )
        }
        content()
    }
}
