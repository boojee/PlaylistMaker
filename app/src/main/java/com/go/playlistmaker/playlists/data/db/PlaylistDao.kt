package com.go.playlistmaker.playlists.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

@Dao
interface PlaylistDao {
    @Insert
    suspend fun insert(playlist: Playlist)

    @Query("SELECT * FROM playlist")
    fun getPlaylist(): Flow<List<Playlist>>

    @Update
    suspend fun update(playlist: Playlist)

    suspend fun updatePlaylistTrackIds(playlistId: Long, trackId: Int) {
        val playlist = getPlaylistById(playlistId)

        val currentTrackIds = playlist.playlistTrackIds.toMutableList()
        if (!currentTrackIds.contains(trackId)) {
            currentTrackIds.add(trackId)
        }

        val updatedPlaylist = playlist.copy(
            playlistTrackIds = currentTrackIds,
            playlistTracksCount = currentTrackIds.size
        )

        update(updatedPlaylist)
    }

    @Query("SELECT * FROM playlist WHERE playlistId = :playlistId")
    suspend fun getPlaylistById(playlistId: Long): Playlist

    suspend fun checkContainsTrackIdInPlaylist(playlistId: Long, trackId: Long): Flow<Boolean> =
        flow {
            val playlist = getPlaylistById(playlistId)
            val currentTrackIds = playlist.playlistTrackIds.toMutableList()
            emit(currentTrackIds.contains(trackId.toInt()))
        }
}