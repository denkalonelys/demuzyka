package com.demuzyka.app.data.music

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

data class Track(
    val id: String,
    val title: String,
    val artist: String,
    val coverUrl: String?,
    val durationSec: Int,
    /** Direct streamable URL (HLS / progressive). Null until a provider resolves it. */
    val streamUrl: String? = null,
    val isLiked: Boolean = false,
)

data class Playlist(
    val id: String,
    val title: String,
    val coverUrl: String?,
    val tracks: List<Track>,
)

data class ConcertCard(
    val id: String,
    val title: String,
    val artist: String,
    val dateText: String,           // "22 мая · пт"
    val venue: String,              // "Москва · VK Stadium"
    val ageRating: String = "12+",
    val priceFromRub: Int? = null,
    val posterUrl: String? = null,
)

data class AudioBookCard(
    val id: String,
    val title: String,
    val author: String,
    val coverUrl: String?,
    val durationMin: Int,
)

/**
 * Music catalog provider. **All methods MUST be safe to call on the main
 * thread** — return cold Flows / suspend in your impl.
 *
 * To plug in a real source:
 *  1. Implement this interface (e.g. `YourCdnMusicProvider`).
 *  2. Swap [com.demuzyka.app.data.DefaultAppContainer.musicProvider].
 *  3. Keep the demo `StubMusicProvider` around — it's used by `@Preview`.
 */
interface MusicProvider {
    /** "Мне нравится" — user's liked tracks, persisted locally. */
    val likes: Flow<List<Track>>

    /** Playlists shown on the home screen ("История", "Моя полка", etc.). */
    fun homeRows(): Flow<List<HomeRow>>

    /** Upcoming concert cards (right-tab "Концерты"). */
    fun concerts(): Flow<List<ConcertCard>>

    /** "Книги и подкасты" tab. */
    fun books(): Flow<List<AudioBookCard>>

    /** Trigger a like toggle. UI is optimistic; provider persists. */
    suspend fun toggleLike(trackId: String)

    /** Currently-playing track, updated by the playback bridge. */
    val nowPlaying: MutableStateFlow<NowPlaying?>
}

/** One horizontally-scrolling section on the music home screen. */
data class HomeRow(
    val title: String,             // "Сериалы на основе ваших интересов", etc.
    val kind: Kind,
    val items: List<Item>,
) {
    enum class Kind { Playlists, Tracks, Books, Mood }
    sealed class Item {
        data class PlaylistItem(val playlist: Playlist) : Item()
        data class TrackItem(val track: Track) : Item()
        data class BookItem(val book: AudioBookCard) : Item()
        data class MoodItem(val moodId: String, val title: String, val coverUrl: String?) : Item()
    }
}

data class NowPlaying(
    val track: Track,
    val positionSec: Int,
    val isPlaying: Boolean,
    /** Source tag — "wave", "liked", playlistId, … — used by the mini-player. */
    val source: String,
)
