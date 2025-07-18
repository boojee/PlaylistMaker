package com.go.playlistmaker.di

import com.go.playlistmaker.audioplayer.data.AudioPlayerRepositoryImpl
import com.go.playlistmaker.audioplayer.domain.api.AudioPlayerRepository
import com.go.playlistmaker.createplaylist.data.CreatePlaylistRepositoryImpl
import com.go.playlistmaker.createplaylist.domain.api.CreatePlaylistRepository
import com.go.playlistmaker.favorites.data.TrackFavoriteRepositoryImpl
import com.go.playlistmaker.favorites.domain.api.TrackFavoriteRepository
import com.go.playlistmaker.playlistdetails.data.PlaylistDetailsRepositoryImpl
import com.go.playlistmaker.playlistdetails.domain.api.PlaylistDetailsRepository
import com.go.playlistmaker.playlists.data.PlaylistRepositoryImpl
import com.go.playlistmaker.playlists.domain.api.PlaylistRepository
import com.go.playlistmaker.searchtrack.data.TrackRepositoryImpl
import com.go.playlistmaker.searchtrack.domain.api.TrackRepository
import com.go.playlistmaker.settings.data.SettingsRepositoryImpl
import com.go.playlistmaker.settings.domain.api.SettingsRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import org.koin.dsl.module

val repositoryModule = module {

    single<TrackRepository> {
        TrackRepositoryImpl(networkClient = get(), searchHistory = get(), trackFavoriteDao = get())
    }

    single<SettingsRepository> {
        SettingsRepositoryImpl(context = get())
    }

    factory<AudioPlayerRepository> {
        AudioPlayerRepositoryImpl(
            mediaPlayer = get(),
            scope = CoroutineScope(Dispatchers.IO),
            playlistDao = get()
        )
    }

    factory<TrackFavoriteRepository> {
        TrackFavoriteRepositoryImpl(
            trackFavoriteDao = get()
        )
    }

    factory<CreatePlaylistRepository> {
        CreatePlaylistRepositoryImpl(
            playlistDao = get()
        )
    }

    factory<PlaylistRepository> {
        PlaylistRepositoryImpl(
            playlistDao = get()
        )
    }

    factory<PlaylistDetailsRepository> {
        PlaylistDetailsRepositoryImpl(
            playlistDao = get()
        )
    }
}