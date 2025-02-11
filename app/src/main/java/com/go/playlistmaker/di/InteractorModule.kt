package com.go.playlistmaker.di

import com.go.playlistmaker.audioplayer.domain.api.AudioPlayerInteractor
import com.go.playlistmaker.audioplayer.domain.impl.AudioPlayerInteractorImpl
import com.go.playlistmaker.searchtrack.domain.api.TrackInteractor
import com.go.playlistmaker.searchtrack.domain.impl.TrackInteractorImpl
import com.go.playlistmaker.settings.domain.api.SettingsInteractor
import com.go.playlistmaker.settings.domain.impl.SettingsInteractorImpl
import org.koin.dsl.module

val interactorModule = module {

    single<TrackInteractor> {
        TrackInteractorImpl(trackRepository = get())
    }

    single<SettingsInteractor> {
        SettingsInteractorImpl(settingsRepository = get())
    }

    factory<AudioPlayerInteractor> {
        AudioPlayerInteractorImpl(audioPlayerRepository = get())
    }
}