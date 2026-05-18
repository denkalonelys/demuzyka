package com.demuzyka.app.ui.music

import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.PlayArrow
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.dp
import com.demuzyka.app.data.AppContainer
import com.demuzyka.app.ui.theme.DeMagenta
import com.demuzyka.app.ui.theme.DePink
import com.demuzyka.app.ui.theme.DeYellow

@Composable
fun MusicBooksScreen(container: AppContainer) {
    val books by remember { container.musicProvider.books() }
        .collectAsState(initial = emptyList())

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 120.dp),
    ) {
        item {
            Text(
                "Книги и подкасты",
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
                style = MaterialTheme.typography.headlineLarge,
            )
        }
        item { TopBookHero() }
        item { ShelfRow() }
        item {
            Text(
                "Актуальное",
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
                style = MaterialTheme.typography.headlineSmall,
            )
        }
        items(books) { b -> BookHero(b.title, b.author, b.durationMin) }
    }
}

@Composable
private fun TopBookHero() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(Brush.horizontalGradient(listOf(DeYellow.copy(alpha = 0.4f), DePink.copy(alpha = 0.4f))))
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .size(64.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(MaterialTheme.colorScheme.surface),
        )
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(start = 12.dp),
        ) {
            Text("СЕСТРЫ БЛЭК.\nЖизнь Нарциссы, …", style = MaterialTheme.typography.titleMedium)
            Text("56 минут", style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(androidx.compose.ui.graphics.Color.White),
            contentAlignment = Alignment.Center,
        ) {
            Icon(Icons.Outlined.PlayArrow, null, tint = androidx.compose.ui.graphics.Color.Black)
        }
    }
}

@Composable
private fun ShelfRow() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
    ) {
        ShelfTile("Моя\nполка", DeMagenta, Modifier.weight(1f))
        Box(Modifier.width(12.dp))
        ShelfTile("Новые\nвыпуски", DePink, Modifier.weight(1f))
    }
}

@Composable
private fun ShelfTile(title: String, color: androidx.compose.ui.graphics.Color, modifier: Modifier) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(color)
            .padding(16.dp)
            .aspectRatio(1.6f),
        contentAlignment = Alignment.TopStart,
    ) {
        Text(title, style = MaterialTheme.typography.titleLarge)
    }
}

@Composable
private fun BookHero(title: String, author: String, durationMin: Int) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .aspectRatio(1.4f)
            .clip(RoundedCornerShape(20.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant),
        contentAlignment = Alignment.BottomStart,
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(title, style = MaterialTheme.typography.titleLarge, maxLines = 2)
            Text("$author · $durationMin мин",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}
