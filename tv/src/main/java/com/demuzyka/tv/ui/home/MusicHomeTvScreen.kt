package com.demuzyka.tv.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.dp
import androidx.tv.foundation.lazy.list.TvLazyColumn
import androidx.tv.foundation.lazy.list.TvLazyRow
import androidx.tv.foundation.lazy.list.items
import androidx.tv.material3.Card
import androidx.tv.material3.CardDefaults
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.Text
import com.demuzyka.tv.data.SAMPLE_PLAYLISTS
import com.demuzyka.tv.data.SAMPLE_TRACKS
import com.demuzyka.tv.ui.parts.WaveHero

@Composable
fun MusicHomeTvScreen() {
    TvLazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(end = 56.dp, bottom = 56.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp),
    ) {
        item { WaveHero() }
        item {
            HomeRow(title = "Моя волна") {
                items(SAMPLE_PLAYLISTS) { p -> PlaylistCard(p.title) }
            }
        }
        item {
            HomeRow(title = "Мне нравится") {
                items(SAMPLE_TRACKS) { t -> TrackCard(t.title, t.artist) }
            }
        }
    }
}

@Composable
internal fun HomeRow(title: String, content: androidx.tv.foundation.lazy.list.TvLazyListScope.() -> Unit) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            title,
            modifier = Modifier.padding(start = 0.dp, end = 0.dp, bottom = 12.dp),
            style = MaterialTheme.typography.headlineMedium,
        )
        TvLazyRow(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            content = content,
        )
    }
}

@Composable
internal fun PlaylistCard(title: String) {
    Card(
        onClick = { /* open playlist */ },
        colors = CardDefaults.colors(
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onSurface,
        ),
        modifier = Modifier.width(260.dp),
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
                .background(MaterialTheme.colorScheme.surfaceVariant),
        )
        Text(
            title,
            modifier = Modifier.padding(12.dp),
            style = MaterialTheme.typography.titleMedium,
            maxLines = 2,
        )
    }
}

@Composable
internal fun TrackCard(title: String, artist: String) {
    Card(
        onClick = { /* play track */ },
        colors = CardDefaults.colors(
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onSurface,
        ),
        modifier = Modifier.width(360.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(RoundedCornerShape(6.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant),
            )
            Column(modifier = Modifier.padding(start = 12.dp)) {
                Text(title, style = MaterialTheme.typography.titleMedium, maxLines = 1)
                Text(
                    artist,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface,
                )
            }
        }
    }
}
