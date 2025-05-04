package com.example.playlistmaker.domain.impl

import com.example.playlistmaker.domain.api.SaveSettingsInteractor
import com.example.playlistmaker.domain.api.SettingsRepository
import com.example.playlistmaker.domain.models.Settings

class SaveSettingsInteractorImpl(
    private val settingsRepo: SettingsRepository
) : SaveSettingsInteractor {
    override fun execute(settings: Settings) {
        settingsRepo.saveSettings(settings)
    }
}
