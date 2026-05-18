package com.demuzyka.app.data.music

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * Demo provider used by previews & by the running app until you wire a real
 * music backend. **Never touches the network** — all data is hard-coded.
 *
 * Cover URLs intentionally point at generic CDN-hosted placeholders so
 * Coil can render something even on an emulator without your CDN.
 */
class StubMusicProvider : MusicProvider {

    private val seedTracks = listOf(
        Track("t1", "Пообещай мне", "ТРАВМА, SODA LUV", null, 120, isLiked = true),
        Track("t2", "Товарищ песня", "Михаил Гаврилов", null, 188),
        Track("t3", "Твои слезы (Sped Up)", "Azzi", null, 142),
        Track("t4", "Я хотел чтобы ты горела", "vovi", null, 174),
        Track("t5", "Ночь и день", "morphy", null, 203),
        Track("t6", "Силуэты", "ROCKET, elya", null, 198),
    )

    private val likedTracks = MutableStateFlow(seedTracks.filter { it.isLiked })

    override val likes: Flow<List<Track>> = likedTracks.asStateFlow()

    // Start empty — mini-player slides up only after the user picks a track.
    override val nowPlaying: MutableStateFlow<NowPlaying?> = MutableStateFlow(null)

    fun play(track: Track) {
        nowPlaying.value = NowPlaying(track = track, positionSec = 0, isPlaying = true, source = "wave")
    }

    fun toggleNow() {
        val now = nowPlaying.value ?: return
        nowPlaying.value = now.copy(isPlaying = !now.isPlaying)
    }

    override fun homeRows(): Flow<List<HomeRow>> = MutableStateFlow(
        listOf(
            HomeRow(
                title = "Моя волна",
                kind = HomeRow.Kind.Mood,
                items = listOf(
                    HomeRow.Item.MoodItem("liked", "Любимое", null),
                    HomeRow.Item.MoodItem("chill", "Спокойное", null),
                    HomeRow.Item.MoodItem("workout", "Бодрое", null),
                    HomeRow.Item.MoodItem("focus", "Фокус", null),
                    HomeRow.Item.MoodItem("party", "Танцевальное", null),
                ),
            ),
            HomeRow(
                title = "Для вас",
                kind = HomeRow.Kind.Playlists,
                items = listOf(
                    HomeRow.Item.PlaylistItem(Playlist("p1", "vovi · КИССКОЛД", null, seedTracks)),
                    HomeRow.Item.PlaylistItem(Playlist("p2", "Тренды", null, seedTracks)),
                    HomeRow.Item.PlaylistItem(Playlist("p3", "Лоу-фай для кода", null, seedTracks)),
                ),
            ),
            HomeRow(
                title = "Мне нравится",
                kind = HomeRow.Kind.Tracks,
                items = seedTracks.filter { it.isLiked }.map { HomeRow.Item.TrackItem(it) },
            ),
        )
    )

    override fun concerts(): Flow<List<ConcertCard>> = MutableStateFlow(
        listOf(
            ConcertCard("c1", "Mary Gu", "Mary Gu", "22 мая · пт", "Москва · VK Stadium",
                ageRating = "12+", priceFromRub = 3300),
            ConcertCard("c2", "macan", "MACAN", "5 июня · сб", "СПб · СКА Арена",
                ageRating = "18+", priceFromRub = 4500),
            ConcertCard("c3", "Три дня дождя", "Три дня дождя", "14 июня · вс", "Москва · Adrenaline Stadium",
                ageRating = "16+", priceFromRub = 2800),
        )
    )

    override fun books(): Flow<List<AudioBookCard>> = MutableStateFlow(
        listOf(
            AudioBookCard("b1", "СЕСТРЫ БЛЭК. Жизнь Нарциссы", "АТМ Studio", null, 56),
            AudioBookCard("b2", "Однажды в Гарлеме", "Колсон Уайтхед", null, 720),
            AudioBookCard("b3", "Время и деньги", "Аудиокниги", null, 320),
        )
    )

    override suspend fun toggleLike(trackId: String) {
        val current = likedTracks.value
        val track = seedTracks.firstOrNull { it.id == trackId } ?: return
        likedTracks.value = if (current.any { it.id == trackId }) {
            current.filter { it.id != trackId }
        } else {
            current + track.copy(isLiked = true)
        }
    }
}
