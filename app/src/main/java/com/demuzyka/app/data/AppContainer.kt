package com.demuzyka.app.data

import android.content.Context
import com.demuzyka.app.data.music.MusicProvider
import com.demuzyka.app.data.music.StubMusicProvider
import com.demuzyka.app.data.music.WaveProvider
import com.demuzyka.app.data.music.StubWaveProvider
import com.demuzyka.app.data.poisk.FilmProvider
import com.demuzyka.app.data.poisk.StubFilmProvider

/**
 * Hand-rolled DI graph. The four "knobs" you flip when going from
 * scaffold → working app live here. See README → "Куда подключать данные".
 *
 *  * [musicProvider]  — your music catalog (CDN, local lib, etc.).
 *  * [waveProvider]   — recommendation engine that drives "Моя волна".
 *  * [filmProvider]   — Kinopoisk-replacement catalog (Lordfilm, KP API, …).
 *  * [playbackBridge] — actual audio/video output (Media3 ExoPlayer here).
 */
interface AppContainer {
    val musicProvider: MusicProvider
    val waveProvider: WaveProvider
    val filmProvider: FilmProvider
}

class DefaultAppContainer(
    @Suppress("unused") private val context: Context,
) : AppContainer {
    // ────────────────────────────────────────────────────────────────────
    // Replace these constructors with real-network providers when ready.
    // The screens never see the concrete class; everything is the interface.
    // ────────────────────────────────────────────────────────────────────
    override val musicProvider: MusicProvider = StubMusicProvider()
    override val waveProvider: WaveProvider = StubWaveProvider(musicProvider)
    override val filmProvider: FilmProvider = StubFilmProvider()
}
