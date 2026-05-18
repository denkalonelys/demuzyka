package com.demuzyka.app.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.core.tween
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import com.demuzyka.app.DeMuzykaApp
import com.demuzyka.app.ui.theme.DeMuzykaTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        // 1) Hand-off from the Android-12 splash. Hold it 350ms so the
        //    orange logo briefly sits on-screen, then fade it into the app.
        val splash = installSplashScreen()
        var keepSplash = true
        splash.setKeepOnScreenCondition { keepSplash }

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        WindowCompat.setDecorFitsSystemWindows(window, false)

        val container = (application as DeMuzykaApp).container
        setContent {
            DeMuzykaTheme {
                var ready by remember { mutableStateOf(false) }
                LaunchedEffect(Unit) {
                    kotlinx.coroutines.delay(350)
                    keepSplash = false
                    ready = true
                }
                // Entry animation — content fades up after the splash dissolves.
                AnimatedVisibility(
                    visible = ready,
                    enter = fadeIn(tween(300)) +
                        slideInVertically(tween(400)) { it / 30 },
                    exit = fadeOut(),
                ) {
                    DeMuzykaRoot(container = container)
                }
            }
        }
    }
}
