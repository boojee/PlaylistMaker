package com.go.playlistmaker.playlists.domain.api

import com.go.playlistmaker.playlists.data.db.Playlist
import kotlinx.coroutines.flow.Flow

interface PlaylistRepository {

    fun getPlaylist(): Flow<List<Playlist>>
}