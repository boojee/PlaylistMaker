package com.go.playlistmaker.data

import com.go.playlistmaker.data.dto.TrackSearchResponse
import com.go.playlistmaker.data.dto.TrackSearchRequest
import com.go.playlistmaker.data.mappers.TrackMapper
import com.go.playlistmaker.domain.api.TrackRepository
import com.go.playlistmaker.domain.models.Track

class TrackRepositoryImpl(
    private val networkClient: NetworkClient,
    private val searchHistory: SearchHistory
) : TrackRepository {
    override fun findMusic(expression: String): List<Track> {
        val response = networkClient.doRequest(TrackSearchRequest(expression))
        return if (response.resultCode == 200) {
            (response as TrackSearchResponse).results.map {
                TrackMapper.toTrack(it)
            }
        } else {
            emptyList()
        }
    }

    override fun findMusicHistory(): List<Track> {
        return searchHistory.getHistory()
    }

    override fun addMusicHistory(track: Track) {
        searchHistory.addTrack(track)
    }

    override fun clearHistory() {
        searchHistory.clearHistory()
    }
}