package com.demuzyka.app.ui.poisk.parts

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Bolt
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.demuzyka.app.data.poisk.Film
import com.demuzyka.app.ui.theme.DeRatingGood
import com.demuzyka.app.ui.theme.DeRatingMid

/**
 * Poster tile with Kinopoisk-style rating chip (top-left) + new-episodes
 * "⚡10" badge (top-right) overlaying the poster.
 */
@Composable
fun FilmPosterTile(film: Film) {
    Column(modifier = Modifier.width(120.dp)) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(0.7f)
                .clip(RoundedCornerShape(12.dp))
                .background(MaterialTheme.colorScheme.surfaceVariant),
        ) {
            if (film.rating != null) RatingChip(film.rating, Modifier.padding(8.dp))
            if (film.newEpisodes != null) NewEpisodesChip(film.newEpisodes,
                Modifier.align(Alignment.TopEnd).padding(8.dp))
        }
        Text(
            film.title,
            modifier = Modifier.padding(top = 8.dp),
            maxLines = 2,
            style = MaterialTheme.typography.titleMedium,
        )
        if (film.genres.isNotEmpty()) {
            Text(
                film.genres.first(),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

/** Bigger backdrop tile used for the "Смотрите в кино" row. */
@Composable
fun FilmCinemaTile(film: Film) {
    Column(modifier = Modifier.width(160.dp)) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(0.75f)
                .clip(RoundedCornerShape(12.dp))
                .background(MaterialTheme.colorScheme.surfaceVariant),
        ) {
            if (film.rating != null) RatingChip(film.rating, Modifier.padding(8.dp))
            // "В кино с …" label sits at the bottom.
            Box(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(8.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(Color.Black.copy(alpha = 0.7f))
                    .padding(horizontal = 6.dp, vertical = 2.dp),
            ) {
                Text(
                    "В КИНО",
                    style = MaterialTheme.typography.labelSmall,
                    color = Color.White,
                )
            }
        }
        Text(
            film.title,
            modifier = Modifier.padding(top = 8.dp),
            maxLines = 2,
            style = MaterialTheme.typography.titleMedium,
        )
    }
}

@Composable
private fun RatingChip(rating: Float, modifier: Modifier = Modifier) {
    val bg = if (rating >= 7f) DeRatingGood else DeRatingMid
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(4.dp))
            .background(bg)
            .padding(horizontal = 6.dp, vertical = 2.dp),
    ) {
        Text(
            "%.1f".format(rating),
            style = MaterialTheme.typography.labelMedium,
            color = Color.White,
        )
    }
}

@Composable
private fun NewEpisodesChip(count: Int, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(4.dp))
            .background(Color(0xFF6B3CE8))
            .padding(horizontal = 6.dp, vertical = 2.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            Icons.Outlined.Bolt,
            null,
            tint = Color.White,
            modifier = Modifier.padding(end = 2.dp),
        )
        Text(
            count.toString(),
            style = MaterialTheme.typography.labelMedium,
            color = Color.White,
        )
    }
}
