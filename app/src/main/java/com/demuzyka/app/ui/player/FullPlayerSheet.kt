package com.demuzyka.app.ui.player

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.KeyboardArrowDown
import androidx.compose.material.icons.outlined.MoreHoriz
import androidx.compose.material.icons.outlined.Pause
import androidx.compose.material.icons.outlined.PlayArrow
import androidx.compose.material.icons.outlined.Shuffle
import androidx.compose.material.icons.outlined.SkipNext
import androidx.compose.material.icons.outlined.SkipPrevious
import androidx.compose.material.icons.outlined.ThumbDown
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.demuzyka.app.data.AppContainer
import com.demuzyka.app.data.music.StubMusicProvider

/**
 * Full-screen now-playing sheet — slides up from the mini-player.
 * Pure black background, oversized cover with breathing radial glow,
 * play / next / prev / like / dislike / shuffle / close buttons.
 */
@Composable
fun FullPlayerSheet(container: AppContainer, onDismiss: () -> Unit) {
    val now by container.musicProvider.nowPlaying.collectAsState()
    val playing = now ?: run {
        onDismiss(); return
    }

    val stub = container.musicProvider as? StubMusicProvider
    val transition = rememberInfiniteTransition(label = "player-bg")
    val radius by transition.animateFloat(
        initialValue = 700f, targetValue = 1300f,
        animationSpec = infiniteRepeatable(tween(4500, easing = LinearEasing), RepeatMode.Reverse),
        label = "player-bg-radius",
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .background(
                Brush.radialGradient(
                    colors = listOf(
                        lerp(Color(0xFFFF2D7F), Color.Black, 0.55f),
                        Color.Black,
                    ),
                    radius = radius,
                )
            ),
    ) {
        val topInset = WindowInsets.statusBars.asPaddingValues().calculateTopPadding()
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = topInset),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            // Top bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                IconButton(onClick = onDismiss) {
                    Icon(Icons.Outlined.KeyboardArrowDown, null, modifier = Modifier.size(28.dp))
                }
                Column(
                    modifier = Modifier.weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Text("ВОЛНА", style = MaterialTheme.typography.labelMedium, color = Color.White.copy(alpha = 0.65f))
                    Text(playing.source.uppercase(), style = MaterialTheme.typography.bodySmall, color = Color.White.copy(alpha = 0.6f))
                }
                IconButton(onClick = { /* TODO: menu */ }) {
                    Icon(Icons.Outlined.MoreHoriz, null, modifier = Modifier.size(24.dp))
                }
            }

            // Cover artwork
            Box(
                modifier = Modifier
                    .padding(horizontal = 36.dp, vertical = 24.dp)
                    .fillMaxWidth()
                    .aspectRatio(1f)
                    .clip(RoundedCornerShape(20.dp))
                    .background(
                        Brush.linearGradient(
                            listOf(Color(0xFFFF6A00), Color(0xFFB200B2), Color(0xFFFF2D7F))
                        )
                    ),
            )

            // Title + artist
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    playing.track.title,
                    style = MaterialTheme.typography.headlineSmall,
                    color = Color.White,
                    textAlign = TextAlign.Center,
                    maxLines = 2,
                )
                Text(
                    playing.track.artist,
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.White.copy(alpha = 0.7f),
                    modifier = Modifier.padding(top = 6.dp),
                )
            }

            // Progress
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp, vertical = 24.dp)
                    .height(3.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(Color.White.copy(alpha = 0.15f)),
                contentAlignment = Alignment.CenterStart,
            ) {
                val pct = if (playing.track.durationSec > 0)
                    (playing.positionSec.toFloat() / playing.track.durationSec).coerceIn(0f, 1f) else 0.2f
                Box(
                    modifier = Modifier
                        .fillMaxWidth(pct)
                        .height(3.dp)
                        .background(Color.White)
                )
            }

            // Transport
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                IconButton(onClick = { /* TODO: dislike */ }) {
                    Icon(Icons.Outlined.ThumbDown, null, tint = Color.White, modifier = Modifier.size(28.dp))
                }
                IconButton(onClick = { /* TODO: prev */ }) {
                    Icon(Icons.Outlined.SkipPrevious, null, tint = Color.White, modifier = Modifier.size(40.dp))
                }
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .clip(CircleShape)
                        .background(Color.White)
                        .clickable { stub?.toggleNow() },
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(
                        if (playing.isPlaying) Icons.Outlined.Pause else Icons.Outlined.PlayArrow,
                        null,
                        tint = Color.Black,
                        modifier = Modifier.size(40.dp),
                    )
                }
                IconButton(onClick = { /* TODO: next */ }) {
                    Icon(Icons.Outlined.SkipNext, null, tint = Color.White, modifier = Modifier.size(40.dp))
                }
                IconButton(onClick = { /* TODO: like */ }) {
                    Icon(Icons.Outlined.Favorite, null, tint = Color.White, modifier = Modifier.size(28.dp))
                }
            }

            Box(modifier = Modifier.weight(1f))

            // Bottom shuffle + share row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp, vertical = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                IconButton(onClick = { /* TODO: shuffle */ }) {
                    Icon(Icons.Outlined.Shuffle, null, tint = Color.White.copy(alpha = 0.7f))
                }
                Text(
                    "Слушаете в ДеМузыке",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.White.copy(alpha = 0.4f),
                )
                Box(modifier = Modifier.size(24.dp))
            }
        }
    }
}
