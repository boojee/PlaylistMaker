package com.go.playlistmaker

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ItunesApi {
    @GET("/search?entity=song")
    fun findMusic(@Query("term") text: String): Call<TrackResponse>
}