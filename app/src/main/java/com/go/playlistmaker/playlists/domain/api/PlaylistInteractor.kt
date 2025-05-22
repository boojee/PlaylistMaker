package com.go.playlistmaker.playlists.domain.api

import com.go.playlistmaker.playlists.data.db.Playlist
import kotlinx.coroutines.flow.Flow

interface PlaylistInteractor {

    fun getPlaylist(): Flow<List<Playlist>>
}