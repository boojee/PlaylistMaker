package com.go.playlistmaker.data.network

import com.go.playlistmaker.data.NetworkClient
import com.go.playlistmaker.data.dto.TrackSearchRequest
import com.go.playlistmaker.data.dto.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ItunesRetrofit : NetworkClient {
    private val itunesBaseUrl = "https://itunes.apple.com"

    private fun provideRetrofit(): ItunesApi = Retrofit.Builder()
        .baseUrl(itunesBaseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(ItunesApi::class.java)

    override fun doRequest(dto: Any): Response {
        if (dto is TrackSearchRequest) {
            val itunesApi = provideRetrofit()
            val resp = itunesApi.findMusic(dto.expression).execute()

            val body = resp.body() ?: Response()

            return body.apply { resultCode = resp.code() }
        } else {
            return Response().apply { resultCode = 400 }
        }
    }
}