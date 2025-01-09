package com.go.playlistmaker.domain.impl

import com.go.playlistmaker.domain.api.SettingsInteractor
import com.go.playlistmaker.domain.api.SettingsRepository

class SettingsInteractorImpl(
    private val settingsRepository: SettingsRepository
) : SettingsInteractor {

    override fun switchTheme(darkThemeEnabled: Boolean) {
        settingsRepository.setTheme(darkThemeEnabled)
    }
}