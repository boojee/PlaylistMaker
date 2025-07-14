package com.go.playlistmaker.searchtrack.domain.api

import com.go.playlistmaker.searchtrack.domain.models.TrackDomain
import com.go.playlistmaker.searchtrack.util.Resource
import kotlinx.coroutines.flow.Flow

interface TrackRepository {
    fun findMusic(expression: String): Flow<Resource<List<TrackDomain>>>
    fun findMusicHistory(): Flow<List<TrackDomain>>
    suspend fun addMusicHistory(trackDomain: TrackDomain)
    fun clearHistory()
}