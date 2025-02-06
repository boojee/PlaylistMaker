package com.go.playlistmaker.settings.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.go.playlistmaker.App
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

    companion object {
        fun getViewModelFactory(): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val settingsInteractor = (this[APPLICATION_KEY] as App).provideSettingsInteractor()
                SettingsViewModel(settingsInteractor)
            }
        }
    }
}