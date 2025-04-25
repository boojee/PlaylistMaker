package com.go.playlistmaker.searchtrack.domain.api

import com.go.playlistmaker.searchtrack.domain.models.Track
import com.go.playlistmaker.searchtrack.util.Resource
import kotlinx.coroutines.flow.Flow

interface TrackRepository {
    fun findMusic(expression: String): Flow<Resource<List<Track>>>
    fun findMusicHistory(): Flow<List<Track>>
    suspend fun addMusicHistory(track: Track)
    fun clearHistory()
}