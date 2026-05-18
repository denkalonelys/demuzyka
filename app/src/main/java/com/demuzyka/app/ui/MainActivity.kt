package com.demuzyka.app.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.view.WindowCompat
import com.demuzyka.app.DeMuzykaApp
import com.demuzyka.app.ui.theme.DeMuzykaTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Edge-to-edge under transparent status / nav bars — matches both
        // Yandex Music and Kinopoisk reference UIs.
        enableEdgeToEdge()
        WindowCompat.setDecorFitsSystemWindows(window, false)

        val container = (application as DeMuzykaApp).container
        setContent {
            DeMuzykaTheme {
                DeMuzykaRoot(container = container)
            }
        }
    }
}
