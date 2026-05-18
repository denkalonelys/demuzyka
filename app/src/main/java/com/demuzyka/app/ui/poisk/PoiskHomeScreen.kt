package com.demuzyka.app.ui.poisk

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.PersonAddAlt
import androidx.compose.material.icons.outlined.RemoveCircleOutline
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.demuzyka.app.data.AppContainer
import com.demuzyka.app.data.poisk.Film
import com.demuzyka.app.data.poisk.FilmRow
import com.demuzyka.app.ui.poisk.parts.FilmPosterTile
import com.demuzyka.app.ui.poisk.parts.FilmCinemaTile
import com.demuzyka.app.ui.theme.DeOrange

/**
 * Poisk "Главное" screen — Kinopoisk-style:
 *  * Big featured carousel with title + tagline + "Купить билеты".
 *  * Streaming-house chips ("Смотрим / Первый канал / НТВ").
 *  * Horizontal rows ("Сериалы на основе ваших интересов", "Фильмы для вас").
 */
@Composable
fun PoiskHomeScreen(container: AppContainer) {
    val featured by remember { container.filmProvider.featured() }
        .collectAsState(initial = emptyList())
    val rows by remember { container.filmProvider.homeRows() }
        .collectAsState(initial = emptyList())

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 96.dp),
    ) {
        items(featured) { f -> FeaturedHero(f.film, f.tagline, f.buttonText) }
        item { StreamingChipsRow() }
        items(rows) { row -> FilmsRowSection(row) }
    }
}

@Composable
private fun FeaturedHero(film: Film, tagline: String, buttonText: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 0.dp, vertical = 0.dp),
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(0.85f)
                .background(
                    Brush.verticalGradient(
                        listOf(
                            MaterialTheme.colorScheme.surface,
                            MaterialTheme.colorScheme.background,
                        )
                    )
                ),
            contentAlignment = Alignment.BottomCenter,
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(horizontal = 24.dp, vertical = 24.dp),
            ) {
                Text(
                    film.title,
                    style = MaterialTheme.typography.displayLarge,
                )
                Text(
                    tagline,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(top = 12.dp),
                )
                Row(
                    modifier = Modifier.padding(top = 16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Button(
                        onClick = { /* TICKETING hook */ },
                        colors = ButtonDefaults.buttonColors(containerColor = DeOrange),
                        shape = RoundedCornerShape(28.dp),
                    ) { Text(buttonText) }
                    Spacer(Modifier.width(8.dp))
                    CircleIconButton(Icons.Outlined.PersonAddAlt)
                    Spacer(Modifier.width(8.dp))
                    CircleIconButton(Icons.Outlined.RemoveCircleOutline)
                }
                Text(
                    "Кешбэк баллами Плюса при оплате картой Пэй >",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(top = 8.dp),
                )
                // pagination dots
                Row(
                    modifier = Modifier.padding(top = 12.dp),
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                ) {
                    repeat(10) { i ->
                        Box(
                            modifier = Modifier
                                .size(if (i == 0) 22.dp else 6.dp, 6.dp)
                                .clip(CircleShape)
                                .background(
                                    if (i == 0) MaterialTheme.colorScheme.onBackground
                                    else MaterialTheme.colorScheme.onBackground.copy(alpha = 0.3f)
                                ),
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun CircleIconButton(icon: androidx.compose.ui.graphics.vector.ImageVector) {
    Box(
        modifier = Modifier
            .size(40.dp)
            .clip(CircleShape)
            .background(Color.Black.copy(alpha = 0.6f)),
        contentAlignment = Alignment.Center,
    ) {
        Icon(icon, null, tint = MaterialTheme.colorScheme.onBackground)
    }
}

@Composable
private fun StreamingChipsRow() {
    Column(modifier = Modifier.padding(top = 16.dp)) {
        Text(
            "Витрины стримингов",
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            style = MaterialTheme.typography.headlineSmall,
        )
        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            items(listOf("Смотрим", "Первый канал", "НТВ", "СТС", "Премьер")) { label ->
                Surface(
                    shape = RoundedCornerShape(50),
                    color = MaterialTheme.colorScheme.surfaceVariant,
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Box(
                            modifier = Modifier
                                .size(20.dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.surface),
                        )
                        Text(
                            label,
                            modifier = Modifier.padding(start = 8.dp),
                            style = MaterialTheme.typography.titleMedium,
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun FilmsRowSection(row: FilmRow) {
    Column(modifier = Modifier.padding(top = 20.dp)) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(row.title, modifier = Modifier.weight(1f),
                style = MaterialTheme.typography.headlineSmall)
            Text("Все", color = DeOrange, style = MaterialTheme.typography.labelLarge)
        }
        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            items(row.items) { film ->
                if (row.cinema) FilmCinemaTile(film) else FilmPosterTile(film)
            }
        }
    }
}
