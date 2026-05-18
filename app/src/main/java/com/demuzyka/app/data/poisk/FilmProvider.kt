package com.demuzyka.app.data.poisk

import kotlinx.coroutines.flow.Flow

data class Film(
    val id: String,
    val title: String,
    val originalTitle: String? = null,
    val year: Int? = null,
    val genres: List<String> = emptyList(),
    val rating: Float? = null,           // 0.0 .. 10.0
    val ageRating: String? = null,       // "12+", "0+", "18+"
    val posterUrl: String? = null,
    val backdropUrl: String? = null,
    val description: String? = null,
    /** True for content that's only in the cinema today ("В кино с 14 мая"). */
    val inCinemas: Boolean = false,
    /** Episodes badge ⚡10 — number of new episodes shown over the poster. */
    val newEpisodes: Int? = null,
    /** Direct streamable URL — resolved lazily by the provider. */
    val streamUrl: String? = null,
)

data class FilmRow(
    val id: String,
    val title: String,
    val items: List<Film>,
    /** When true, items render with the big "В кино" backdrop layout. */
    val cinema: Boolean = false,
)

data class FilmFeatured(
    val film: Film,
    val tagline: String,                 // headline text under the title
    val buttonText: String = "Купить билеты",
)

/**
 * Film catalog. Same plug-replace pattern as [com.demuzyka.app.data.music.MusicProvider]:
 * swap [com.demuzyka.app.data.DefaultAppContainer.filmProvider] for your own impl
 * (Lordfilm scraper, Kinopoisk REST, your own catalog…).
 */
interface FilmProvider {
    /** Big featured carousel at the top of "Главное". */
    fun featured(): Flow<List<FilmFeatured>>

    /** Horizontal rows on the home screen ("Советуем посмотреть", …). */
    fun homeRows(): Flow<List<FilmRow>>

    /** Bookmarked / saved films ("Буду смотреть"). */
    fun bookmarks(): Flow<List<Film>>

    /** Search query → results. Debounce upstream. */
    fun search(query: String): Flow<List<Film>>

    /** Toggle a bookmark. */
    suspend fun toggleBookmark(filmId: String)

    /**
     * Resolve a streamable URL on demand. Return value mutates the Film.
     * Heavy: do it only when the user actually presses Play.
     */
    suspend fun resolveStream(filmId: String): String?
}
