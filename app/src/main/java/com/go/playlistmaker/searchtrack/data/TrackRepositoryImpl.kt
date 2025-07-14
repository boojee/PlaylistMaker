package com.go.playlistmaker.searchtrack.data

import com.go.playlistmaker.favorites.data.db.TrackFavoriteDao
import com.go.playlistmaker.searchtrack.data.dto.TrackSearchResponse
import com.go.playlistmaker.searchtrack.data.dto.TrackSearchRequest
import com.go.playlistmaker.searchtrack.data.mappers.TrackMapper
import com.go.playlistmaker.searchtrack.domain.api.TrackRepository
import com.go.playlistmaker.searchtrack.domain.models.TrackDomain
import com.go.playlistmaker.searchtrack.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow

class TrackRepositoryImpl(
    private val networkClient: NetworkClient,
    private val searchHistory: SearchHistory,
    private val trackFavoriteDao: TrackFavoriteDao
) : TrackRepository {
    override fun findMusic(expression: String): Flow<Resource<List<TrackDomain>>> = flow {
        val response = networkClient.doRequest(TrackSearchRequest(expression))
        when (response.resultCode) {
            -1 -> {
                emit(Resource.Error("Проверьте подключение к интернету"))
            }

            200 -> {
                val trackFavoriteList = trackFavoriteDao.getAllItemsId().first()
                emit(Resource.Success((response as TrackSearchResponse).results.map { trackDto ->
                    val track = trackDto.copy(isFavorite = trackFavoriteList.any { trackId ->
                        trackId == trackDto.trackId
                    })
                    TrackMapper.toTrack(track)
                }))
            }

            else -> {
                emit(Resource.Error("Ошибка сервера"))
            }
        }
    }

    override fun findMusicHistory(): Flow<List<TrackDomain>> = flow {
        searchHistory.getHistory().collect { tracksHistory ->
            val trackFavoriteList = trackFavoriteDao.getAllItemsId().first()
            emit(tracksHistory.map { trackHistory ->
                val track = trackHistory.copy(isFavorite = trackFavoriteList.any { trackId ->
                    trackId == trackHistory.trackId
                })
                track
            }
            )
        }
    }

    override suspend fun addMusicHistory(trackDomain: TrackDomain) {
        searchHistory.addTrack(trackDomain)
    }

    override fun clearHistory() {
        searchHistory.clearHistory()
    }
}