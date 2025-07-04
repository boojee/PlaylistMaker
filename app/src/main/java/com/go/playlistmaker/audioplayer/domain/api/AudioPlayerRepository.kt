package com.go.playlistmaker.audioplayer.domain.api

import com.go.playlistmaker.playlistdetails.data.db.Track
import com.go.playlistmaker.playlists.data.db.Playlist
import kotlinx.coroutines.flow.Flow

interface AudioPlayerRepository {
    fun preparePlayer(previewUrl: String, onCompletion: () -> Unit)
    fun startPlayer(callback: TimeUpdateCallback)
    fun pausePlayer()
    fun release()
    suspend fun updatePlaylistTrackIds(playlistId: Long, trackId: Int)
    suspend fun checkContainsTrackIdInPlaylist(playlistId: Long, trackId: Long): Flow<Boolean>
    fun getPlaylist(): Flow<List<Playlist>>
    suspend fun insertTrack(track: Track)

    interface TimeUpdateCallback {
        fun updatePlaybackTime(time: String)
    }
}