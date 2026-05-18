package com.demuzyka.app.ui.music

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.demuzyka.app.data.AppContainer
import com.demuzyka.app.data.music.ConcertCard
import com.demuzyka.app.ui.theme.DeYellow

@Composable
fun MusicConcertsScreen(container: AppContainer) {
    val concerts by remember { container.musicProvider.concerts() }
        .collectAsState(initial = emptyList())

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(top = 8.dp, bottom = 120.dp),
    ) {
        item {
            Text(
                "Концерты",
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
                style = MaterialTheme.typography.headlineLarge,
            )
        }
        items(concerts) { c -> ConcertHero(c) }
    }
}

@Composable
private fun ConcertHero(c: ConcertCard) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp)
            .clip(RoundedCornerShape(24.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant),
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(0.95f)
                .background(MaterialTheme.colorScheme.surface),
            contentAlignment = Alignment.BottomStart,
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(2.dp),
            ) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(MaterialTheme.colorScheme.surface)
                        .padding(horizontal = 8.dp, vertical = 4.dp),
                ) {
                    Text(c.dateText.split("·").first().trim(),
                        style = MaterialTheme.typography.labelMedium)
                }
            }
        }
        Row(
            modifier = Modifier
                .padding(horizontal = 20.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(c.artist, style = MaterialTheme.typography.headlineMedium)
                Text(
                    "${c.venue} · ${c.ageRating}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
        if (c.priceFromRub != null) {
            Button(
                onClick = { /* hook your ticketing flow */ },
                colors = ButtonDefaults.buttonColors(containerColor = DeYellow,
                    contentColor = androidx.compose.ui.graphics.Color.Black),
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 12.dp)
                    .fillMaxWidth(),
                shape = RoundedCornerShape(28.dp),
            ) {
                Text("Купить от ${c.priceFromRub} ₽",
                    style = MaterialTheme.typography.titleMedium)
            }
        }
    }
}
