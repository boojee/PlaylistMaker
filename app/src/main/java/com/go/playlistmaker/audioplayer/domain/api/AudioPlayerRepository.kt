package com.go.playlistmaker.audioplayer.domain.api

import com.go.playlistmaker.playlists.domain.models.PlaylistDomain
import com.go.playlistmaker.searchtrack.domain.models.TrackDomain
import kotlinx.coroutines.flow.Flow

interface AudioPlayerRepository {
    fun preparePlayer(previewUrl: String, onCompletion: () -> Unit)
    fun startPlayer(callback: TimeUpdateCallback)
    fun pausePlayer()
    fun release()
    suspend fun updatePlaylistTrackIds(playlistId: Long, trackId: Int)
    suspend fun checkContainsTrackIdInPlaylist(playlistId: Long, trackId: Long): Flow<Boolean>
    fun getPlaylist(): Flow<List<PlaylistDomain>>
    suspend fun insertTrack(track: TrackDomain)

    interface TimeUpdateCallback {
        fun updatePlaybackTime(time: String)
    }
}