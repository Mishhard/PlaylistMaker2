package com.example.playlistmaker.domain.api

import com.example.playlistmaker.domain.models.Settings

interface SaveSettingsInteractor {
    fun execute(settings: Settings)
}
