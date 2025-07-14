package com.go.playlistmaker.di

import com.go.playlistmaker.favorites.ui.FavoritesViewModel
import com.go.playlistmaker.playlists.ui.PlaylistsViewModel
import com.go.playlistmaker.audioplayer.ui.AudioPlayerViewModel
import com.go.playlistmaker.audioplayer.ui.playlistbottomsheet.PlaylistBottomSheetViewModel
import com.go.playlistmaker.createplaylist.ui.CreatePlaylistViewModel
import com.go.playlistmaker.playlistdetails.ui.PlaylistDetailsViewModel
import com.go.playlistmaker.searchtrack.ui.TrackSearchViewModel
import com.go.playlistmaker.settings.ui.SettingsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import com.go.playlistmaker.playlistdetails.ui.playlistoptionsbottomsheet.PlaylistOptionsBottomSheetViewModel

val viewModelModule = module {

    viewModel {
        TrackSearchViewModel(trackInteractor = get())
    }

    viewModel {
        SettingsViewModel(settingsInteractor = get())
    }

    viewModel {
        AudioPlayerViewModel(audioPlayerInteractor = get(), trackFavoriteInteractor = get())
    }

    viewModel {
        FavoritesViewModel(trackFavoriteInteractor = get())
    }

    viewModel {
        CreatePlaylistViewModel(createPlaylistInteractor = get(), playlistDetailsInteractor = get())
    }

    viewModel {
        PlaylistsViewModel(playlistInteractor = get())
    }

    viewModel {
        PlaylistBottomSheetViewModel(audioPlayerInteractor = get())
    }

    viewModel {
        PlaylistDetailsViewModel(playlistDetailsInteractor = get())
    }

    viewModel {
        PlaylistOptionsBottomSheetViewModel(playlistDetailsInteractor = get())
    }
}