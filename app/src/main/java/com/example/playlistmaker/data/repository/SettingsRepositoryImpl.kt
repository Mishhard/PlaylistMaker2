package com.example.playlistmaker.data.storage

import android.content.Context
import android.content.SharedPreferences
import com.example.playlistmaker.domain.api.SettingsRepository
import com.example.playlistmaker.domain.models.Settings
import com.example.playlistmaker.PLAYLISTMAKER_PREFERENCES
import com.example.playlistmaker.APP_THEME_KEY

class SettingsRepositoryImpl(context: Context) : SettingsRepository {
    private val prefs: SharedPreferences =
        context.getSharedPreferences(PLAYLISTMAKER_PREFERENCES, Context.MODE_PRIVATE)

    override fun getSettings(): Settings =
        Settings(darkThemeEnabled = prefs.getBoolean(APP_THEME_KEY, false))

    override fun saveSettings(settings: Settings) {
        prefs.edit().putBoolean(APP_THEME_KEY, settings.darkThemeEnabled).apply()
    }
}