package com.example.playlistmaker.domain.impl

import com.example.playlistmaker.domain.api.GetSettingsInteractor
import com.example.playlistmaker.domain.api.SettingsRepository

class GetSettingsInteractorImpl(
    private val repo: SettingsRepository
) : GetSettingsInteractor {
    override fun execute() = repo.getSettings()
}