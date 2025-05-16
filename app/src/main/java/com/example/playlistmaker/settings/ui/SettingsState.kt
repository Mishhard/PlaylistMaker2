package com.example.playlistmaker.settings.ui

sealed class SettingsState {
    object Loading : SettingsState()
    data class Content(val darkThemeEnabled: Boolean) : SettingsState()
}