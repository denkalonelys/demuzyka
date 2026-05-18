package com.demuzyka.app.ui.music

import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.DownloadForOffline
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material.icons.outlined.PlayCircle
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
import com.demuzyka.app.data.music.Track
import com.demuzyka.app.ui.theme.DeMagenta
import com.demuzyka.app.ui.theme.DePink
import com.demuzyka.app.ui.theme.DeYellow

/**
 * "Лайки" tab — matches the screenshot: gradient wave button, "Мне нравится"
 * row with track count, then linear list of tracks with download icon.
 */
@Composable
fun MusicCollectionScreen(container: AppContainer) {
    val likes by remember { container.musicProvider.likes }.collectAsState(initial = emptyList())

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 120.dp),
    ) {
        item { CollectionHeader() }
        item { WaveBigButton() }
        item { LikesRow(count = likes.size) }
        items(likes) { track -> TrackRow(track) }
    }
}

@Composable
private fun CollectionHeader() {
    Row(modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)) {
        Text("Коллекция", style = MaterialTheme.typography.headlineLarge)
    }
}

@Composable
private fun WaveBigButton() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .height(86.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(Brush.horizontalGradient(listOf(DePink, DeMagenta))),
        contentAlignment = Alignment.Center,
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Outlined.PlayCircle, null, modifier = Modifier.size(28.dp))
            Column(modifier = Modifier.padding(start = 8.dp)) {
                Text("Моя волна по разделу", style = MaterialTheme.typography.titleSmall)
                Text("Коллекция", style = MaterialTheme.typography.headlineSmall)
            }
        }
    }
}

@Composable
private fun LikesRow(count: Int) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .size(64.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(DePink),
            contentAlignment = Alignment.Center,
        ) {
            Icon(Icons.Outlined.Favorite, null, tint = androidx.compose.ui.graphics.Color.White)
        }
        Column(modifier = Modifier.padding(start = 12.dp)) {
            Text("Мне нравится", style = MaterialTheme.typography.titleLarge)
            Text("$count треков", style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

@Composable
private fun TrackRow(track: Track) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(RoundedCornerShape(6.dp))
                .background(MaterialTheme.colorScheme.surfaceVariant),
        )
        Column(modifier = Modifier.padding(start = 12.dp).weight(1f)) {
            Text(track.title, style = MaterialTheme.typography.titleMedium, maxLines = 1)
            Text(
                track.artist,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1,
            )
        }
        Icon(
            Icons.Outlined.DownloadForOffline,
            contentDescription = "Скачать",
            tint = DeYellow,
            modifier = Modifier.size(28.dp),
        )
        Icon(
            Icons.Outlined.MoreVert,
            contentDescription = null,
            modifier = Modifier.padding(start = 8.dp),
        )
    }
}
