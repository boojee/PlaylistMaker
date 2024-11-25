package com.go.playlistmaker

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate

const val PLAYLIST_MAKER_PREFERENCES = "playlist_maker_preferences"
const val LIGHT_THEME_KEY = "light"
const val DARK_THEME_KEY = "dark"

class App : Application() {

    var darkTheme = false
        private set

    override fun onCreate() {
        super.onCreate()

        val sharedPreferences = getSharedPreferences(PLAYLIST_MAKER_PREFERENCES, MODE_PRIVATE)
        val savedTheme = sharedPreferences.getString(LIGHT_THEME_KEY, LIGHT_THEME_KEY)
        darkTheme = savedTheme == DARK_THEME_KEY
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

        getSharedPreferences(PLAYLIST_MAKER_PREFERENCES, MODE_PRIVATE).edit()
            .putString(LIGHT_THEME_KEY, if (darkThemeEnabled) DARK_THEME_KEY else LIGHT_THEME_KEY)
            .apply()
    }
}

