package com.demuzyka.tv.data

/**
 * Minimal model classes for the TV scaffold. Mirrors the relevant bits of
 * the mobile module's data layer; lift these (and the real providers) into a
 * shared `:data` library when you wire up a real backend.
 */

data class TvTrack(val id: String, val title: String, val artist: String)
data class TvPlaylist(val id: String, val title: String, val subtitle: String = "Плейлист")
data class TvMood(val id: String, val title: String)
data class TvFilm(val id: String, val title: String, val year: Int?, val genre: List<String>, val rating: Float? = null)

val SAMPLE_TRACKS = listOf(
    TvTrack("t1", "Пообещай мне", "ТРАВМА, SODA LUV"),
    TvTrack("t2", "Товарищ песня", "Михаил Гаврилов"),
    TvTrack("t3", "Твои слезы", "Azzi"),
    TvTrack("t4", "Ночь и день", "morphy"),
    TvTrack("t5", "Силуэты", "ROCKET, elya"),
    TvTrack("t6", "Sayonara детка", "MORGENSHTERN"),
    TvTrack("t7", "Зима в сердце", "Скриптонит"),
    TvTrack("t8", "Свет", "Хаски"),
)

val SAMPLE_PLAYLISTS = listOf(
    TvPlaylist("p1", "Тренды недели", "Хиты сейчас"),
    TvPlaylist("p2", "Русский рэп 2026", "Подборка"),
    TvPlaylist("p3", "Лоу-фай для кода", "Для работы"),
    TvPlaylist("p4", "Меланхолия", "Под настроение"),
    TvPlaylist("p5", "Поп без ботокса", "Чистый поп"),
    TvPlaylist("p6", "Под кофе", "Утро"),
)

val SAMPLE_MOODS = listOf(
    TvMood("liked", "Любимое"),
    TvMood("chill", "Спокойное"),
    TvMood("workout", "Бодрое"),
    TvMood("focus", "Фокус"),
    TvMood("party", "Танцевальное"),
    TvMood("evening", "Вечернее"),
)

val SAMPLE_FILMS = listOf(
    TvFilm("f1", "Спасти бессмертного", 2026, listOf("боевик"), 7.4f),
    TvFilm("f2", "Сеструха", 2025, listOf("комедия"), 7.2f),
    TvFilm("f3", "Кузя. Путь к успеху", 2025, listOf("сериал", "комедия"), 7.9f),
    TvFilm("f4", "Беспринципные", 2024, listOf("сериал", "драма"), 7.7f),
    TvFilm("f5", "Универ. 15 лет спустя", 2025, listOf("сериал"), 8.1f),
    TvFilm("f6", "Своя в доску", 2025, listOf("комедия"), 7.8f),
    TvFilm("f7", "На деревню дедушке", 2025, listOf("драма"), 7.7f),
    TvFilm("f8", "Моя собака — космонавт", 2025, listOf("семейное"), 8.2f),
)
