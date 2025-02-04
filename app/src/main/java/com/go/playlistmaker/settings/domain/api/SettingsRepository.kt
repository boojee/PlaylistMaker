package com.go.playlistmaker.settings.domain.api

interface SettingsRepository {
    fun setTheme(darkThemeEnabled: Boolean)
    fun getCurrentTheme(): Boolean
}