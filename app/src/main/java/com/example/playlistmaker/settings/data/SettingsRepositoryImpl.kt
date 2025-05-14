package com.example.playlistmaker.settings.data

import android.content.Context
import android.content.SharedPreferences
import com.example.playlistmaker.settings.domain.api.SettingsRepository
import com.example.playlistmaker.settings.domain.models.Settings
import com.example.playlistmaker.common.PLAYLISTMAKER_PREFERENCES
import com.example.playlistmaker.common.APP_THEME_KEY

class SettingsRepositoryImpl(context: Context) : SettingsRepository {
    private val prefs: SharedPreferences =
        context.getSharedPreferences(PLAYLISTMAKER_PREFERENCES, Context.MODE_PRIVATE)

    override fun getSettings(): Settings =
        Settings(darkThemeEnabled = prefs.getBoolean(APP_THEME_KEY, false))

    override fun saveSettings(enabled: Boolean) {
        prefs.edit().putBoolean(APP_THEME_KEY, enabled).apply()
    }
}