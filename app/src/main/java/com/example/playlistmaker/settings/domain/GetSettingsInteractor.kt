package com.example.playlistmaker.settings.domain

import com.example.playlistmaker.domain.models.Settings

interface GetSettingsInteractor {
    fun execute(): Settings
}