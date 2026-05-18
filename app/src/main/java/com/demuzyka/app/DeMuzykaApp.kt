package com.demuzyka.app

import android.app.Application
import com.demuzyka.app.data.AppContainer
import com.demuzyka.app.data.DefaultAppContainer

/**
 * Application entry. Holds an [AppContainer] — a tiny hand-rolled DI graph
 * where you swap stub providers for real ones (Lordfilm, music CDN, etc.)
 * without touching UI code. See README → "Куда подключать данные".
 */
class DeMuzykaApp : Application() {
    lateinit var container: AppContainer
        private set

    override fun onCreate() {
        super.onCreate()
        container = DefaultAppContainer(this)
    }
}
