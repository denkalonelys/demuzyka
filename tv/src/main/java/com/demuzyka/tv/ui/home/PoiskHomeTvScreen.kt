package com.demuzyka.tv.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.PlayArrow
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.tv.foundation.lazy.list.TvLazyColumn
import androidx.tv.foundation.lazy.list.TvLazyRow
import androidx.tv.foundation.lazy.list.items
import androidx.tv.material3.Card
import androidx.tv.material3.CardDefaults
import androidx.tv.material3.Icon
import androidx.tv.material3.Text
import com.demuzyka.tv.data.SAMPLE_FILMS
import com.demuzyka.tv.data.TvFilm
import com.demuzyka.tv.ui.parts.CoverArt
import com.demuzyka.tv.ui.parts.MeshHero
import com.demuzyka.tv.ui.parts.coverPaletteFor

@Composable
fun PoiskHomeTvScreen() {
    val featured = SAMPLE_FILMS.first()
    TvLazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(end = 56.dp, bottom = 56.dp),
        verticalArrangement = Arrangement.spacedBy(28.dp),
    ) {
        item { FeaturedHero(featured) }
        item { SectionHeader("Советуем посмотреть") }
        item {
            TvLazyRow(horizontalArrangement = Arrangement.spacedBy(20.dp)) {
                items(SAMPLE_FILMS) { FilmPoster(it) }
            }
        }
        item { SectionHeader("Сериалы на основе ваших интересов") }
        item {
            TvLazyRow(horizontalArrangement = Arrangement.spacedBy(20.dp)) {
                items(SAMPLE_FILMS.filter { it.genre.contains("сериал") }) { FilmPoster(it) }
            }
        }
        item { SectionHeader("Фильмы для вас") }
        item {
            TvLazyRow(horizontalArrangement = Arrangement.spacedBy(20.dp)) {
                items(SAMPLE_FILMS.shuffled()) { FilmPoster(it) }
            }
        }
    }
}

@Composable
private fun FeaturedHero(film: TvFilm) {
    val p = coverPaletteFor(film.id)
    MeshHero(
        modifier = Modifier
            .fillMaxWidth()
            .height(420.dp)
            .clip(RoundedCornerShape(28.dp)),
        a = p.start,
        b = p.mid,
        c = p.end,
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(48.dp),
            verticalArrangement = Arrangement.Bottom,
        ) {
            Text(
                film.title,
                fontSize = 56.sp,
                fontWeight = FontWeight.Black,
                color = Color.White,
                letterSpacing = (-1.5).sp,
            )
            Text(
                "${film.year ?: "—"} · ${film.genre.joinToString(", ")}",
                fontSize = 16.sp,
                color = Color.White.copy(alpha = 0.85f),
                modifier = Modifier.padding(top = 12.dp),
            )
            Row(
                modifier = Modifier.padding(top = 24.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(30.dp))
                        .background(Color.White)
                        .padding(horizontal = 28.dp, vertical = 14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(Icons.Outlined.PlayArrow, null, tint = Color.Black, modifier = Modifier.size(28.dp))
                    Text(
                        "Смотреть",
                        modifier = Modifier.padding(start = 8.dp),
                        color = Color.Black,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                    )
                }
            }
        }
    }
}

@Composable
private fun FilmPoster(f: TvFilm) {
    Card(
        onClick = { /* navigate to detail */ },
        colors = CardDefaults.colors(containerColor = Color.Transparent, contentColor = Color.White),
        modifier = Modifier.width(220.dp),
    ) {
        Column {
            Box {
                CoverArt(
                    id = f.id,
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(0.7f),
                    corner = 14.dp,
                )
                if (f.rating != null) {
                    Box(
                        modifier = Modifier
                            .padding(8.dp)
                            .clip(RoundedCornerShape(6.dp))
                            .background(
                                if (f.rating >= 7f) Color(0xFF3BB45F) else Color(0xFF7C7C7E)
                            )
                            .padding(horizontal = 8.dp, vertical = 3.dp),
                    ) {
                        Text(
                            "%.1f".format(f.rating),
                            color = Color.White,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                        )
                    }
                }
            }
            Text(
                f.title,
                modifier = Modifier.padding(top = 10.dp, start = 4.dp, end = 4.dp),
                fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.White,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
            )
            Text(
                "${f.year ?: "—"} · ${f.genre.firstOrNull() ?: ""}",
                modifier = Modifier.padding(start = 4.dp, end = 4.dp, bottom = 6.dp),
                fontSize = 12.sp,
                color = Color.White.copy(alpha = 0.55f),
                maxLines = 1,
            )
        }
    }
}
