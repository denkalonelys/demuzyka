package com.demuzyka.tv.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.tv.foundation.lazy.list.TvLazyColumn
import androidx.tv.foundation.lazy.list.items
import androidx.tv.material3.Card
import androidx.tv.material3.CardDefaults
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.Text
import com.demuzyka.tv.data.SAMPLE_FILMS
import com.demuzyka.tv.data.TvFilm

@Composable
fun PoiskHomeTvScreen() {
    TvLazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(end = 56.dp, bottom = 56.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp),
    ) {
        item {
            Text(
                "Кинопоиск (Де-вариант)",
                style = MaterialTheme.typography.displayMedium,
            )
        }
        item {
            HomeRow(title = "Советуем посмотреть") {
                items(SAMPLE_FILMS) { f -> FilmPoster(f) }
            }
        }
        item {
            HomeRow(title = "Сериалы на основе ваших интересов") {
                items(SAMPLE_FILMS.filter { it.genre.contains("сериал") }) { f -> FilmPoster(f) }
            }
        }
        item {
            HomeRow(title = "Фильмы для вас") {
                items(SAMPLE_FILMS.shuffled()) { f -> FilmPoster(f) }
            }
        }
    }
}

@Composable
private fun FilmPoster(f: TvFilm) {
    Card(
        onClick = { /* navigate to detail */ },
        colors = CardDefaults.colors(
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onSurface,
        ),
        modifier = Modifier.width(220.dp),
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(0.7f)
                .background(MaterialTheme.colorScheme.surfaceVariant),
        ) {
            if (f.rating != null) {
                Box(
                    modifier = Modifier
                        .padding(8.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(if (f.rating >= 7f) Color(0xFF3BB45F) else Color(0xFF7C7C7E))
                        .padding(horizontal = 6.dp, vertical = 2.dp),
                ) {
                    Text("%.1f".format(f.rating), color = Color.White,
                        style = MaterialTheme.typography.labelMedium)
                }
            }
        }
        Text(
            f.title,
            modifier = Modifier.padding(12.dp),
            style = MaterialTheme.typography.titleMedium,
            maxLines = 2,
        )
    }
}
