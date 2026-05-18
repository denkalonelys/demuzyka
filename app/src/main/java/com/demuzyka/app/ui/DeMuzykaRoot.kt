package com.demuzyka.app.ui

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.demuzyka.app.data.AppContainer
import com.demuzyka.app.ui.nav.BottomBar
import com.demuzyka.app.ui.music.MusicBooksScreen
import com.demuzyka.app.ui.music.MusicCollectionScreen
import com.demuzyka.app.ui.music.MusicConcertsScreen
import com.demuzyka.app.ui.music.MusicHomeScreen
import com.demuzyka.app.ui.player.FullPlayerSheet
import com.demuzyka.app.ui.player.MiniPlayerHost
import com.demuzyka.app.ui.poisk.PoiskHomeScreen
import com.demuzyka.app.ui.poisk.PoiskMyScreen
import com.demuzyka.app.ui.poisk.PoiskSearchScreen
import com.demuzyka.app.ui.tabs.AppTab
import com.demuzyka.app.ui.tabs.AppTabRow

/**
 * Root scaffold.
 *
 * Top: two big tabs — «ДеМузыка» / «ДеПоиск».
 * Each tab keeps its own nav stack so switching back restores state.
 * Mini-player lives inside Scaffold.bottomBar on the Muzyka tab, directly
 * above the BottomBar — like Yandex.Music. Tap mini-player to open the
 * full-screen now-playing sheet (slides up from the bottom).
 */
@Composable
fun DeMuzykaRoot(container: AppContainer) {
    val musicNav = rememberNavController()
    val poiskNav = rememberNavController()

    var currentTab by remember { mutableStateOf(AppTab.Muzyka) }
    var fullPlayerOpen by remember { mutableStateOf(false) }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            AppTabRow(
                current = currentTab,
                onSelect = { currentTab = it },
            )
        },
        bottomBar = {
            Column {
                if (currentTab == AppTab.Muzyka) {
                    MiniPlayerHost(
                        container = container,
                        onExpand = { fullPlayerOpen = true },
                    )
                }
                when (currentTab) {
                    AppTab.Muzyka -> BottomBar(navController = musicNav, sections = MusicSections)
                    AppTab.Poisk -> BottomBar(navController = poiskNav, sections = PoiskSections)
                }
            }
        },
    ) { inner ->
        Box(Modifier.fillMaxSize().padding(inner)) {
            AnimatedContent(
                targetState = currentTab,
                transitionSpec = {
                    val forward = targetState.ordinal > initialState.ordinal
                    val offset: (Int) -> Int = { full -> if (forward) full else -full }
                    val offsetReverse: (Int) -> Int = { full -> if (forward) -full else full }
                    (
                        fadeIn(tween(220)) +
                            slideInHorizontally(tween(280), initialOffsetX = offset)
                        ) togetherWith (
                        fadeOut(tween(180)) +
                            slideOutHorizontally(tween(280), targetOffsetX = offsetReverse)
                        )
                },
                label = "tab-switch",
            ) { tab ->
                when (tab) {
                    AppTab.Muzyka -> MusicNavHost(navController = musicNav, container = container)
                    AppTab.Poisk -> PoiskNavHost(navController = poiskNav, container = container)
                }
            }
        }
    }

    // Full-screen player sheet — overlays the whole scaffold.
    AnimatedVisibility(
        visible = fullPlayerOpen,
        enter = slideInVertically(tween(320)) { it } + fadeIn(tween(220)),
        exit = slideOutVertically(tween(280)) { it } + fadeOut(tween(180)),
    ) {
        FullPlayerSheet(container = container, onDismiss = { fullPlayerOpen = false })
    }
}

@Composable
private fun MusicNavHost(navController: androidx.navigation.NavHostController, container: AppContainer) {
    NavHost(navController = navController, startDestination = MusicSections.first().route) {
        composable(MusicSections[0].route) { MusicHomeScreen(container) }
        composable(MusicSections[1].route) { MusicConcertsScreen(container) }
        composable(MusicSections[2].route) { MusicBooksScreen(container) }
        composable(MusicSections[3].route) { MusicCollectionScreen(container) }
    }
}

@Composable
private fun PoiskNavHost(navController: androidx.navigation.NavHostController, container: AppContainer) {
    NavHost(navController = navController, startDestination = PoiskSections.first().route) {
        composable(PoiskSections[0].route) { PoiskHomeScreen(container) }
        composable(PoiskSections[1].route) { PoiskHomeScreen(container) } // media reuses home for now
        composable(PoiskSections[2].route) { PoiskMyScreen(container) }
        composable(PoiskSections[3].route) { PoiskSearchScreen(container) }
    }
}
