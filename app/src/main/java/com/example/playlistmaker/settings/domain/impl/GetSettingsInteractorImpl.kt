package com.example.playlistmaker.settings.domain.impl

import com.example.playlistmaker.settings.domain.api.GetSettingsInteractor
import com.example.playlistmaker.settings.domain.api.SettingsRepository

class GetSettingsInteractorImpl(
    private val repo: SettingsRepository
) : GetSettingsInteractor {
    override fun execute() = repo.getSettings()
}