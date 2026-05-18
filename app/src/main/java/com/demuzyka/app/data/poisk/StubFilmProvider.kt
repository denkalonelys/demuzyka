package com.demuzyka.app.data.poisk

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map

class StubFilmProvider : FilmProvider {

    private val catalog: List<Film> = listOf(
        Film("f1", "Спасти бессмертного", year = 2026, genres = listOf("боевик"),
            rating = 7.4f, ageRating = "12+", inCinemas = false),
        Film("f2", "Сеструха", year = 2025, genres = listOf("комедия"),
            rating = 7.2f, ageRating = "16+"),
        Film("f3", "Как Майк 2: Стритбол", year = 2007, genres = listOf("комедия"),
            rating = 5.7f, ageRating = "0+"),
        Film("f4", "Кузя. Путь к успеху", year = 2025, genres = listOf("сериал","комедия"),
            rating = 7.9f, ageRating = "16+", newEpisodes = 10),
        Film("f5", "Беспринципные", year = 2024, genres = listOf("сериал","драма"),
            rating = 7.7f, ageRating = "18+", newEpisodes = 10),
        Film("f6", "Универ. 15 лет спустя", year = 2025, genres = listOf("сериал"),
            rating = 8.1f, ageRating = "16+", newEpisodes = 10),
        Film("f7", "На деревню дедушке", year = 2025, genres = listOf("драма"),
            rating = 7.7f, ageRating = "12+"),
        Film("f8", "Новая тёща", year = 2025, genres = listOf("комедия"),
            rating = 7.2f, ageRating = "16+", newEpisodes = 10),
        Film("f9", "Моя собака — космонавт", year = 2025, genres = listOf("семейное"),
            rating = 8.2f, ageRating = "6+"),
        Film("f10", "Лео и Тиг. Дорога на Байкал", year = 2025, genres = listOf("мультфильм"),
            rating = 7.8f, ageRating = "0+", inCinemas = true),
        Film("f11", "Своя в доску", year = 2025, genres = listOf("комедия"),
            rating = 7.8f, ageRating = "16+", inCinemas = true),
        Film("f12", "Семь вёрст до рассвета", year = 2025, genres = listOf("военный"),
            rating = 7.8f, ageRating = "16+", inCinemas = true),
        Film("f13", "Великолепный Гай Ричи", year = 2024, genres = listOf("подборка"), rating = null),
    )

    private val bookmarks = MutableStateFlow<List<String>>(
        listOf("f1", "f2", "f3", "f4"),
    )

    override fun featured(): Flow<List<FilmFeatured>> = MutableStateFlow(
        listOf(
            FilmFeatured(
                film = Film(
                    id = "feat1",
                    title = "Грязные деньги",
                    description = "Джейк Джилленхол и Генри Кавилл в азартном экшене Гая Ричи.",
                ),
                tagline = "Джейк Джилленхол и Генри Кавилл в азартном экшене Гая Ричи. Билеты — на Кинопоиске!",
            ),
        )
    )

    override fun homeRows(): Flow<List<FilmRow>> = MutableStateFlow(
        listOf(
            FilmRow("r-recommend", "Советуем посмотреть",
                catalog.filter { it.id in listOf("f13", "f4", "f2") }),
            FilmRow("r-cinema", "Смотрите в кино",
                catalog.filter { it.inCinemas }, cinema = true),
            FilmRow("r-interests", "Сериалы на основе ваших интересов",
                catalog.filter { it.genres.contains("сериал") }),
            FilmRow("r-for-you", "Фильмы для вас",
                catalog.filter { it.id in listOf("f7", "f8", "f9") }),
        )
    )

    override fun bookmarks(): Flow<List<Film>> =
        bookmarks.map { ids -> ids.mapNotNull { id -> catalog.firstOrNull { it.id == id } } }

    override fun search(query: String): Flow<List<Film>> = MutableStateFlow(
        if (query.isBlank()) emptyList()
        else catalog.filter { it.title.contains(query, ignoreCase = true) }
    )

    override suspend fun toggleBookmark(filmId: String) {
        val current = bookmarks.value
        bookmarks.value = if (filmId in current) current - filmId else current + filmId
    }

    override suspend fun resolveStream(filmId: String): String? {
        // Stub: no real source. Replace with Lordfilm scraper / KP API resolver.
        return null
    }
}
