package com.go.playlistmaker.playlists.data

import com.go.playlistmaker.playlists.data.db.Playlist
import com.go.playlistmaker.playlists.data.db.PlaylistDao
import com.go.playlistmaker.playlists.domain.api.PlaylistRepository
import kotlinx.coroutines.flow.Flow

class PlaylistRepositoryImpl(private val playlistDao: PlaylistDao) : PlaylistRepository {

    override fun getPlaylist(): Flow<List<Playlist>> = playlistDao.getPlaylist()
}