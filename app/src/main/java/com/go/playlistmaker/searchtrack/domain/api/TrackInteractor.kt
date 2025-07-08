package com.go.playlistmaker.searchtrack.domain.api

import com.go.playlistmaker.searchtrack.domain.models.TrackDomain
import kotlinx.coroutines.flow.Flow

interface TrackInteractor {
    fun findMusic(expression: String): Flow<Pair<List<TrackDomain>?, String?>>
    fun findMusicHistory(): Flow<List<TrackDomain>>
    suspend fun addMusicHistory(trackDomain: TrackDomain)
    fun clearHistory()
}