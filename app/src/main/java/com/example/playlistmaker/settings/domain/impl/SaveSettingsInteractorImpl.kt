package com.example.playlistmaker.settings.domain.impl

import com.example.playlistmaker.settings.domain.api.SaveSettingsInteractor
import com.example.playlistmaker.settings.domain.api.SettingsRepository

class SaveSettingsInteractorImpl(
    private val settingsRepo: SettingsRepository
) : SaveSettingsInteractor {
    override fun execute(settings: Boolean) {
        settingsRepo.saveSettings(settings)
    }
}
