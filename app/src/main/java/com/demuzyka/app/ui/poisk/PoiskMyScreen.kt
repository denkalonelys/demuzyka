package com.demuzyka.app.ui.poisk

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Download
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.demuzyka.app.data.AppContainer
import com.demuzyka.app.ui.poisk.parts.FilmPosterTile

/**
 * Poisk "Моё" screen — matches the Kinopoisk screenshot:
 *  * Section title "Моё".
 *  * "Буду смотреть" row of bookmarks.
 *  * "Загрузки" empty-state card.
 */
@Composable
fun PoiskMyScreen(container: AppContainer) {
    val bookmarks by remember { container.filmProvider.bookmarks() }
        .collectAsState(initial = emptyList())

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 96.dp),
    ) {
        item {
            Text(
                "Моё",
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 20.dp),
                style = MaterialTheme.typography.displayLarge,
            )
        }
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text("Буду смотреть", modifier = Modifier.weight(1f),
                    style = MaterialTheme.typography.headlineSmall)
                Text("Все", color = com.demuzyka.app.ui.theme.DeOrange,
                    style = MaterialTheme.typography.labelLarge)
            }
        }
        item {
            LazyRow(
                contentPadding = PaddingValues(horizontal = 20.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                items(bookmarks) { f -> FilmPosterTile(f) }
            }
        }
        item { DownloadsEmptyState() }
        item {
            Text(
                "Покупки",
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 24.dp),
                style = MaterialTheme.typography.headlineSmall,
            )
        }
    }
}

@Composable
private fun DownloadsEmptyState() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 24.dp),
    ) {
        Text("Загрузки", style = MaterialTheme.typography.headlineSmall)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(MaterialTheme.colorScheme.surface)
                .padding(24.dp),
            contentAlignment = Alignment.Center,
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(
                    Icons.Outlined.Download,
                    contentDescription = null,
                    modifier = Modifier.size(56.dp),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                Text(
                    "Загружайте фильмы и сериалы, чтобы\nсмотреть их без интернета",
                    modifier = Modifier.padding(top = 16.dp),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                Button(
                    onClick = { /* navigate to search */ },
                    modifier = Modifier.padding(top = 12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant,
                        contentColor = MaterialTheme.colorScheme.onBackground,
                    ),
                    shape = RoundedCornerShape(28.dp),
                ) { Text("Выбрать, что загрузить") }
            }
        }
    }
}
