package com.go.playlistmaker

import android.app.Application
import android.content.Context
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatDelegate
import com.go.playlistmaker.di.dataModule
import com.go.playlistmaker.di.interactorModule
import com.go.playlistmaker.di.repositoryModule
import com.go.playlistmaker.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

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

        startKoin {
            androidContext(this@App)
            modules(dataModule, repositoryModule, interactorModule, viewModelModule)
        }

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

    fun isSystemDarkTheme(): Boolean {
        return (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES
    }
}