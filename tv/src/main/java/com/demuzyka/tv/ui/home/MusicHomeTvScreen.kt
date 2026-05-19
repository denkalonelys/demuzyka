package com.demuzyka.tv.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.tv.foundation.lazy.list.TvLazyListScope
import androidx.tv.foundation.lazy.list.TvLazyRow
import androidx.tv.foundation.lazy.list.items
import androidx.tv.material3.Card
import androidx.tv.material3.CardDefaults
import androidx.tv.material3.Text
import com.demuzyka.tv.data.SAMPLE_MOODS
import com.demuzyka.tv.data.SAMPLE_PLAYLISTS
import com.demuzyka.tv.data.SAMPLE_TRACKS
import com.demuzyka.tv.data.TvMood
import com.demuzyka.tv.data.TvPlaylist
import com.demuzyka.tv.data.TvTrack
import com.demuzyka.tv.ui.parts.CoverArt
import com.demuzyka.tv.ui.parts.WaveHero
import com.demuzyka.tv.ui.parts.coverPaletteFor

/**
 * Music home for Android TV.  Visual language matches the :app phone
 * scaffold (procedural covers, mesh hero), scaled up for 10-foot UI and
 * laid out for D-pad focus traversal (no touch targets).
 */
@Composable
fun MusicHomeTvScreen() {
    TvLazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(end = 56.dp, bottom = 56.dp),
        verticalArrangement = Arrangement.spacedBy(32.dp),
    ) {
        item { WaveHero() }
        item { SectionHeader("Моя волна"); Box(Modifier.height(8.dp)) }
        item {
            TvLazyRow(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                items(SAMPLE_MOODS) { MoodPillTv(it) }
            }
        }
        item { SectionHeader("Для вас") }
        item {
            TvLazyRow(horizontalArrangement = Arrangement.spacedBy(20.dp)) {
                items(SAMPLE_PLAYLISTS) { PlaylistCardTv(it) }
            }
        }
        item { SectionHeader("Новые релизы") }
        item {
            TvLazyRow(horizontalArrangement = Arrangement.spacedBy(20.dp)) {
                items(SAMPLE_TRACKS) { TrackCardTv(it) }
            }
        }
    }
}

@Composable
internal fun SectionHeader(title: String) {
    Text(
        title,
        modifier = Modifier.padding(top = 8.dp, bottom = 4.dp),
        fontSize = 30.sp,
        fontWeight = FontWeight.Black,
        letterSpacing = (-0.5).sp,
        color = Color.White,
    )
}

@Composable
private fun MoodPillTv(mood: TvMood) {
    val p = coverPaletteFor(mood.id)
    Card(
        onClick = { /* TODO: open mood */ },
        colors = CardDefaults.colors(containerColor = Color.Transparent, contentColor = Color.White),
        modifier = Modifier
            .height(140.dp)
            .width(280.dp),
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(20.dp))
                .background(Brush.linearGradient(listOf(p.start, p.end)))
                .padding(20.dp),
        ) {
            Text(
                mood.title,
                modifier = Modifier.align(Alignment.BottomStart),
                fontSize = 28.sp,
                fontWeight = FontWeight.Black,
                color = Color.White,
            )
        }
    }
}

@Composable
internal fun PlaylistCardTv(p: TvPlaylist) {
    Card(
        onClick = { /* open playlist */ },
        colors = CardDefaults.colors(containerColor = Color.Transparent, contentColor = Color.White),
        modifier = Modifier.width(260.dp),
    ) {
        Column {
            CoverArt(
                id = p.id,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f),
                corner = 16.dp,
            )
            Text(
                p.title,
                modifier = Modifier.padding(top = 10.dp, start = 4.dp, end = 4.dp),
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.White,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
            )
            Text(
                p.subtitle,
                modifier = Modifier.padding(start = 4.dp, end = 4.dp, bottom = 6.dp),
                fontSize = 13.sp,
                color = Color.White.copy(alpha = 0.55f),
            )
        }
    }
}

@Composable
internal fun TrackCardTv(t: TvTrack) {
    Card(
        onClick = { /* play track */ },
        colors = CardDefaults.colors(containerColor = Color.Transparent, contentColor = Color.White),
        modifier = Modifier.width(220.dp),
    ) {
        Column {
            CoverArt(
                id = t.id,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f),
                corner = 14.dp,
            )
            Text(
                t.title,
                modifier = Modifier.padding(top = 10.dp, start = 4.dp, end = 4.dp),
                fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.White,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            Text(
                t.artist,
                modifier = Modifier.padding(start = 4.dp, end = 4.dp, bottom = 6.dp),
                fontSize = 12.sp,
                color = Color.White.copy(alpha = 0.55f),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }
    }
}
