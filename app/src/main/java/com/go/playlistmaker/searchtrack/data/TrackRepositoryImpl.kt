package com.go.playlistmaker.searchtrack.data

import com.go.playlistmaker.searchtrack.data.dto.TrackSearchResponse
import com.go.playlistmaker.searchtrack.data.dto.TrackSearchRequest
import com.go.playlistmaker.searchtrack.data.mappers.TrackMapper
import com.go.playlistmaker.searchtrack.domain.api.TrackRepository
import com.go.playlistmaker.searchtrack.domain.models.Track
import com.go.playlistmaker.searchtrack.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class TrackRepositoryImpl(
    private val networkClient: NetworkClient,
    private val searchHistory: SearchHistory
) : TrackRepository {
    override fun findMusic(expression: String): Flow<Resource<List<Track>>> = flow {
        val response = networkClient.doRequest(TrackSearchRequest(expression))
        when (response.resultCode) {
            -1 -> {
                emit(Resource.Error("Проверьте подключение к интернету"))
            }

            200 -> {
                emit(Resource.Success((response as TrackSearchResponse).results.map {
                    TrackMapper.toTrack(it)
                }))
            }

            else -> {
                emit(Resource.Error("Ошибка сервера"))
            }
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