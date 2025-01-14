package com.go.playlistmaker

import android.app.Application
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatDelegate

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

        Creator.initApplication(this)

        val sharedPreferences = Creator.provideSharedPreferences()
        val savedTheme = sharedPreferences.getString(THEME_KEY, null)

        darkTheme = if (savedTheme == null) {
            val isSystemDarkTheme =
                (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES
            sharedPreferences.edit()
                .putString(THEME_KEY, if (isSystemDarkTheme) DARK_THEME_KEY else LIGHT_THEME_KEY)
                .apply()
            isSystemDarkTheme
        } else {
            savedTheme == DARK_THEME_KEY
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

        Creator.provideSharedPreferences().edit()
            .putString(THEME_KEY, if (darkThemeEnabled) DARK_THEME_KEY else LIGHT_THEME_KEY)
            .apply()
    }
}