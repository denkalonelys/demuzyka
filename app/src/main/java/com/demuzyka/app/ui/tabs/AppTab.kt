package com.demuzyka.app.ui.tabs

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

enum class AppTab(val title: String) {
    Muzyka("ДеМузыка"),
    Poisk("ДеПоиск"),
}

/**
 * Two-pill product switcher. Mirrors the section-tab look from Kinopoisk
 * («Моё кино / Каналы / Загрузить») but operates at the app-product level:
 * tapping a tab swaps the entire bottom navigation underneath.
 */
@Composable
fun AppTabRow(
    current: AppTab,
    onSelect: (AppTab) -> Unit,
    modifier: Modifier = Modifier,
) {
    val statusPadding = WindowInsets.statusBars.asPaddingValues()
    Surface(
        color = MaterialTheme.colorScheme.background,
        modifier = modifier.fillMaxWidth(),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = statusPadding.calculateTopPadding())
                .padding(start = 20.dp, end = 20.dp, top = 12.dp, bottom = 8.dp),
            verticalAlignment = Alignment.Bottom,
        ) {
            AppTab.values().forEach { tab ->
                val selected = tab == current
                val targetTextColor =
                    if (selected) MaterialTheme.colorScheme.onBackground
                    else MaterialTheme.colorScheme.onBackground.copy(alpha = 0.4f)
                val targetUnderline =
                    if (selected) MaterialTheme.colorScheme.primary
                    else MaterialTheme.colorScheme.primary.copy(alpha = 0f)
                val textColor by animateColorAsState(targetTextColor, tween(220), label = "tab-text")
                val underlineColor by animateColorAsState(targetUnderline, tween(260), label = "tab-underline")
                Column(
                    modifier = Modifier
                        .padding(end = 28.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .clickable(enabled = !selected) { onSelect(tab) },
                    horizontalAlignment = Alignment.Start,
                ) {
                    Text(
                        text = tab.title,
                        color = textColor,
                        fontSize = 22.sp,
                    )
                    Box(
                        Modifier
                            .padding(top = 6.dp)
                            .height(3.dp)
                            .width(34.dp)
                            .clip(RoundedCornerShape(2.dp))
                            .background(underlineColor),
                    )
                }
            }
        }
    }
}
