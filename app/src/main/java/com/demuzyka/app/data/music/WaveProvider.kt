package com.demuzyka.app.data.music

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * "Моя волна" — endless personalised radio. The contract is intentionally
 * minimal so the UI can stay dumb: a single ordered queue that the wave
 * provider mutates as the user likes / dislikes / skips.
 *
 * To plug in real recommendations:
 *  * Implement [WaveProvider] with your model (collaborative filter,
 *    content-based, etc.).
 *  * Update [com.demuzyka.app.data.DefaultAppContainer.waveProvider].
 */
interface WaveProvider {
    /** Current ordered queue ahead of the listener. Head = playing. */
    val queue: Flow<List<Track>>

    /** Mood filter selected by the user (null = default). */
    val mood: Flow<String?>

    /** Set the mood — the queue will be regenerated lazily. */
    suspend fun setMood(moodId: String?)

    /** User skipped — drop the head, lazily refill the tail. */
    suspend fun skip()

    /** Like the currently-playing track — feeds back into recs. */
    suspend fun like(trackId: String)

    /** Dislike — never recommend this artist again until cleared. */
    suspend fun dislike(trackId: String)
}

class StubWaveProvider(private val library: MusicProvider) : WaveProvider {

    private val _mood = MutableStateFlow<String?>("liked")
    override val mood: Flow<String?> = _mood.asStateFlow()

    private val _queue = MutableStateFlow<List<Track>>(emptyList())
    override val queue: Flow<List<Track>> = _queue.asStateFlow()

    override suspend fun setMood(moodId: String?) {
        _mood.value = moodId
        regenerate()
    }

    override suspend fun skip() {
        _queue.value = _queue.value.drop(1).ifEmpty { regenerateInline() }
    }

    override suspend fun like(trackId: String) {
        library.toggleLike(trackId)
    }

    override suspend fun dislike(trackId: String) {
        // No-op in stub. Real impl would push a negative signal to the
        // recommendation model.
    }

    private fun regenerateInline(): List<Track> = _queue.value.also { _queue.value = it }
    private fun regenerate() {
        // Stub: just rotates the same demo set.
        _queue.value = _queue.value
    }
}
