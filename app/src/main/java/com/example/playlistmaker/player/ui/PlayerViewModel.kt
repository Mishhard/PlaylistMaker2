package com.example.playlistmaker.player.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.player.domain.api.PlayerInteractor
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class PlayerViewModel(
    private val interactor: PlayerInteractor
) : ViewModel() {

    private val _state = MutableLiveData<PlayerState>()
    val state: LiveData<PlayerState> = _state

    private val _position = MutableLiveData<Int>()
    val positionLiveData: LiveData<Int> = _position

    private var posJob: Job? = null

    fun prepare(url: String) {
        _state.value = PlayerState.Loading
        interactor.prepare(
            url,
            onPrepared = { _state.postValue(PlayerState.Prepared) },
            onCompletion = {
                posJob?.cancel()
                _position.postValue(0)
                _state.postValue(PlayerState.Completed)
            }
        )
    }

    fun play() {
        interactor.play()
        _state.value = PlayerState.Playing
        posJob = viewModelScope.launch {
            while (isActive) {
                delay(300)
                _position.postValue(interactor.getCurrentPosition())
            }
        }
    }

    fun pause() {
        interactor.pause()
        posJob?.cancel()
        _state.value = PlayerState.Paused
    }

    fun release() {
        posJob?.cancel()
        interactor.release()
    }

    fun isPlaying(): Boolean = interactor.isPlaying()

}