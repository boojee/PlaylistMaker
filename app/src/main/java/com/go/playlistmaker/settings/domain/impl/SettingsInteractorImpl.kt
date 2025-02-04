package com.go.playlistmaker.settings.domain.impl

import com.go.playlistmaker.settings.domain.api.SettingsInteractor
import com.go.playlistmaker.settings.domain.api.SettingsRepository

class SettingsInteractorImpl(
    private val settingsRepository: SettingsRepository
) : SettingsInteractor {

    override fun switchTheme(darkThemeEnabled: Boolean) {
        settingsRepository.setTheme(darkThemeEnabled)
    }

    override fun getCurrentTheme(): Boolean {
        return settingsRepository.getCurrentTheme()
    }
}