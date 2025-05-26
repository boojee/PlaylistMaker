package com.go.playlistmaker.playlists.domain.impl

import com.go.playlistmaker.playlists.data.db.Playlist
import com.go.playlistmaker.playlists.domain.api.PlaylistInteractor
import com.go.playlistmaker.playlists.domain.api.PlaylistRepository
import kotlinx.coroutines.flow.Flow

class PlaylistInteractorImpl(private val playlistRepository: PlaylistRepository) :
    PlaylistInteractor {

    override fun getPlaylist(): Flow<List<Playlist>> {
        return playlistRepository.getPlaylist()
    }
}