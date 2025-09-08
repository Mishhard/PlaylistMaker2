package com.example.playlistmaker.settings.ui

import androidx.lifecycle.*
import com.example.playlistmaker.settings.domain.api.GetSettingsInteractor
import com.example.playlistmaker.settings.domain.api.SaveSettingsInteractor
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val getSettingsInteractor: GetSettingsInteractor,
    private val saveSettingsInteractor: SaveSettingsInteractor
) : ViewModel() {

    private val _state = MutableLiveData<SettingsState>()
    val state: LiveData<SettingsState> = _state

    init {
        loadSettings()
    }

    private fun loadSettings() {
        _state.value = SettingsState.Loading
        val enabled = getSettingsInteractor.execute().darkThemeEnabled
        _state.value = SettingsState.Content(enabled)
    }

    fun onThemeToggled(enabled: Boolean) {
        viewModelScope.launch {
            saveSettingsInteractor.execute(enabled)
            _state.value = SettingsState.Content(enabled)
        }
    }
}