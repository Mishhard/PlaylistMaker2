package com.example.playlistmaker.settings.domain.api

import com.example.playlistmaker.domain.models.Settings

interface GetSettingsInteractor {
    fun execute(): Settings
}