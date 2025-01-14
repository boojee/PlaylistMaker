package com.go.playlistmaker.data.network

import com.go.playlistmaker.data.dto.TrackSearchResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ItunesApi {
    @GET("/search?entity=song")
    fun findMusic(@Query("term") text: String): Call<TrackSearchResponse>
}