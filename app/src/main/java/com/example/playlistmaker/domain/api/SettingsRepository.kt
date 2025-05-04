package com.example.playlistmaker.domain.api

import com.example.playlistmaker.domain.models.Settings

interface SettingsRepository {
    fun getSettings(): Settings
    fun saveSettings(settings: Settings)
}
