package com.go.playlistmaker.settings.domain.api

interface SettingsInteractor {
    fun switchTheme(darkThemeEnabled: Boolean)
    fun getCurrentTheme(): Boolean
}