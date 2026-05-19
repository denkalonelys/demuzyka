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
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.KeyboardArrowDown
import androidx.compose.material.icons.outlined.MoreHoriz
import androidx.compose.material.icons.outlined.Pause
import androidx.compose.material.icons.outlined.PlayArrow
import androidx.compose.material.icons.outlined.QueueMusic
import androidx.compose.material.icons.outlined.Repeat
import androidx.compose.material.icons.outlined.Shuffle
import androidx.compose.material.icons.outlined.SkipNext
import androidx.compose.material.icons.outlined.SkipPrevious
import androidx.compose.material.icons.outlined.ThumbDownAlt
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
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.demuzyka.app.data.AppContainer
import com.demuzyka.app.data.music.StubMusicProvider
import com.demuzyka.app.ui.parts.CoverArt
import com.demuzyka.app.ui.parts.MeshHero
import com.demuzyka.app.ui.theme.coverPaletteFor

/**
 * Full-screen now-playing sheet — slides up from the mini-player.
 *
 * Visual layers (back→front):
 *   • pure black wash
 *   • animated mesh-gradient backdrop tinted by the current track's
 *     procedural cover palette
 *   • huge square cover
 *   • title / artist / progress bar
 *   • transport (prev / play-pause / next) + secondary row (shuffle,
 *     dislike, like, repeat) + bottom (queue, source label, more).
 */
@Composable
fun FullPlayerSheet(container: AppContainer, onDismiss: () -> Unit) {
    val now by container.musicProvider.nowPlaying.collectAsState()
    val playing = now ?: run {
        onDismiss(); return
    }

    val stub = container.musicProvider as? StubMusicProvider
    val palette = coverPaletteFor(playing.track.id)

    // Subtle cover scale on play/pause.
    val tx = rememberInfiniteTransition(label = "fp-bg")
    val pulse by tx.animateFloat(
        initialValue = 0.97f, targetValue = 1.03f,
        animationSpec = infiniteRepeatable(
            tween(2400, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse,
        ),
        label = "fp-cover-pulse",
    )

    Box(modifier = Modifier.fillMaxSize().background(Color.Black)) {
        // Backdrop — soft cover-tinted mesh that fills the whole sheet.
        MeshHero(
            modifier = Modifier.fillMaxSize(),
            a = palette.start.copy(alpha = 0.85f),
            b = palette.mid.copy(alpha = 0.7f),
            c = palette.end.copy(alpha = 0.5f),
        )

        val topInset = WindowInsets.statusBars.asPaddingValues().calculateTopPadding()
        val bottomInset = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = topInset, bottom = bottomInset),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            // Top bar — close + source + more.
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                IconButton(onClick = onDismiss) {
                    Icon(
                        Icons.Outlined.KeyboardArrowDown,
                        null,
                        modifier = Modifier.size(30.dp),
                        tint = Color.White,
                    )
                }
                Column(
                    modifier = Modifier.weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Text(
                        "СЕЙЧАС ИГРАЕТ",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White.copy(alpha = 0.65f),
                        letterSpacing = 1.5.sp,
                    )
                    Text(
                        playing.source.uppercase(),
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White,
                    )
                }
                IconButton(onClick = { /* TODO: menu */ }) {
                    Icon(
                        Icons.Outlined.MoreHoriz,
                        null,
                        modifier = Modifier.size(24.dp),
                        tint = Color.White,
                    )
                }
            }

            Spacer(Modifier.height(8.dp))

            // Cover artwork — huge, with subtle scale pulse while playing.
            Box(
                modifier = Modifier
                    .padding(horizontal = 32.dp)
                    .fillMaxWidth()
                    .aspectRatio(1f),
                contentAlignment = Alignment.Center,
            ) {
                val scale = if (playing.isPlaying) pulse else 1f
                CoverArt(
                    id = playing.track.id,
                    modifier = Modifier
                        .fillMaxSize()
                        .graphicsLayer { scaleX = scale; scaleY = scale },
                    corner = 24.dp,
                )
            }

            Spacer(Modifier.height(28.dp))

            // Title + artist
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 36.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    playing.track.title,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Black,
                    letterSpacing = (-0.5).sp,
                    color = Color.White,
                    textAlign = TextAlign.Center,
                    maxLines = 2,
                )
                Text(
                    playing.track.artist,
                    fontSize = 15.sp,
                    color = Color.White.copy(alpha = 0.7f),
                    modifier = Modifier.padding(top = 6.dp),
                )
            }

            Spacer(Modifier.height(20.dp))

            // Progress track + time labels
            Column(
                modifier = Modifier.padding(horizontal = 36.dp).fillMaxWidth(),
            ) {
                ProgressRail(
                    positionSec = playing.positionSec,
                    durationSec = playing.track.durationSec,
                )
                Spacer(Modifier.height(6.dp))
                Row(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        formatTime(playing.positionSec),
                        fontSize = 11.sp,
                        color = Color.White.copy(alpha = 0.6f),
                        modifier = Modifier.weight(1f),
                    )
                    Text(
                        "-${formatTime((playing.track.durationSec - playing.positionSec).coerceAtLeast(0))}",
                        fontSize = 11.sp,
                        color = Color.White.copy(alpha = 0.6f),
                    )
                }
            }

            Spacer(Modifier.height(20.dp))

            // Transport — prev / big play / next.
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly,
            ) {
                CircleIcon(Icons.Outlined.ThumbDownAlt, size = 26.dp) { /* TODO: dislike */ }
                CircleIcon(Icons.Outlined.SkipPrevious, size = 38.dp) { /* TODO: prev */ }
                Box(
                    modifier = Modifier
                        .size(78.dp)
                        .clip(CircleShape)
                        .background(Color.White)
                        .clickable { stub?.toggleNow() },
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(
                        if (playing.isPlaying) Icons.Outlined.Pause else Icons.Outlined.PlayArrow,
                        null,
                        tint = Color.Black,
                        modifier = Modifier.size(42.dp),
                    )
                }
                CircleIcon(Icons.Outlined.SkipNext, size = 38.dp) { /* TODO: next */ }
                CircleIcon(Icons.Outlined.FavoriteBorder, size = 26.dp) { /* TODO: like */ }
            }

            Spacer(Modifier.weight(1f))

            // Secondary row — shuffle, queue, source.
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                CircleIcon(Icons.Outlined.Shuffle, size = 22.dp, alpha = 0.75f) { /* TODO: shuffle */ }
                Text(
                    "Слушаете в ДеМузыке",
                    fontSize = 11.sp,
                    color = Color.White.copy(alpha = 0.5f),
                )
                CircleIcon(Icons.Outlined.QueueMusic, size = 22.dp, alpha = 0.75f) { /* TODO: queue */ }
            }
        }
    }
}

@Composable
private fun ProgressRail(positionSec: Int, durationSec: Int) {
    val pct = if (durationSec > 0)
        (positionSec.toFloat() / durationSec).coerceIn(0f, 1f)
    else 0.15f
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(3.dp)
            .clip(RoundedCornerShape(2.dp))
            .background(Color.White.copy(alpha = 0.15f)),
        contentAlignment = Alignment.CenterStart,
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(pct)
                .height(3.dp)
                .clip(RoundedCornerShape(2.dp))
                .background(Color.White),
        )
    }
}

@Composable
private fun CircleIcon(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    size: androidx.compose.ui.unit.Dp,
    alpha: Float = 1f,
    onClick: () -> Unit,
) {
    IconButton(onClick = onClick) {
        Icon(icon, null, tint = Color.White.copy(alpha = alpha), modifier = Modifier.size(size))
    }
}

private fun formatTime(sec: Int): String {
    val s = sec.coerceAtLeast(0)
    return "%d:%02d".format(s / 60, s % 60)
}
