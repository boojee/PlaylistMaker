package com.go.playlistmaker.di

import com.go.playlistmaker.audioplayer.ui.AudioPlayerViewModel
import com.go.playlistmaker.searchtrack.ui.TrackSearchViewModel
import com.go.playlistmaker.settings.ui.SettingsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {

    viewModel {
        TrackSearchViewModel(trackInteractor = get())
    }

    viewModel {
        SettingsViewModel(settingsInteractor = get())
    }

    viewModel {
        AudioPlayerViewModel(audioPlayerInteractor = get())
    }

}