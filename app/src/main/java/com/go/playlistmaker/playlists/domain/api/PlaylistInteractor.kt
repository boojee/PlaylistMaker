package com.go.playlistmaker.playlists.domain.api

import com.go.playlistmaker.playlists.domain.models.PlaylistDomain
import kotlinx.coroutines.flow.Flow

interface PlaylistInteractor {

    fun getPlaylist(): Flow<List<PlaylistDomain>>
}