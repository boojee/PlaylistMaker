package com.go.playlistmaker.createplaylist.domain.impl

import com.go.playlistmaker.createplaylist.domain.api.CreatePlaylistInteractor
import com.go.playlistmaker.createplaylist.domain.api.CreatePlaylistRepository
import com.go.playlistmaker.playlists.data.db.Playlist

class CreatePlaylistInteractorImpl(private val createPlaylistRepository: CreatePlaylistRepository) :
    CreatePlaylistInteractor {

    override suspend fun insertPlaylist(playlist: Playlist) {
        createPlaylistRepository.insertPlaylist(playlist)
    }
}