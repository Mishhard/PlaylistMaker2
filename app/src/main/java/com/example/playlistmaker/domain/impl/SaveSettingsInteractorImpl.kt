package com.example.playlistmaker.domain.impl

import com.example.playlistmaker.settings.domain.SaveSettingsInteractor
import com.example.playlistmaker.settings.domain.SettingsRepository
import com.example.playlistmaker.domain.models.Settings

class SaveSettingsInteractorImpl(
    private val settingsRepo: SettingsRepository
) : SaveSettingsInteractor {
    override fun execute(settings: Settings) {
        settingsRepo.saveSettings(settings)
    }
}
