package com.go.playlistmaker.settings.ui

sealed class SettingsState {
    data class CurrentTheme(val isThemeDark: Boolean): SettingsState()
}