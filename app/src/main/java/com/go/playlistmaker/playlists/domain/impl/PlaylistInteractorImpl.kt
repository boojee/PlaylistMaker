package com.go.playlistmaker.playlists.domain.impl

import com.go.playlistmaker.playlists.domain.api.PlaylistInteractor
import com.go.playlistmaker.playlists.domain.api.PlaylistRepository
import com.go.playlistmaker.playlists.domain.models.PlaylistDomain
import kotlinx.coroutines.flow.Flow

class PlaylistInteractorImpl(private val playlistRepository: PlaylistRepository) :
    PlaylistInteractor {

    override fun getPlaylist(): Flow<List<PlaylistDomain>> {
        return playlistRepository.getPlaylist()
    }
}