package com.example.playlistmaker.settings.domain.api

import com.example.playlistmaker.settings.domain.models.Settings

interface GetSettingsInteractor {
    fun execute(): Settings
}