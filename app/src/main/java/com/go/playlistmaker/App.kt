package com.go.playlistmaker

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate

class App : Application() {

    companion object {
        const val LIGHT_THEME_KEY = "light"
        const val DARK_THEME_KEY = "dark"
    }

    var darkTheme = false
        private set

    override fun onCreate() {
        super.onCreate()

        Creator.initApplication(this)

        val sharedPreferences = Creator.provideSharedPreferences()
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

        Creator.provideSharedPreferences().edit()
            .putString(LIGHT_THEME_KEY, if (darkThemeEnabled) DARK_THEME_KEY else LIGHT_THEME_KEY)
            .apply()
    }
}