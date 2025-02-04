package com.go.playlistmaker

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.content.res.Configuration
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatDelegate
import com.go.playlistmaker.common.SEARCH_HISTORY_KEY
import com.go.playlistmaker.audioplayer.data.AudioPlayerRepositoryImpl
import com.go.playlistmaker.settings.data.SettingsRepositoryImpl
import com.go.playlistmaker.audioplayer.domain.api.AudioPlayerInteractor
import com.go.playlistmaker.audioplayer.domain.api.AudioPlayerRepository
import com.go.playlistmaker.settings.domain.api.SettingsInteractor
import com.go.playlistmaker.settings.domain.api.SettingsRepository
import com.go.playlistmaker.audioplayer.domain.impl.AudioPlayerInteractorImpl
import com.go.playlistmaker.settings.domain.impl.SettingsInteractorImpl
import com.go.playlistmaker.searchtrack.data.SearchHistory
import com.go.playlistmaker.searchtrack.data.TrackRepositoryImpl
import com.go.playlistmaker.searchtrack.data.localdata.SearchHistoryImpl
import com.go.playlistmaker.searchtrack.data.network.ItunesRetrofit
import com.go.playlistmaker.searchtrack.domain.api.TrackInteractor
import com.go.playlistmaker.searchtrack.domain.api.TrackRepository
import com.go.playlistmaker.searchtrack.domain.impl.TrackInteractorImpl

class App : Application() {

    companion object {
        const val THEME_KEY = "theme_key"
        const val LIGHT_THEME_KEY = "light"
        const val DARK_THEME_KEY = "dark"
    }

    var darkTheme = false
        private set

    override fun onCreate() {
        super.onCreate()

        val sharedPreferences = getSharedPreferences("app_preferences", Context.MODE_PRIVATE)
        val savedTheme = sharedPreferences.getString(THEME_KEY, null)

        darkTheme = when (savedTheme) {
            DARK_THEME_KEY -> true
            LIGHT_THEME_KEY -> false
            null -> isSystemDarkTheme()
            else -> isSystemDarkTheme()
        }

        switchTheme(darkTheme)
    }

    fun switchTheme(darkThemeEnabled: Boolean) {
        darkTheme = darkThemeEnabled
        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnabled) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }

    fun provideTrackInteractor(): TrackInteractor {
        return TrackInteractorImpl(getTrackRepository())
    }

    fun provideSettingsInteractor(): SettingsInteractor {
        return SettingsInteractorImpl(getSettingsRepository())
    }

    private fun getSettingsRepository(): SettingsRepository {
        return SettingsRepositoryImpl(this)
    }

    private fun getTrackRepository(): TrackRepository {
        return TrackRepositoryImpl(
            ItunesRetrofit(this), getSearchHistory(
                getSharedPreferences(
                    SEARCH_HISTORY_KEY, Context.MODE_PRIVATE
                )
            )
        )
    }

    private fun getSearchHistory(sharedPreferences: SharedPreferences): SearchHistory {
        return SearchHistoryImpl(sharedPreferences)
    }

    fun provideAudioPlayerInteractor(): AudioPlayerInteractor {
        return AudioPlayerInteractorImpl(getAudioPlayerRepository())
    }

    private fun getAudioPlayerRepository(): AudioPlayerRepository {
        return AudioPlayerRepositoryImpl(MediaPlayer())
    }

    fun isSystemDarkTheme(): Boolean {
        return (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES
    }
}