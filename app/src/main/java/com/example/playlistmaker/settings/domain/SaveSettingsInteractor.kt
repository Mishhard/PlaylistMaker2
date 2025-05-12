package com.example.playlistmaker.settings.domain

import com.example.playlistmaker.domain.models.Settings

interface SaveSettingsInteractor {
    fun execute(settings: Settings)
}
