package com.example.playlistmaker.common

import android.app.Application
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate
import com.example.playlistmaker.di.dataModule
import com.example.playlistmaker.di.dbModule
import com.example.playlistmaker.di.domainModule
import com.example.playlistmaker.di.playlistModule
import com.example.playlistmaker.di.repositoryModule
import com.example.playlistmaker.di.viewModelModule
import org.koin.core.context.startKoin
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger

const val PLAYLISTMAKER_PREFERENCES = "playlistmaker_preferences"
const val APP_THEME_KEY = "key_for_app_theme"


class App : Application() {
    var darkTheme = false
    private lateinit var sharedPrefs: SharedPreferences

    override fun onCreate() {
        super.onCreate()
        sharedPrefs = getSharedPreferences(PLAYLISTMAKER_PREFERENCES, MODE_PRIVATE)

        startKoin {
            androidContext(this@App)
            modules(listOf(dataModule, domainModule, viewModelModule, repositoryModule, dbModule, playlistModule))
        }

        darkTheme = sharedPrefs.getBoolean(APP_THEME_KEY, false)
        switchTheme(darkTheme)
    }

    fun switchTheme(darkThemeEnabled: Boolean) {
        if (darkTheme != darkThemeEnabled) {
            darkTheme = darkThemeEnabled
            sharedPrefs.edit().putBoolean(APP_THEME_KEY, darkThemeEnabled).apply()
        }

        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnabled) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }
}