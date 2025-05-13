package com.example.playlistmaker.settings.domain.api

import com.example.playlistmaker.settings.domain.models.Settings

interface SettingsRepository {
    fun getSettings(): Settings
    fun saveSettings(enabled: Boolean)
}
