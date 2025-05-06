package com.go.playlistmaker.searchtrack.domain.impl

import com.go.playlistmaker.searchtrack.domain.api.TrackInteractor
import com.go.playlistmaker.searchtrack.domain.api.TrackRepository
import com.go.playlistmaker.searchtrack.domain.models.Track
import com.go.playlistmaker.searchtrack.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class TrackInteractorImpl(private val trackRepository: TrackRepository) : TrackInteractor {

    override fun findMusic(expression: String): Flow<Pair<List<Track>?, String?>> {
        return trackRepository.findMusic(expression).map { result ->
            when (result) {
                is Resource.Success -> {
                    Pair(result.data, null)
                }

                is Resource.Error -> {
                    Pair(null, result.message)
                }
            }
        }
    }

    override fun findMusicHistory(): Flow<List<Track>> {
        return trackRepository.findMusicHistory()
    }

    override suspend fun addMusicHistory(track: Track) {
        trackRepository.addMusicHistory(track)
    }

    override fun clearHistory() {
        trackRepository.clearHistory()
    }
}