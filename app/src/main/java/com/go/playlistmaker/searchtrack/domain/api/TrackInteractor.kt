package com.go.playlistmaker.searchtrack.domain.api

import com.go.playlistmaker.searchtrack.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface TrackInteractor {
    fun findMusic(expression: String): Flow<Pair<List<Track>?, String?>>
    fun findMusicHistory(): Flow<List<Track>>
    suspend fun addMusicHistory(track: Track)
    fun clearHistory()
}