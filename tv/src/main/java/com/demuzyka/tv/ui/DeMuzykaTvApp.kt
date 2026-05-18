package com.demuzyka.tv.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.tv.material3.Button
import androidx.tv.material3.ButtonDefaults
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.Text
import com.demuzyka.tv.ui.home.MusicHomeTvScreen
import com.demuzyka.tv.ui.home.PoiskHomeTvScreen

/**
 * Top-level TV nav. We deliberately use a simple top bar with two big focus-
 * able pills rather than `NavigationDrawer`, because the drawer's API in
 * `androidx.tv:tv-material:1.0.0` is still settling and tends to break across
 * minor releases. Replace with a real drawer when you wire menu items in.
 */
@Composable
fun DeMuzykaTvApp() {
    var selected by remember { mutableStateOf(TvTab.Muzyka) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
    ) {
        Row(
            modifier = Modifier.padding(start = 56.dp, top = 32.dp, end = 56.dp, bottom = 16.dp),
        ) {
            TvTab.values().forEach { tab ->
                val isActive = tab == selected
                Button(
                    onClick = { selected = tab },
                    colors = if (isActive)
                        ButtonDefaults.colors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = MaterialTheme.colorScheme.onPrimary,
                        )
                    else
                        ButtonDefaults.colors(
                            containerColor = MaterialTheme.colorScheme.surface,
                            contentColor = MaterialTheme.colorScheme.onSurface,
                        ),
                    shape = ButtonDefaults.shape(shape = RoundedCornerShape(50)),
                    modifier = Modifier.height(56.dp),
                ) {
                    Text(tab.title, style = MaterialTheme.typography.titleLarge)
                }
                Spacer(Modifier.width(16.dp))
            }
        }
        Box(Modifier.weight(1f)) {
            when (selected) {
                TvTab.Muzyka -> MusicHomeTvScreen()
                TvTab.Poisk -> PoiskHomeTvScreen()
            }
        }
    }
}

enum class TvTab(val title: String) {
    Muzyka("ДеМузыка"),
    Poisk("ДеПоиск"),
}
