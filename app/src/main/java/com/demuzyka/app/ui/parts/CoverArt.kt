package com.demuzyka.app.ui.parts

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.demuzyka.app.ui.theme.coverPaletteFor

/**
 * A pretty placeholder cover.  Generates a 2-blob mesh-ish gradient from
 * a stable id so adjacent tiles all look distinct without bundling any
 * actual artwork.  Drop in `coverUrl != null` once the provider returns
 * real URLs and replace this with AsyncImage.
 */
@Composable
fun CoverArt(
    id: String?,
    modifier: Modifier = Modifier,
    corner: Dp = 12.dp,
) {
    val p = coverPaletteFor(id)
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(corner))
            .background(
                Brush.linearGradient(
                    listOf(p.start, p.mid, p.end),
                    start = Offset(0f, 0f),
                    end = Offset.Infinite,
                )
            ),
    ) {
        // Highlight blob in the top-left to give the gradient some depth.
        Canvas(modifier = Modifier.fillMaxSize()) {
            val r = size.minDimension * 0.55f
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(
                        lerp(p.start, Color.White, 0.25f).copy(alpha = 0.55f),
                        Color.Transparent,
                    ),
                    center = Offset(size.width * 0.25f, size.height * 0.3f),
                    radius = r,
                ),
                radius = r,
                center = Offset(size.width * 0.25f, size.height * 0.3f),
            )
            // Deep shadow in the bottom-right for depth.
            val r2 = size.minDimension * 0.6f
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(
                        p.edge.copy(alpha = 0.55f),
                        Color.Transparent,
                    ),
                    center = Offset(size.width * 0.85f, size.height * 0.85f),
                    radius = r2,
                ),
                radius = r2,
                center = Offset(size.width * 0.85f, size.height * 0.85f),
            )
        }
    }
}
