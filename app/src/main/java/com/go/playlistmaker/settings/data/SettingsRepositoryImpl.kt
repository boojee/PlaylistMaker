package com.go.playlistmaker.settings.data

import android.content.Context
import android.content.SharedPreferences
import com.go.playlistmaker.App
import com.go.playlistmaker.settings.domain.api.SettingsRepository

class SettingsRepositoryImpl(private val context: Context) : SettingsRepository {

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("app_preferences", Context.MODE_PRIVATE)

    override fun setTheme(darkThemeEnabled: Boolean) {
        sharedPreferences.edit()
            .putString(
                App.THEME_KEY,
                if (darkThemeEnabled) App.DARK_THEME_KEY else App.LIGHT_THEME_KEY
            )
            .apply()

        (context.applicationContext as App).switchTheme(darkThemeEnabled)
    }

    override fun getCurrentTheme(): Boolean {
        val savedTheme = sharedPreferences.getString(App.THEME_KEY, null)
        return when (savedTheme) {
            App.DARK_THEME_KEY -> true
            App.LIGHT_THEME_KEY -> false
            else -> (context.applicationContext as App).isSystemDarkTheme()
        }
    }
}