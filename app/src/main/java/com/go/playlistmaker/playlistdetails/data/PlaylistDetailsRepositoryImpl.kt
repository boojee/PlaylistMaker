package com.go.playlistmaker.playlistdetails.data

import com.go.playlistmaker.playlistdetails.domain.api.PlaylistDetailsRepository
import com.go.playlistmaker.playlists.data.db.Playlist
import com.go.playlistmaker.playlists.data.db.PlaylistDao
import com.go.playlistmaker.searchtrack.data.mappers.TrackMapper
import com.go.playlistmaker.searchtrack.domain.models.TrackDomain
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class PlaylistDetailsRepositoryImpl(private val playlistDao: PlaylistDao) :
    PlaylistDetailsRepository {
    override fun getPlaylistById(playlistId: Long): Flow<Playlist> {
        return playlistDao.getPlaylistById(playlistId)
    }

    override suspend fun deleteTrackFromPlaylist(
        playlistId: Long,
        trackId: Int
    ): Flow<List<TrackDomain>> =
        playlistDao.deleteTrackFromPlaylist(playlistId, trackId).map { tracks ->
            tracks.map {
                TrackMapper.toTrack(it)
            }
        }

    override fun getAllTracksForPlaylist(trackIds: List<Long>): Flow<List<TrackDomain>> =
        playlistDao.getTracksByIds(trackIds).map { tracks ->
            tracks.map {
                TrackMapper.toTrack(it)
            }
        }

    override suspend fun updatePlaylist(playlist: Playlist) {
        playlistDao.update(playlist)
    }

    override suspend fun deletePlaylistAndUnusedTracks(playlistId: Long) {
        playlistDao.deletePlaylistAndUnusedTracks(playlistId)
    }
}