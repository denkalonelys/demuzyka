package com.demuzyka.tv.ui.parts

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

/** Same multi-orbit mesh background as :app/parts/MeshHero.kt — duplicated
 *  here to keep :tv independent of :app (no shared :data library yet). */
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
            drawRect(Color.Black)
            val w = size.width
            val h = size.height
            val r = size.minDimension

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
