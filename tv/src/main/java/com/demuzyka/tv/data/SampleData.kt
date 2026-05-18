package com.demuzyka.tv.data

/**
 * Minimal model classes for the TV scaffold. Mirrors the relevant bits of
 * the mobile module's data layer; lift these (and the real providers) into a
 * shared `:data` library when you wire up a real backend.
 */

data class TvTrack(val title: String, val artist: String)
data class TvPlaylist(val title: String)
data class TvFilm(val title: String, val year: Int?, val genre: List<String>, val rating: Float? = null)

val SAMPLE_TRACKS = listOf(
    TvTrack("Пообещай мне", "ТРАВМА, SODA LUV"),
    TvTrack("Товарищ песня", "Михаил Гаврилов"),
    TvTrack("Твои слезы", "Azzi"),
    TvTrack("Ночь и день", "morphy"),
    TvTrack("Силуэты", "ROCKET, elya"),
)

val SAMPLE_PLAYLISTS = listOf(
    TvPlaylist("Моя волна"),
    TvPlaylist("Любимое"),
    TvPlaylist("Спокойное"),
    TvPlaylist("Бодрое"),
    TvPlaylist("Танцевальное"),
)

val SAMPLE_FILMS = listOf(
    TvFilm("Спасти бессмертного", 2026, listOf("боевик"), 7.4f),
    TvFilm("Сеструха", 2025, listOf("комедия"), 7.2f),
    TvFilm("Кузя. Путь к успеху", 2025, listOf("сериал", "комедия"), 7.9f),
    TvFilm("Беспринципные", 2024, listOf("сериал", "драма"), 7.7f),
    TvFilm("Универ. 15 лет спустя", 2025, listOf("сериал"), 8.1f),
    TvFilm("Своя в доску", 2025, listOf("комедия"), 7.8f),
    TvFilm("На деревню дедушке", 2025, listOf("драма"), 7.7f),
    TvFilm("Моя собака — космонавт", 2025, listOf("семейное"), 8.2f),
)
