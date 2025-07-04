package com.go.playlistmaker.playlistdetails.domain.api

import com.go.playlistmaker.playlistdetails.data.db.Track
import com.go.playlistmaker.playlists.data.db.Playlist
import kotlinx.coroutines.flow.Flow

interface PlaylistDetailsRepository {
    fun getPlaylistById(playlistId: Long): Flow<Playlist>
    suspend fun deleteTrackFromPlaylist(playlistId: Long, trackId: Int): Flow<List<Track>>
    fun getAllTracksForPlaylist(trackIds: List<Long>): Flow<List<Track>>
    suspend fun updatePlaylist(playlist: Playlist)
    suspend fun deletePlaylistAndUnusedTracks(playlistId: Long)
}