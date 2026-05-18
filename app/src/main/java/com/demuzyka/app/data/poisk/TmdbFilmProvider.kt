package com.demuzyka.app.data.poisk

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.update
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import okhttp3.OkHttpClient
import okhttp3.Request
import java.util.concurrent.TimeUnit

/**
 * Example [FilmProvider] backed by [The Movie Database](https://www.themoviedb.org/)
 * — a legitimate, free-to-use film/TV catalog with Russian metadata.
 *
 * Why TMDB and not Lordfilm / Kinopoisk-scraping:
 *  • Lordfilm is a piracy site — embedding it in your app puts you at risk of
 *    Play Store removal and copyright takedowns.
 *  • TMDB has an official, free API and ships posters / ratings / synopses for
 *    Russian releases out of the box.
 *  • Kinopoisk's official API (`kinopoiskapiunofficial.tech`) is a great
 *    swap-in if you want exact KP ratings — the structure of this class is
 *    almost identical.
 *
 * Plug it in by editing [com.demuzyka.app.data.AppContainer]:
 * ```
 * override val filmProvider: FilmProvider = TmdbFilmProvider(apiKey = "YOUR_KEY")
 * ```
 * Get your free key at https://www.themoviedb.org/settings/api .
 *
 * Streaming (`resolveStream`) is intentionally `null` — TMDB does not host
 * video. Plug your own CDN / DRM-aware backend in once you have rights.
 */
class TmdbFilmProvider(
    private val apiKey: String,
    private val language: String = "ru-RU",
    private val region: String = "RU",
) : FilmProvider {

    private val http = OkHttpClient.Builder()
        .connectTimeout(10, TimeUnit.SECONDS)
        .readTimeout(15, TimeUnit.SECONDS)
        .build()

    private val json = Json { ignoreUnknownKeys = true; coerceInputValues = true }
    private val bookmarksState = MutableStateFlow<List<Film>>(emptyList())

    override fun featured(): Flow<List<FilmFeatured>> = flow {
        val nowPlaying = get("/movie/now_playing", limit = 5)
        emit(
            nowPlaying.map { f ->
                FilmFeatured(film = f, tagline = "Сейчас в кино", buttonText = "Купить билеты")
            }
        )
    }.flowOn(Dispatchers.IO)

    override fun homeRows(): Flow<List<FilmRow>> = flow {
        val popular = get("/movie/popular", limit = 20)
        val topRated = get("/movie/top_rated", limit = 20)
        val nowPlaying = get("/movie/now_playing", limit = 20)
        val series = get("/tv/popular", limit = 20)
        emit(
            listOf(
                FilmRow(id = "rec", title = "Советуем посмотреть", items = popular),
                FilmRow(id = "cinema", title = "Смотрите в кино", items = nowPlaying, cinema = true),
                FilmRow(id = "tv", title = "Сериалы", items = series),
                FilmRow(id = "top", title = "Топ-250 по версии TMDB", items = topRated),
            )
        )
    }.flowOn(Dispatchers.IO)

    override fun bookmarks(): Flow<List<Film>> = bookmarksState.asStateFlow()

    override fun search(query: String): Flow<List<Film>> = flow {
        if (query.isBlank()) {
            emit(emptyList())
            return@flow
        }
        val path = "/search/multi?query=${query.encode()}"
        emit(get(path, limit = 30))
    }.flowOn(Dispatchers.IO)

    override suspend fun toggleBookmark(filmId: String) {
        bookmarksState.update { current ->
            if (current.any { it.id == filmId }) current.filterNot { it.id == filmId } else current
        }
    }

    /** TMDB serves metadata, not video. Bring your own player backend. */
    override suspend fun resolveStream(filmId: String): String? = null

    // ── HTTP plumbing ─────────────────────────────────────────────────────

    private fun get(path: String, limit: Int): List<Film> {
        val sep = if ("?" in path) "&" else "?"
        val url = "https://api.themoviedb.org/3$path" +
            "${sep}api_key=$apiKey&language=$language&region=$region"
        val req = Request.Builder().url(url).get().build()
        return http.newCall(req).execute().use { resp ->
            if (!resp.isSuccessful) return emptyList()
            val body = resp.body?.string().orEmpty()
            try {
                json.decodeFromString(PageDto.serializer(), body).results
                    .take(limit)
                    .map { it.toFilm() }
            } catch (e: SerializationException) {
                emptyList()
            }
        }
    }

    private fun String.encode(): String =
        java.net.URLEncoder.encode(this, Charsets.UTF_8.name())

    @Serializable
    private data class PageDto(val results: List<ResultDto> = emptyList())

    @Serializable
    private data class ResultDto(
        val id: Int = 0,
        val title: String? = null,
        val name: String? = null,
        val original_title: String? = null,
        val original_name: String? = null,
        val release_date: String? = null,
        val first_air_date: String? = null,
        val vote_average: Double? = null,
        val poster_path: String? = null,
        val backdrop_path: String? = null,
        val overview: String? = null,
        val genre_ids: List<Int> = emptyList(),
    ) {
        fun toFilm(): Film {
            val displayTitle = title ?: name ?: "—"
            val origTitle = original_title ?: original_name
            val year = (release_date ?: first_air_date)?.take(4)?.toIntOrNull()
            return Film(
                id = id.toString(),
                title = displayTitle,
                originalTitle = origTitle.takeUnless { it == displayTitle },
                year = year,
                genres = genre_ids.mapNotNull(GENRE_MAP::get),
                rating = vote_average?.toFloat(),
                ageRating = null,
                posterUrl = poster_path?.let { "https://image.tmdb.org/t/p/w500$it" },
                backdropUrl = backdrop_path?.let { "https://image.tmdb.org/t/p/w1280$it" },
                description = overview.orEmpty(),
                inCinemas = first_air_date == null && release_date != null,
                newEpisodes = if (first_air_date != null) 0 else null,
                streamUrl = null,
            )
        }
    }

    private companion object {
        // Subset of TMDB genre IDs → Russian labels. Full list:
        // https://developer.themoviedb.org/reference/genre-movie-list
        val GENRE_MAP = mapOf(
            28 to "боевик", 12 to "приключения", 16 to "мультфильм",
            35 to "комедия", 80 to "криминал", 99 to "документальный",
            18 to "драма", 10751 to "семейный", 14 to "фэнтези",
            36 to "история", 27 to "ужасы", 10402 to "музыкальный",
            9648 to "детектив", 10749 to "мелодрама", 878 to "фантастика",
            10770 to "ТВ-фильм", 53 to "триллер", 10752 to "военный",
            37 to "вестерн", 10759 to "приключенческий", 10762 to "детский",
            10763 to "новости", 10764 to "реалити", 10765 to "фантастика",
            10766 to "мыльная опера", 10767 to "ток-шоу", 10768 to "военный",
        )
    }
}
