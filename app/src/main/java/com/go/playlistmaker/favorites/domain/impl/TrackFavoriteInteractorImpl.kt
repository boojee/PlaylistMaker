package com.go.playlistmaker.favorites.domain.impl

import com.go.playlistmaker.favorites.domain.api.TrackFavoriteInteractor
import com.go.playlistmaker.favorites.domain.api.TrackFavoriteRepository
import com.go.playlistmaker.searchtrack.domain.models.TrackDomain
import kotlinx.coroutines.flow.Flow

class TrackFavoriteInteractorImpl(private val trackFavoriteRepository: TrackFavoriteRepository) :
    TrackFavoriteInteractor {

    override suspend fun insert(item: TrackDomain) {
        trackFavoriteRepository.insert(item)
    }

    override suspend fun delete(item: TrackDomain) {
        trackFavoriteRepository.delete(item)
    }

    override fun getAllItem(): Flow<List<TrackDomain>> {
        return trackFavoriteRepository.getAllItem()
    }

    override fun getAllItemId(): Flow<List<Long>> {
        return trackFavoriteRepository.getAllItemId()
    }
}