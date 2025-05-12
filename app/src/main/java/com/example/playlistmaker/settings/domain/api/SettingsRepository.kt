package com.example.playlistmaker.settings.domain.api

import com.example.playlistmaker.domain.models.Settings

interface SettingsRepository {
    fun getSettings(): Settings
    fun saveSettings(enabled: Boolean)
}
