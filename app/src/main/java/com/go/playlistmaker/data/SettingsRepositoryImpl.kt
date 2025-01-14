package com.go.playlistmaker.data

import android.content.Context
import com.go.playlistmaker.App
import com.go.playlistmaker.domain.api.SettingsRepository

class SettingsRepositoryImpl(private val context: Context) : SettingsRepository {

    override fun setTheme(darkThemeEnabled: Boolean) {
        val app = context.applicationContext as App
        app.switchTheme(darkThemeEnabled)
    }
}