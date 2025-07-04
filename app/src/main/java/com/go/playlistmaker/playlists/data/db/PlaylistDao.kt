package com.go.playlistmaker.playlists.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.go.playlistmaker.playlistdetails.data.db.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
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
        val playlist = getPlaylistById(playlistId).first()
        val currentTrackIds = playlist.playlistTrackIds.toMutableList()

        if (!currentTrackIds.contains(trackId)) {
            currentTrackIds.add(trackId)
        }

        val tracks = getTracksByIds(currentTrackIds.map { it.toLong() }).first()
        val durationSum = calculateTotalDuration(tracks)

        val updatedPlaylist = playlist.copy(
            playlistTrackIds = currentTrackIds,
            playlistTracksCount = currentTrackIds.size,
            playlistTrackTiming = durationSum
        )

        update(updatedPlaylist)
    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTrack(track: Track)

    @Query("SELECT * FROM track WHERE trackId IN (:ids)")
    fun getTracksByIds(ids: List<Long>): Flow<List<Track>>

    @Query("SELECT * FROM playlist WHERE playlistId = :playlistId")
    fun getPlaylistById(playlistId: Long): Flow<Playlist>

    suspend fun checkContainsTrackIdInPlaylist(playlistId: Long, trackId: Long): Flow<Boolean> =
        getPlaylistById(playlistId).map { playlist ->
            playlist.playlistTrackIds.contains(trackId.toInt())
        }

    @Query("DELETE FROM playlist WHERE playlistId = :playlistId")
    suspend fun deletePlaylistById(playlistId: Long)

    suspend fun deleteTrackFromPlaylist(playlistId: Long, trackId: Int): Flow<List<Track>> {
        return getPlaylistById(playlistId).flatMapLatest { playlist ->
            val currentTrackIds = playlist.playlistTrackIds.toMutableList()
            currentTrackIds.remove(trackId)

            val updatedTracks = if (currentTrackIds.isNotEmpty()) {
                getTracksByIds(currentTrackIds.map { it.toLong() }).first()
            } else {
                emptyList()
            }

            val durationSum = calculateTotalDuration(updatedTracks)

            val updatedPlaylist = playlist.copy(
                playlistTrackIds = currentTrackIds,
                playlistTracksCount = currentTrackIds.size,
                playlistTrackTiming = durationSum
            )

            update(updatedPlaylist)

            if (currentTrackIds.isEmpty()) {
                flow { emit(emptyList()) }
            } else {
                val trackIds = currentTrackIds.map { trackId -> trackId.toLong() }
                getTracksByIds(trackIds)
            }
        }
    }

    private fun calculateTotalDuration(tracks: List<Track>): Int {
        if (tracks.isEmpty()) return 0

        val totalSeconds = tracks.sumOf { track ->
            parseDurationToSeconds(track.trackTimeMillis)
        }

        return (totalSeconds + 59) / 60
    }

    private fun parseDurationToSeconds(durationString: String): Int {
        return try {
            val parts = durationString.split(":")
            when (parts.size) {
                2 -> {
                    val minutes = parts[0].toInt()
                    val seconds = parts[1].toInt()
                    minutes * 60 + seconds
                }

                3 -> {
                    val hours = parts[0].toInt()
                    val minutes = parts[1].toInt()
                    val seconds = parts[2].toInt()
                    hours * 3600 + minutes * 60 + seconds
                }

                else -> 0
            }
        } catch (e: Exception) {
            0
        }
    }

    @Transaction
    suspend fun deletePlaylistAndUnusedTracks(playlistId: Long) {
        val playlistToDelete = getPlaylistById(playlistId).first()
        val tracksToCheck = playlistToDelete.playlistTrackIds.map { it.toLong() }

        deletePlaylistById(playlistId)

        val allPlaylists = getPlaylist().first()

        tracksToCheck.forEach { trackId ->
            val isUsedInOtherPlaylists = allPlaylists.any { playlist ->
                playlist.playlistId != playlistId && trackId.toInt() in playlist.playlistTrackIds
            }

            if (!isUsedInOtherPlaylists) {
                deleteTrackById(trackId)
            }
        }
    }

    @Query("DELETE FROM track WHERE trackId = :trackId")
    suspend fun deleteTrackById(trackId: Long)
}