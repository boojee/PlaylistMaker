package com.go.playlistmaker.searchtrack.data.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.go.playlistmaker.searchtrack.data.NetworkClient
import com.go.playlistmaker.searchtrack.data.dto.TrackSearchRequest
import com.go.playlistmaker.searchtrack.data.dto.Response

class ItunesRetrofit(
    private val context: Context,
    private val itunesApi: ItunesApi
) : NetworkClient {

    override fun doRequest(dto: Any): Response {
        if (!isConnected()) {
            return Response().apply { resultCode = -1 }
        }
        if (dto !is TrackSearchRequest) {
            return Response().apply { resultCode = 400 }
        }

        val resp = itunesApi.findMusic(dto.expression).execute()
        val body = resp.body() ?: Response()

        return body.apply { resultCode = resp.code() }
    }

    private fun isConnected(): Boolean {
        val connectivityManager = context.getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager
        val capabilities =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        if (capabilities != null) {
            when {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> return true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> return true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> return true
            }
        }
        return false
    }
}