package com.go.playlistmaker.favorites.data

import com.go.playlistmaker.favorites.data.db.TrackFavorite
import com.go.playlistmaker.favorites.data.db.TrackFavoriteDao
import com.go.playlistmaker.favorites.domain.api.TrackFavoriteRepository
import com.go.playlistmaker.searchtrack.data.mappers.TrackMapper
import com.go.playlistmaker.searchtrack.domain.models.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class TrackFavoriteRepositoryImpl(private val trackFavoriteDao: TrackFavoriteDao) :
    TrackFavoriteRepository {

    override suspend fun insert(item: TrackFavorite) {
        trackFavoriteDao.insert(item)
    }

    override suspend fun delete(item: TrackFavorite) {
        trackFavoriteDao.delete(item)
    }

    override fun getAllItem(): Flow<List<Track>> = trackFavoriteDao.getAllItems().map { items ->
        items.map { TrackMapper.trackFavoriteToTrack(it) }
    }

    override fun getAllItemId(): Flow<List<Long>> {
        return trackFavoriteDao.getAllItemsId()
    }
}