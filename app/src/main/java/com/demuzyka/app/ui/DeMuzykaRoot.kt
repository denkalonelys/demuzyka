package com.demuzyka.app.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.demuzyka.app.data.AppContainer
import com.demuzyka.app.ui.nav.BottomBar
import com.demuzyka.app.ui.nav.Section
import com.demuzyka.app.ui.music.MusicHomeScreen
import com.demuzyka.app.ui.music.MusicCollectionScreen
import com.demuzyka.app.ui.music.MusicConcertsScreen
import com.demuzyka.app.ui.music.MusicBooksScreen
import com.demuzyka.app.ui.player.MiniPlayerHost
import com.demuzyka.app.ui.poisk.PoiskHomeScreen
import com.demuzyka.app.ui.poisk.PoiskMyScreen
import com.demuzyka.app.ui.poisk.PoiskSearchScreen
import com.demuzyka.app.ui.tabs.AppTab
import com.demuzyka.app.ui.tabs.AppTabRow

/**
 * Root scaffold.
 *
 * Top: two big tabs — «ДеМузыка» / «ДеПоиск» (same app, two product
 * experiences just like Yandex.Plus combines Music + Kinopoisk).
 * Each tab keeps its own bottom nav state so switching back to a tab
 * restores the section the user left.
 */
@Composable
fun DeMuzykaRoot(container: AppContainer) {
    val musicNav = rememberNavController()
    val poiskNav = rememberNavController()

    val tabState = androidx.compose.runtime.remember { androidx.compose.runtime.mutableStateOf(AppTab.Muzyka) }
    val currentTab = tabState.value

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            AppTabRow(
                current = currentTab,
                onSelect = { tabState.value = it },
            )
        },
        bottomBar = {
            // Each tab gets its OWN bottom bar — Kinopoisk's "Главное / Медиа /
            // Моё / Поиск" vs Yandex Music's "Музыка / Концерты / Книги / Лайки".
            when (currentTab) {
                AppTab.Muzyka -> BottomBar(
                    navController = musicNav,
                    sections = MusicSections,
                )
                AppTab.Poisk -> BottomBar(
                    navController = poiskNav,
                    sections = PoiskSections,
                )
            }
        },
    ) { inner ->
        Box(Modifier.fillMaxSize().padding(inner)) {
            when (currentTab) {
                AppTab.Muzyka -> MusicNavHost(navController = musicNav, container = container)
                AppTab.Poisk -> PoiskNavHost(navController = poiskNav, container = container)
            }
            // The currently-playing track ribbon is rendered ABOVE the bottom
            // bar — exactly like Yandex Music. Hide it on Poisk for clarity.
            if (currentTab == AppTab.Muzyka) {
                MiniPlayerHost(
                    container = container,
                    modifier = Modifier.fillMaxSize(),
                )
            }
        }
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
        // "Медиа" reuses the home grid for now — wire to live channels later.
        composable(PoiskSections[1].route) { PoiskHomeScreen(container) }
        composable(PoiskSections[2].route) { PoiskMyScreen(container) }
        composable(PoiskSections[3].route) { PoiskSearchScreen(container) }
    }
}

private val MusicSections = listOf(
    Section.Music.Wave,
    Section.Music.Concerts,
    Section.Music.Books,
    Section.Music.Likes,
)

private val PoiskSections = listOf(
    Section.Poisk.Home,
    Section.Poisk.Media,
    Section.Poisk.My,
    Section.Poisk.Search,
)
