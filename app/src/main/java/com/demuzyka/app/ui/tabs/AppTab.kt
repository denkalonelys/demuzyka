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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

enum class AppTab(val title: String) {
    Muzyka("ДеМузыка"),
    Poisk("ДеПоиск"),
}

/**
 * Two-pill product switcher.  Mirrors Kinopoisk's section-tab look
 * («Моё кино / Каналы / Загрузить») but operates at the app-product
 * level: tapping a tab swaps the entire bottom navigation under it.
 *
 * Underline is animated, text colour cross-fades.
 */
@Composable
fun AppTabRow(
    current: AppTab,
    onSelect: (AppTab) -> Unit,
    modifier: Modifier = Modifier,
) {
    val statusPadding = WindowInsets.statusBars.asPaddingValues()
    Surface(
        color = Color.Black,
        modifier = modifier.fillMaxWidth(),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = statusPadding.calculateTopPadding())
                .padding(start = 18.dp, end = 18.dp, top = 14.dp, bottom = 10.dp),
            verticalAlignment = Alignment.Bottom,
        ) {
            AppTab.values().forEach { tab ->
                val selected = tab == current
                val targetTextColor =
                    if (selected) Color.White
                    else Color.White.copy(alpha = 0.35f)
                val accent = when (tab) {
                    AppTab.Muzyka -> MaterialTheme.colorScheme.secondary
                    AppTab.Poisk -> MaterialTheme.colorScheme.primary
                }
                val targetUnderline =
                    if (selected) accent
                    else accent.copy(alpha = 0f)
                val textColor by animateColorAsState(targetTextColor, tween(220), label = "tab-text")
                val underlineColor by animateColorAsState(targetUnderline, tween(260), label = "tab-underline")
                Column(
                    modifier = Modifier
                        .padding(end = 24.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .clickable(enabled = !selected) { onSelect(tab) },
                ) {
                    Text(
                        text = tab.title,
                        color = textColor,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Black,
                        letterSpacing = (-0.5).sp,
                    )
                    Box(
                        Modifier
                            .padding(top = 5.dp)
                            .height(3.dp)
                            .width(28.dp)
                            .clip(RoundedCornerShape(2.dp))
                            .background(underlineColor),
                    )
                }
            }
        }
    }
}
