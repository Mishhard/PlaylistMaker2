package com.example.playlistmaker.domain.impl

import com.example.playlistmaker.settings.domain.GetSettingsInteractor
import com.example.playlistmaker.settings.domain.SettingsRepository

class GetSettingsInteractorImpl(
    private val repo: SettingsRepository
) : GetSettingsInteractor {
    override fun execute() = repo.getSettings()
}