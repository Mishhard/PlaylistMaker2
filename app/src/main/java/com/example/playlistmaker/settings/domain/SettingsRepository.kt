package com.example.playlistmaker.settings.domain

import com.example.playlistmaker.domain.models.Settings

interface SettingsRepository {
    fun getSettings(): Settings
    fun saveSettings(settings: Settings)
}
