package com.demuzyka.app.ui.music

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
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.PlayArrow
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.IconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.dp
import com.demuzyka.app.data.AppContainer
import com.demuzyka.app.data.music.HomeRow
import com.demuzyka.app.ui.theme.DeMagenta
import com.demuzyka.app.ui.theme.DePink
import com.demuzyka.app.ui.theme.DeYellow
import kotlinx.coroutines.flow.flowOf

/**
 * Music home — replicates Yandex.Music's layout:
 *  1. Big "Моя волна" hero with a gradient blob.
 *  2. Mood chips.
 *  3. Horizontal rows of playlists / tracks / books.
 */
@Composable
fun MusicHomeScreen(container: AppContainer) {
    val rows by remember { container.musicProvider.homeRows() }.collectAsState(initial = emptyList())
    val provider = container.musicProvider as? com.demuzyka.app.data.music.StubMusicProvider
    val onPlayTrack: (com.demuzyka.app.data.music.Track) -> Unit = { track ->
        provider?.play(track)
    }
    val onPlayMood: () -> Unit = {
        val firstTrack = rows.firstNotNullOfOrNull { row ->
            (row.items.firstOrNull { it is HomeRow.Item.TrackItem } as? HomeRow.Item.TrackItem)?.track
        } ?: com.demuzyka.app.data.music.Track("wave", "Моя волна", "Радио", null, 0)
        provider?.play(firstTrack)
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 120.dp),
    ) {
        item { HomeHeader() }
        item { WaveHero(onClick = onPlayMood) }
        items(rows) { row -> HomeRowSection(row, onPlayTrack = onPlayTrack) }
    }
}

@Composable
private fun HomeHeader() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        // Plus logo placeholder.
        Box(
            modifier = Modifier
                .size(32.dp)
                .clip(CircleShape)
                .background(
                    Brush.linearGradient(
                        listOf(DePink, DeMagenta),
                    )
                ),
        )
        Text(
            text = "ДеМузыка",
            modifier = Modifier
                .padding(start = 12.dp)
                .weight(1f),
            style = MaterialTheme.typography.titleLarge,
        )
        IconButton(onClick = { /* TODO: open search */ }) {
            Icon(Icons.Outlined.Search, contentDescription = "Поиск")
        }
    }
}

/** Gradient "wave" blob with a centred play affordance — Y.Music's hero.
 *  Radius slowly pulses to give the blob a "breathing" feel. */
@Composable
private fun WaveHero(onClick: () -> Unit) {
    val transition = rememberInfiniteTransition(label = "wave-pulse")
    val radius by transition.animateFloat(
        initialValue = 900f,
        targetValue = 1400f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 4500, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse,
        ),
        label = "wave-radius",
    )
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp)
            .aspectRatio(0.95f)
            .clip(RoundedCornerShape(24.dp))
            .clickable(onClick = onClick)
            .background(
                Brush.radialGradient(
                    colors = listOf(
                        DeYellow,
                        DePink,
                        MaterialTheme.colorScheme.background,
                    ),
                    radius = radius,
                )
            ),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(20.dp),
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    Icons.Outlined.PlayArrow,
                    contentDescription = null,
                    modifier = Modifier.size(40.dp),
                )
                Text(
                    "Моя волна",
                    style = MaterialTheme.typography.displayLarge,
                )
            }
            Text(
                "Слушайте треки под ваше настроение",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
            )
        }
    }
}

@Composable
private fun HomeRowSection(row: HomeRow, onPlayTrack: (com.demuzyka.app.data.music.Track) -> Unit) {
    Column(modifier = Modifier.fillMaxWidth().padding(top = 24.dp)) {
        Text(
            row.title,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            style = MaterialTheme.typography.headlineSmall,
        )
        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            items(row.items) { item ->
                when (item) {
                    is HomeRow.Item.MoodItem -> MoodCard(item)
                    is HomeRow.Item.PlaylistItem -> PlaylistCard(item)
                    is HomeRow.Item.TrackItem -> TrackTile(item, onClick = { onPlayTrack(item.track) })
                    is HomeRow.Item.BookItem -> BookCard(item)
                }
            }
        }
    }
}

@Composable
private fun MoodCard(item: HomeRow.Item.MoodItem) {
    Box(
        modifier = Modifier
            .size(160.dp)
            .clip(RoundedCornerShape(20.dp))
            .clickable { /* TODO: open mood */ }
            .background(
                Brush.linearGradient(
                    when (item.moodId) {
                        "chill" -> listOf(DeMagenta, MaterialTheme.colorScheme.background)
                        "workout" -> listOf(DePink, DeYellow)
                        "focus" -> listOf(MaterialTheme.colorScheme.primary, MaterialTheme.colorScheme.background)
                        "party" -> listOf(DeYellow, DePink)
                        else -> listOf(DePink, DeYellow)
                    }
                )
            ),
        contentAlignment = Alignment.BottomStart,
    ) {
        Text(
            item.title,
            modifier = Modifier.padding(12.dp),
            style = MaterialTheme.typography.titleLarge,
        )
    }
}

@Composable
private fun PlaylistCard(item: HomeRow.Item.PlaylistItem) {
    Column(
        modifier = Modifier
            .width(160.dp)
            .clip(RoundedCornerShape(16.dp))
            .clickable { /* TODO: open playlist */ }
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
                .clip(RoundedCornerShape(16.dp))
                .background(MaterialTheme.colorScheme.surfaceVariant),
        )
        Text(
            item.playlist.title,
            modifier = Modifier.padding(top = 8.dp),
            style = MaterialTheme.typography.titleMedium,
            maxLines = 2,
        )
    }
}

@Composable
private fun TrackTile(item: HomeRow.Item.TrackItem, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .width(280.dp)
            .clip(RoundedCornerShape(10.dp))
            .clickable(onClick = onClick),
    ) {
        Box(
            modifier = Modifier
                .size(56.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(MaterialTheme.colorScheme.surfaceVariant),
        )
        Column(modifier = Modifier.padding(start = 12.dp)) {
            Text(item.track.title, style = MaterialTheme.typography.titleMedium)
            Text(
                item.track.artist,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

@Composable
private fun BookCard(item: HomeRow.Item.BookItem) {
    Column(modifier = Modifier.width(140.dp)) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(0.7f)
                .clip(RoundedCornerShape(12.dp))
                .background(MaterialTheme.colorScheme.surfaceVariant),
        )
        Text(item.book.title, modifier = Modifier.padding(top = 8.dp),
            style = MaterialTheme.typography.titleMedium, maxLines = 2)
    }
}
