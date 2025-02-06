package com.go.playlistmaker

import com.go.playlistmaker.searchtrack.data.network.ItunesApi
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object Creator {
    fun provideItunesApi(): ItunesApi {
        return Retrofit.Builder()
            .baseUrl("https://itunes.apple.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ItunesApi::class.java)
    }
}