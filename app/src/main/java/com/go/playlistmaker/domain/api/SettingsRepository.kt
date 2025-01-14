package com.go.playlistmaker.domain.api

interface SettingsRepository {
    fun setTheme(darkThemeEnabled: Boolean)
}