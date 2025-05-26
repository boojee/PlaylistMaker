package com.go.playlistmaker.createplaylist.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.go.playlistmaker.createplaylist.domain.api.CreatePlaylistInteractor
import com.go.playlistmaker.playlists.data.db.Playlist
import kotlinx.coroutines.launch

class CreatePlaylistViewModel(
    private val createPlaylistInteractor: CreatePlaylistInteractor
) : ViewModel() {

    fun insertPlaylist(playlist: Playlist) {
        viewModelScope.launch {
            createPlaylistInteractor.insertPlaylist(playlist)
        }
    }
}