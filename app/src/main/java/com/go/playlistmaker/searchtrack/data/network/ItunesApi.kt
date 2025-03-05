package com.go.playlistmaker.searchtrack.data.network

import com.go.playlistmaker.searchtrack.data.dto.TrackSearchResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ItunesApi {
    @GET("/search?entity=song")
    suspend fun findMusic(@Query("term") text: String): TrackSearchResponse
}