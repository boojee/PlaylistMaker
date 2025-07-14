package com.go.playlistmaker.di

import com.go.playlistmaker.audioplayer.domain.api.AudioPlayerInteractor
import com.go.playlistmaker.audioplayer.domain.impl.AudioPlayerInteractorImpl
import com.go.playlistmaker.createplaylist.domain.api.CreatePlaylistInteractor
import com.go.playlistmaker.createplaylist.domain.impl.CreatePlaylistInteractorImpl
import com.go.playlistmaker.favorites.domain.api.TrackFavoriteInteractor
import com.go.playlistmaker.favorites.domain.impl.TrackFavoriteInteractorImpl
import com.go.playlistmaker.playlistdetails.domain.api.PlaylistDetailsInteractor
import com.go.playlistmaker.playlistdetails.domain.impl.PlaylistDetailsInteractorImpl
import com.go.playlistmaker.playlists.domain.api.PlaylistInteractor
import com.go.playlistmaker.playlists.domain.impl.PlaylistInteractorImpl
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

    factory<TrackFavoriteInteractor> {
        TrackFavoriteInteractorImpl(trackFavoriteRepository = get())
    }

    factory<CreatePlaylistInteractor> {
        CreatePlaylistInteractorImpl(createPlaylistRepository = get())
    }

    factory<PlaylistInteractor> {
        PlaylistInteractorImpl(playlistRepository = get())
    }

    factory<PlaylistDetailsInteractor> {
        PlaylistDetailsInteractorImpl(playlistDetailsRepository = get())
    }
}