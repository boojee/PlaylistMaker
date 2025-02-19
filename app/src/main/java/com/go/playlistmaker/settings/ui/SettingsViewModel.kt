package com.go.playlistmaker.settings.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.go.playlistmaker.settings.domain.api.SettingsInteractor

class SettingsViewModel(private val settingsInteractor: SettingsInteractor) : ViewModel() {

    private val themeStateLiveData = MutableLiveData<SettingsState>()

    fun getThemeStateLiveData(): MutableLiveData<SettingsState> = themeStateLiveData

    init {
        themeStateLiveData.postValue(SettingsState.CurrentTheme(settingsInteractor.getCurrentTheme()))
    }

    fun switchTheme(darkThemeEnabled: Boolean) {
        settingsInteractor.switchTheme(darkThemeEnabled)
        themeStateLiveData.postValue(SettingsState.CurrentTheme(darkThemeEnabled))
    }
}