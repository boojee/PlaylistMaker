package com.go.playlistmaker.di

import com.go.playlistmaker.audioplayer.data.AudioPlayerRepositoryImpl
import com.go.playlistmaker.audioplayer.domain.api.AudioPlayerRepository
import com.go.playlistmaker.searchtrack.data.TrackRepositoryImpl
import com.go.playlistmaker.searchtrack.domain.api.TrackRepository
import com.go.playlistmaker.settings.data.SettingsRepositoryImpl
import com.go.playlistmaker.settings.domain.api.SettingsRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import org.koin.dsl.module

val repositoryModule = module {

    single<TrackRepository> {
        TrackRepositoryImpl(networkClient = get(), searchHistory = get())
    }

    single<SettingsRepository> {
        SettingsRepositoryImpl(context = get())
    }

    factory<AudioPlayerRepository> {
        AudioPlayerRepositoryImpl(
            mediaPlayer = get(),
            scope = CoroutineScope(Dispatchers.IO)
        )
    }
}