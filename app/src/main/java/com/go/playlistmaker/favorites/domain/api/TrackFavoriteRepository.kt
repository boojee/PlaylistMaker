package com.go.playlistmaker.favorites.domain.api

import com.go.playlistmaker.searchtrack.domain.models.TrackDomain
import kotlinx.coroutines.flow.Flow

interface TrackFavoriteRepository {

    suspend fun insert(item: TrackDomain)

    suspend fun delete(item: TrackDomain)

    fun getAllItem(): Flow<List<TrackDomain>>

    fun getAllItemId(): Flow<List<Long>>
}