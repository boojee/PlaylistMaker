package com.go.playlistmaker.favorites.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface TrackFavoriteDao {
    @Insert
    suspend fun insert(trackFavorite: TrackFavorite)

    @Delete
    suspend fun delete(trackFavorite: TrackFavorite)

    @Query("SELECT * FROM favoriteTracks ORDER BY createdAt DESC")
    fun getAllItems(): Flow<List<TrackFavorite>>

    @Query("SELECT trackId FROM favoriteTracks")
    fun getAllItemsId(): Flow<List<Long>>
}