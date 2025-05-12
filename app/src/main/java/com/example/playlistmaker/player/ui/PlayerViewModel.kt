package com.example.playlistmaker.player.ui

import androidx.lifecycle.*
import com.example.playlistmaker.player.domain.api.PlayerInteractor
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class PlayerViewModel(
    private val playerInteractor: PlayerInteractor
) : ViewModel() {

    private val _state = MutableLiveData<PlayerState>()
    val state: LiveData<PlayerState> = _state

    fun prepare(url: String) {
        _state.value = PlayerState.Loading
        playerInteractor.prepare(url) {
            _state.postValue(PlayerState.Prepared)
            startPositionUpdates()
        }
    }

    fun play() {
        playerInteractor.play()
        _state.value = PlayerState.Playing
    }

    fun pause() {
        playerInteractor.pause()
        _state.value = PlayerState.Paused
    }

    fun release() {
        playerInteractor.release()
    }

    private fun startPositionUpdates() {
        viewModelScope.launch {
            while (true) {
                delay(300)
                val pos = playerInteractor.getCurrentPosition()
                _state.postValue(PlayerState.Position(pos))
            }
        }
    }

    companion object {
        fun provideFactory(
            interactor: PlayerInteractor
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return PlayerViewModel(interactor) as T
            }
        }
    }
}