package com.go.playlistmaker

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ItunesRetrofit {
    private val itunesBaseUrl = "https://itunes.apple.com"

    fun provideRetrofit(): ItunesApi = Retrofit.Builder()
        .baseUrl(itunesBaseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(ItunesApi::class.java)
}