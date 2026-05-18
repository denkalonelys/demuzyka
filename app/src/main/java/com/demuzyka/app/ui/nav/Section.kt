package com.demuzyka.app.ui.nav

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Bookmark
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.LocalActivity
import androidx.compose.material.icons.outlined.MenuBook
import androidx.compose.material.icons.outlined.MusicNote
import androidx.compose.material.icons.outlined.PlayCircleOutline
import androidx.compose.material.icons.outlined.Search
import androidx.compose.ui.graphics.vector.ImageVector

/** One bottom-bar entry: route + label + icon. */
sealed class Section(
    val route: String,
    val label: String,
    val icon: ImageVector,
) {
    object Music {
        // Yandex Music bottom bar: «Музыка / Концерты / Книги / Лайки».
        val Wave = object : Section("music/wave", "Музыка", Icons.Outlined.MusicNote) {}
        val Concerts = object : Section("music/concerts", "Концерты", Icons.Outlined.LocalActivity) {}
        val Books = object : Section("music/books", "Книги", Icons.Outlined.MenuBook) {}
        val Likes = object : Section("music/likes", "Лайки", Icons.Outlined.Favorite) {}
    }

    object Poisk {
        // Kinopoisk bottom bar: «Главное / Медиа / Моё / Поиск».
        val Home = object : Section("poisk/home", "Главное", Icons.Outlined.Home) {}
        val Media = object : Section("poisk/media", "Медиа", Icons.Outlined.PlayCircleOutline) {}
        val My = object : Section("poisk/my", "Моё", Icons.Outlined.Bookmark) {}
        val Search = object : Section("poisk/search", "Поиск", Icons.Outlined.Search) {}
    }
}
