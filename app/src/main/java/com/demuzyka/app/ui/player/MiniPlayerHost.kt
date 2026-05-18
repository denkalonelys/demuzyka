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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.Pause
import androidx.compose.material.icons.outlined.PlayArrow
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
import androidx.compose.ui.unit.dp
import com.demuzyka.app.data.AppContainer
import com.demuzyka.app.data.music.StubMusicProvider

/**
 * Mini-player ribbon — sits inline above the bottom navigation bar (like
 * Yandex.Music). Hidden when nothing is playing. Tap opens the full-screen
 * player sheet.
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
        enter = slideInVertically(tween(280)) { it } + fadeIn(tween(220)),
        exit = slideOutVertically(tween(220)) { it } + fadeOut(tween(180)),
        modifier = modifier,
    ) {
        val playing = now ?: return@AnimatedVisibility
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 6.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(Color(0xFF1A1A1A))
                .clickable(onClick = onExpand)
                .padding(horizontal = 8.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(6.dp))
                    .background(
                        Brush.linearGradient(
                            listOf(Color(0xFFFF6A00), Color(0xFFB200B2))
                        )
                    ),
            )
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 10.dp),
            ) {
                Text(
                    playing.track.title,
                    style = MaterialTheme.typography.titleSmall,
                    color = Color.White,
                    maxLines = 1,
                )
                Text(
                    playing.track.artist,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.White.copy(alpha = 0.6f),
                    maxLines = 1,
                )
            }
            IconButton(onClick = { /* TODO: like */ }) {
                Icon(
                    Icons.Outlined.Favorite,
                    null,
                    modifier = Modifier.size(20.dp),
                    tint = Color.White.copy(alpha = 0.8f),
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
    }
}
