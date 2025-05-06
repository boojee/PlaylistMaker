package com.go.playlistmaker.searchtrack.data

import com.go.playlistmaker.searchtrack.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface SearchHistory {
    suspend fun addTrack(track: Track)
    fun getHistory(): Flow<List<Track>>
    fun clearHistory()
}