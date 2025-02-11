package com.go.playlistmaker.di

import android.content.Context
import android.media.MediaPlayer
import com.go.playlistmaker.searchtrack.data.NetworkClient
import com.go.playlistmaker.searchtrack.data.SearchHistory
import com.go.playlistmaker.searchtrack.data.localdata.SearchHistoryImpl
import com.go.playlistmaker.searchtrack.data.network.ItunesApi
import com.go.playlistmaker.searchtrack.data.network.ItunesRetrofit
import com.google.gson.Gson
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val dataModule = module {

    single<ItunesApi> {
        Retrofit.Builder()
            .baseUrl("https://itunes.apple.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ItunesApi::class.java)
    }

    single {
        androidContext()
            .getSharedPreferences("local_storage", Context.MODE_PRIVATE)
    }

    factory { Gson() }

    single<SearchHistory> {
        SearchHistoryImpl(sharedPreferences = get())
    }

    single<NetworkClient> {
        ItunesRetrofit(context = androidContext(), itunesApi = get())
    }

    factory {
        MediaPlayer()
    }
}