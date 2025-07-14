package com.go.playlistmaker.searchtrack.data

import com.go.playlistmaker.searchtrack.domain.models.TrackDomain
import kotlinx.coroutines.flow.Flow

interface SearchHistory {
    suspend fun addTrack(trackDomain: TrackDomain)
    fun getHistory(): Flow<List<TrackDomain>>
    fun clearHistory()
}