package com.go.playlistmaker.playlistdetails.domain.api

import com.go.playlistmaker.searchtrack.domain.models.TrackDomain
import com.go.playlistmaker.playlists.domain.models.PlaylistDomain
import kotlinx.coroutines.flow.Flow

interface PlaylistDetailsInteractor {
    fun getPlaylistById(playlistId: Long): Flow<PlaylistDomain>
    suspend fun deleteTrackFromPlaylist(playlistId: Long, trackId: Int): Flow<List<TrackDomain>>
    fun getAllTracksForPlaylist(trackIds: List<Long>): Flow<List<TrackDomain>>
    suspend fun updatePlaylist(playlist: PlaylistDomain)
    suspend fun deletePlaylistAndUnusedTracks(playlistId: Long)
}