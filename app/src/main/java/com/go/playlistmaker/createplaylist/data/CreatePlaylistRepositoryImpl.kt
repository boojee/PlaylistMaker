package com.go.playlistmaker.createplaylist.data

import com.go.playlistmaker.createplaylist.domain.api.CreatePlaylistRepository
import com.go.playlistmaker.playlists.data.db.PlaylistDao
import com.go.playlistmaker.playlists.data.mappers.PlaylistMapper
import com.go.playlistmaker.playlists.domain.models.PlaylistDomain

class CreatePlaylistRepositoryImpl(private val playlistDao: PlaylistDao): CreatePlaylistRepository {

    override suspend fun insertPlaylist(playlist: PlaylistDomain) {
        playlistDao.insert(PlaylistMapper.toPlaylistEntity(playlist))
    }
}