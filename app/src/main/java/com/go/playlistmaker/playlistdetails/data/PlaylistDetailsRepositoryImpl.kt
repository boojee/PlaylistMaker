package com.go.playlistmaker.playlistdetails.data

import com.go.playlistmaker.playlistdetails.data.db.Track
import com.go.playlistmaker.playlistdetails.domain.api.PlaylistDetailsRepository
import com.go.playlistmaker.playlists.data.db.Playlist
import com.go.playlistmaker.playlists.data.db.PlaylistDao
import kotlinx.coroutines.flow.Flow

class PlaylistDetailsRepositoryImpl(private val playlistDao: PlaylistDao) :
    PlaylistDetailsRepository {
    override fun getPlaylistById(playlistId: Long): Flow<Playlist> {
        return playlistDao.getPlaylistById(playlistId)
    }

    override suspend fun deleteTrackFromPlaylist(
        playlistId: Long,
        trackId: Int
    ): Flow<List<Track>> {
        return playlistDao.deleteTrackFromPlaylist(playlistId, trackId)
    }

    override fun getAllTracksForPlaylist(trackIds: List<Long>): Flow<List<Track>> {
        return playlistDao.getTracksByIds(trackIds)
    }

    override suspend fun updatePlaylist(playlist: Playlist) {
        playlistDao.update(playlist)
    }

    override suspend fun deletePlaylistAndUnusedTracks(playlistId: Long) {
        playlistDao.deletePlaylistAndUnusedTracks(playlistId)
    }
}