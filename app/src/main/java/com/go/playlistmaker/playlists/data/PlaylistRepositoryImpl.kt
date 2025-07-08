package com.go.playlistmaker.playlists.data

import com.go.playlistmaker.playlists.data.db.PlaylistDao
import com.go.playlistmaker.playlists.data.mappers.PlaylistMapper
import com.go.playlistmaker.playlists.domain.api.PlaylistRepository
import com.go.playlistmaker.playlists.domain.models.PlaylistDomain
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class PlaylistRepositoryImpl(private val playlistDao: PlaylistDao) : PlaylistRepository {

    override fun getPlaylist(): Flow<List<PlaylistDomain>> = playlistDao.getPlaylist().map { playlist ->
        playlist.map {
            PlaylistMapper.toPlaylist(it)
        }
    }
}