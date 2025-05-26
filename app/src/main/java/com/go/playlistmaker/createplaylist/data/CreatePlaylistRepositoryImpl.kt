package com.go.playlistmaker.createplaylist.data

import com.go.playlistmaker.createplaylist.domain.api.CreatePlaylistRepository
import com.go.playlistmaker.playlists.data.db.Playlist
import com.go.playlistmaker.playlists.data.db.PlaylistDao

class CreatePlaylistRepositoryImpl(private val playlistDao: PlaylistDao): CreatePlaylistRepository {
    override suspend fun insertPlaylist(playlist: Playlist) {
        playlistDao.insert(playlist)
    }
}