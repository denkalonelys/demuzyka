package com.demuzyka.app.ui.music

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.PlayArrow
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.demuzyka.app.data.AppContainer
import com.demuzyka.app.data.music.HomeRow
import com.demuzyka.app.data.music.StubMusicProvider
import com.demuzyka.app.data.music.Track
import com.demuzyka.app.ui.parts.CoverArt
import com.demuzyka.app.ui.parts.EqBars
import com.demuzyka.app.ui.parts.MeshHero
import com.demuzyka.app.ui.parts.SectionTitle
import com.demuzyka.app.ui.theme.DeMagenta
import com.demuzyka.app.ui.theme.DeOrange
import com.demuzyka.app.ui.theme.DePink
import com.demuzyka.app.ui.theme.DeYellow
import com.demuzyka.app.ui.theme.coverPaletteFor

/**
 * Music home — Y.Music layout, but procedural artwork.
 *
 *  1. Soft status-bar gradient + ДеМузыка title + search icon.
 *  2. Big "Моя волна" hero with an animated mesh-gradient background.
 *  3. Mood-pill row.
 *  4. Horizontal rows: «Для вас», «Новые релизы», «Шеф рекомендует», «Мне нравится».
 *  5. Track tile shows EQ-bars when this track is currently playing.
 */
@Composable
fun MusicHomeScreen(container: AppContainer) {
    val rows by remember { container.musicProvider.homeRows() }.collectAsState(initial = emptyList())
    val now by container.musicProvider.nowPlaying.collectAsState()
    val provider = container.musicProvider as? StubMusicProvider
    val onPlayTrack: (Track) -> Unit = { track -> provider?.play(track) }
    val onPlayWave: () -> Unit = {
        val firstTrack = rows.firstNotNullOfOrNull { row ->
            (row.items.firstOrNull { it is HomeRow.Item.TrackItem } as? HomeRow.Item.TrackItem)?.track
        } ?: Track("wave", "Моя волна", "Радио", null, 0)
        provider?.play(firstTrack)
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 24.dp),
    ) {
        item { HomeHeader() }
        item { WaveHero(onClick = onPlayWave) }
        items(rows) { row ->
            HomeRowSection(
                row,
                playingId = now?.track?.id,
                isPlaying = now?.isPlaying == true,
                onPlayTrack = onPlayTrack,
            )
        }
        item { Spacer(Modifier.height(24.dp)) }
    }
}

@Composable
private fun HomeHeader() {
    val topInset = WindowInsets.statusBars.asPaddingValues().calculateTopPadding()
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = topInset)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        // Plus crown — looks like the Y.Plus badge in the corner of Y.Music.
        Box(
            modifier = Modifier
                .size(34.dp)
                .clip(CircleShape)
                .background(Brush.linearGradient(listOf(DePink, DeMagenta))),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                "ДМ",
                color = Color.White,
                fontWeight = FontWeight.Black,
                fontSize = 13.sp,
            )
        }
        Column(modifier = Modifier.padding(start = 12.dp).weight(1f)) {
            Text(
                "ДеМузыка",
                fontSize = 22.sp,
                fontWeight = FontWeight.Black,
                color = Color.White,
                letterSpacing = (-0.6).sp,
            )
            Text(
                "Подписка Плюс активна",
                fontSize = 12.sp,
                color = Color.White.copy(alpha = 0.45f),
            )
        }
        IconButton(onClick = { /* TODO: open search */ }) {
            Icon(Icons.Outlined.Search, contentDescription = "Поиск", tint = Color.White)
        }
    }
}

/** Big animated "Моя волна" hero — multi-orbit mesh gradient. */
@Composable
private fun WaveHero(onClick: () -> Unit) {
    MeshHero(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .aspectRatio(0.95f)
            .clip(RoundedCornerShape(28.dp))
            .clickable(onClick = onClick),
        a = DePink,
        b = DeMagenta,
        c = DeOrange,
    ) {
        Box(modifier = Modifier.fillMaxSize().padding(20.dp)) {
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Text(
                    "Моя",
                    fontSize = 48.sp,
                    fontWeight = FontWeight.Black,
                    letterSpacing = (-1.5).sp,
                    color = Color.White,
                )
                Text(
                    "волна",
                    fontSize = 48.sp,
                    fontWeight = FontWeight.Black,
                    letterSpacing = (-1.5).sp,
                    color = Color.White,
                )
            }
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Bottom,
            ) {
                Text(
                    "Бесконечный поток под ваше настроение",
                    fontSize = 13.sp,
                    color = Color.White.copy(alpha = 0.85f),
                    modifier = Modifier.padding(bottom = 14.dp),
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .height(46.dp)
                            .clip(RoundedCornerShape(24.dp))
                            .background(Color.White)
                            .clickable(onClick = onClick)
                            .padding(horizontal = 18.dp),
                        contentAlignment = Alignment.Center,
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                Icons.Outlined.PlayArrow,
                                null,
                                tint = Color.Black,
                                modifier = Modifier.size(22.dp),
                            )
                            Spacer(Modifier.width(6.dp))
                            Text(
                                "Включить",
                                color = Color.Black,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun HomeRowSection(
    row: HomeRow,
    playingId: String?,
    isPlaying: Boolean,
    onPlayTrack: (Track) -> Unit,
) {
    Column(modifier = Modifier.fillMaxWidth().padding(top = 18.dp)) {
        SectionTitle(title = row.title, onMore = { /* TODO: open */ })
        when (row.kind) {
            HomeRow.Kind.Mood -> LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                items(row.items.filterIsInstance<HomeRow.Item.MoodItem>()) { MoodPill(it) }
            }
            HomeRow.Kind.Playlists -> LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                items(row.items.filterIsInstance<HomeRow.Item.PlaylistItem>()) { PlaylistCard(it) }
            }
            HomeRow.Kind.Tracks -> {
                // "Мне нравится" → vertical track list.  Everything else
                // (e.g. "Новые релизы") → horizontal TrackTile carousel.
                if (row.title == "Мне нравится") {
                    Column(modifier = Modifier.padding(horizontal = 8.dp)) {
                        row.items.filterIsInstance<HomeRow.Item.TrackItem>().forEach { item ->
                            TrackRow(
                                item = item,
                                playing = playingId == item.track.id && isPlaying,
                                active = playingId == item.track.id,
                                onClick = { onPlayTrack(item.track) },
                            )
                        }
                    }
                } else {
                    LazyRow(
                        contentPadding = PaddingValues(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                    ) {
                        items(row.items.filterIsInstance<HomeRow.Item.TrackItem>()) { item ->
                            TrackTile(
                                item = item,
                                playing = playingId == item.track.id && isPlaying,
                                active = playingId == item.track.id,
                                onClick = { onPlayTrack(item.track) },
                            )
                        }
                    }
                }
            }
            else -> LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                items(row.items) { item ->
                    when (item) {
                        is HomeRow.Item.MoodItem -> MoodPill(item)
                        is HomeRow.Item.PlaylistItem -> PlaylistCard(item)
                        is HomeRow.Item.TrackItem -> TrackTile(
                            item = item,
                            playing = playingId == item.track.id && isPlaying,
                            active = playingId == item.track.id,
                            onClick = { onPlayTrack(item.track) },
                        )
                        is HomeRow.Item.BookItem -> BookCard(item)
                    }
                }
            }
        }
    }
}

/** Sleek pill chip — like the mood selector in Y.Music. */
@Composable
private fun MoodPill(item: HomeRow.Item.MoodItem) {
    val p = coverPaletteFor(item.moodId)
    Box(
        modifier = Modifier
            .height(96.dp)
            .width(150.dp)
            .clip(RoundedCornerShape(18.dp))
            .background(Brush.linearGradient(listOf(p.start, p.end)))
            .clickable { /* TODO: open mood */ }
            .padding(12.dp),
    ) {
        Text(
            item.title,
            color = Color.White,
            fontWeight = FontWeight.Black,
            fontSize = 18.sp,
            letterSpacing = (-0.3).sp,
            modifier = Modifier.align(Alignment.BottomStart),
        )
    }
}

@Composable
private fun PlaylistCard(item: HomeRow.Item.PlaylistItem) {
    Column(
        modifier = Modifier
            .width(150.dp)
            .clickable { /* TODO: open playlist */ },
    ) {
        CoverArt(
            id = item.playlist.id,
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f),
            corner = 14.dp,
        )
        Text(
            item.playlist.title,
            modifier = Modifier.padding(top = 8.dp),
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color.White,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
        )
        Text(
            "Плейлист",
            fontSize = 12.sp,
            color = Color.White.copy(alpha = 0.5f),
        )
    }
}

/** Compact track tile used inside horizontal rows. */
@Composable
private fun TrackTile(
    item: HomeRow.Item.TrackItem,
    playing: Boolean,
    active: Boolean,
    onClick: () -> Unit,
) {
    val tint = if (active) DeYellow else Color.White
    Column(
        modifier = Modifier
            .width(150.dp)
            .clickable(onClick = onClick),
    ) {
        Box {
            CoverArt(
                id = item.track.id,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f),
                corner = 14.dp,
            )
            if (active) {
                Box(
                    modifier = Modifier
                        .padding(8.dp)
                        .clip(CircleShape)
                        .background(Color.Black.copy(alpha = 0.45f))
                        .padding(horizontal = 6.dp, vertical = 4.dp),
                ) { EqBars(playing = playing, tint = DeYellow) }
            }
        }
        Text(
            item.track.title,
            modifier = Modifier.padding(top = 8.dp),
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold,
            color = tint,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
        Text(
            item.track.artist,
            fontSize = 12.sp,
            color = Color.White.copy(alpha = 0.55f),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
    }
}

/** Wide track row — used in the "Мне нравится" track list. */
@Composable
private fun TrackRow(
    item: HomeRow.Item.TrackItem,
    playing: Boolean,
    active: Boolean,
    onClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .clickable(onClick = onClick)
            .padding(horizontal = 8.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box {
            CoverArt(
                id = item.track.id,
                modifier = Modifier.size(48.dp),
                corner = 8.dp,
            )
            if (active) {
                Box(
                    modifier = Modifier
                        .matchParentSize()
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color.Black.copy(alpha = 0.4f)),
                    contentAlignment = Alignment.Center,
                ) { EqBars(playing = playing, tint = DeYellow) }
            }
        }
        Column(modifier = Modifier.weight(1f).padding(start = 12.dp)) {
            Text(
                item.track.title,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = if (active) DeYellow else Color.White,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            Text(
                item.track.artist,
                fontSize = 12.sp,
                color = Color.White.copy(alpha = 0.55f),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }
        Text(
            formatDuration(item.track.durationSec),
            fontSize = 12.sp,
            color = Color.White.copy(alpha = 0.45f),
        )
    }
}

@Composable
private fun BookCard(item: HomeRow.Item.BookItem) {
    Column(modifier = Modifier.width(150.dp)) {
        CoverArt(
            id = item.book.id,
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(0.7f),
            corner = 12.dp,
        )
        Text(
            item.book.title,
            modifier = Modifier.padding(top = 8.dp),
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color.White,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
        )
        Text(
            item.book.author,
            fontSize = 12.sp,
            color = Color.White.copy(alpha = 0.55f),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
    }
}

private fun formatDuration(sec: Int): String {
    val m = sec / 60
    val s = sec % 60
    return "%d:%02d".format(m, s)
}
