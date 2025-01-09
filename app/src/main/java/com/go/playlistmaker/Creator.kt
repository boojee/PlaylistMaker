package com.go.playlistmaker

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.go.playlistmaker.common.PLAYLIST_MAKER_PREFERENCES
import com.go.playlistmaker.common.SEARCH_HISTORY_KEY
import com.go.playlistmaker.data.AudioPlayerRepositoryImpl
import com.go.playlistmaker.data.SettingsRepositoryImpl
import com.go.playlistmaker.data.TrackRepositoryImpl
import com.go.playlistmaker.data.localdata.SearchHistoryImpl
import com.go.playlistmaker.data.network.ItunesRetrofit
import com.go.playlistmaker.domain.api.AudioPlayerInteractor
import com.go.playlistmaker.domain.api.AudioPlayerRepository
import com.go.playlistmaker.data.SearchHistory
import com.go.playlistmaker.domain.api.SettingsInteractor
import com.go.playlistmaker.domain.api.SettingsRepository
import com.go.playlistmaker.domain.api.TrackInteractor
import com.go.playlistmaker.domain.api.TrackRepository
import com.go.playlistmaker.domain.impl.AudioPlayerInteractorImpl
import com.go.playlistmaker.domain.impl.SettingsInteractorImpl
import com.go.playlistmaker.domain.impl.TrackInteractorImpl

object Creator {
    private lateinit var application: Application

    private fun getTrackRepository(): TrackRepository {
        return TrackRepositoryImpl(
            ItunesRetrofit(), getSearchHistory(
                application.getSharedPreferences(
                    SEARCH_HISTORY_KEY, Context.MODE_PRIVATE
                )
            )
        )
    }

    private fun getSettingsRepository(): SettingsRepository {
        return SettingsRepositoryImpl(application)
    }

    private fun getAudioPlayerRepository(): AudioPlayerRepository {
        return AudioPlayerRepositoryImpl()
    }

    fun provideTrackInteractor(): TrackInteractor {
        return TrackInteractorImpl(getTrackRepository())
    }

    fun provideAudioPlayerInteractor(): AudioPlayerInteractor {
        return AudioPlayerInteractorImpl(getAudioPlayerRepository())
    }

    fun provideSettingsInteractor(): SettingsInteractor {
        return SettingsInteractorImpl(getSettingsRepository())
    }

    fun initApplication(application: Application) {
        this.application = application
    }

    fun provideSharedPreferences(): SharedPreferences {
        return application.getSharedPreferences(PLAYLIST_MAKER_PREFERENCES, Context.MODE_PRIVATE)
    }

    fun getSearchHistory(sharedPreferences: SharedPreferences): SearchHistory {
        return SearchHistoryImpl(sharedPreferences)
    }
}