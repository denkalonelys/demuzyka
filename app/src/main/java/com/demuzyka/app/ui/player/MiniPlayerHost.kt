package com.demuzyka.app.ui.player

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Pause
import androidx.compose.material.icons.outlined.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.demuzyka.app.data.AppContainer
import com.demuzyka.app.data.music.StubMusicProvider
import com.demuzyka.app.ui.parts.CoverArt
import com.demuzyka.app.ui.theme.coverPaletteFor

/**
 * Mini-player ribbon — sits inline above the bottom navigation bar.
 * Hidden when nothing is playing.  Tap opens the full-screen sheet.
 *
 * Background uses the current track's procedural cover palette so the
 * ribbon feels glued to the now-playing artwork.
 */
@Composable
fun MiniPlayerHost(
    container: AppContainer,
    onExpand: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val now by container.musicProvider.nowPlaying.collectAsState()
    val stub = container.musicProvider as? StubMusicProvider

    AnimatedVisibility(
        visible = now != null,
        enter = slideInVertically(tween(320)) { it } + fadeIn(tween(220)),
        exit = slideOutVertically(tween(220)) { it } + fadeOut(tween(180)),
        modifier = modifier,
    ) {
        val playing = now ?: return@AnimatedVisibility
        val p = coverPaletteFor(playing.track.id)
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 6.dp)
                .clip(RoundedCornerShape(14.dp))
                .background(
                    Brush.horizontalGradient(
                        listOf(
                            p.edge.copy(alpha = 0.95f),
                            Color(0xFF1A1A1A),
                        )
                    )
                )
                .clickable(onClick = onExpand)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                CoverArt(id = playing.track.id, modifier = Modifier.size(42.dp), corner = 8.dp)
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 10.dp),
                ) {
                    Text(
                        playing.track.title,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                    Text(
                        playing.track.artist,
                        fontSize = 12.sp,
                        color = Color.White.copy(alpha = 0.65f),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
                IconButton(onClick = { /* TODO: like */ }) {
                    Icon(
                        Icons.Outlined.FavoriteBorder,
                        null,
                        modifier = Modifier.size(20.dp),
                        tint = Color.White.copy(alpha = 0.85f),
                    )
                }
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(Color.White)
                        .clickable { stub?.toggleNow() },
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(
                        if (playing.isPlaying) Icons.Outlined.Pause else Icons.Outlined.PlayArrow,
                        null,
                        tint = Color.Black,
                    )
                }
            }
            // Thin progress strip at the bottom — animates with positionSec.
            val pct = if (playing.track.durationSec > 0) {
                (playing.positionSec.toFloat() / playing.track.durationSec).coerceIn(0f, 1f)
            } else 0.25f
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(2.dp)
                    .background(Color.White.copy(alpha = 0.08f)),
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(pct)
                        .height(2.dp)
                        .background(Color.White.copy(alpha = 0.85f)),
                )
            }
        }
    }
}
