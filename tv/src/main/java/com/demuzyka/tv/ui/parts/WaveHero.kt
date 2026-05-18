package com.demuzyka.tv.ui.parts

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.Text

@Composable
fun WaveHero() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(280.dp)
            .clip(RoundedCornerShape(24.dp))
            .background(
                Brush.horizontalGradient(
                    listOf(
                        Color(0xFFFF2D7F),
                        Color(0xFFFFCC00),
                    )
                )
            ),
        contentAlignment = Alignment.CenterStart,
    ) {
        Text(
            "Моя волна",
            modifier = Modifier.padding(48.dp),
            style = MaterialTheme.typography.displayLarge,
            color = Color.White,
        )
    }
}
