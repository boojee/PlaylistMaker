package com.go.playlistmaker.favorites.domain.api

import com.go.playlistmaker.favorites.data.db.TrackFavorite
import com.go.playlistmaker.searchtrack.domain.models.TrackDomain
import kotlinx.coroutines.flow.Flow

interface TrackFavoriteRepository {

    suspend fun insert(item: TrackFavorite)

    suspend fun delete(item: TrackFavorite)

    fun getAllItem(): Flow<List<TrackDomain>>

    fun getAllItemId(): Flow<List<Long>>
}