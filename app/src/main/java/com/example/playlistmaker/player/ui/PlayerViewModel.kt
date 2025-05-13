package com.example.playlistmaker.player.ui

import androidx.lifecycle.*
import com.example.playlistmaker.player.domain.api.PlayerInteractor
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

sealed class PlayerState {
    object Loading : PlayerState()
    object Prepared : PlayerState()
    object Playing : PlayerState()
    object Paused  : PlayerState()
    data class Position(val ms: Int) : PlayerState()
}

class PlayerViewModel(
    private val interactor: PlayerInteractor
) : ViewModel() {

    private val _state = MutableLiveData<PlayerState>()
    val state: LiveData<PlayerState> = _state

    private var posJob: Job? = null

    fun prepare(url: String) {
        _state.value = PlayerState.Loading
        interactor.prepare(
            url,
            onPrepared = {
                _state.postValue(PlayerState.Prepared)
            },
            onCompletion = {
                // при завершении трека
                posJob?.cancel()
                _state.postValue(PlayerState.Paused)
                _state.postValue(PlayerState.Position(0))
            }
        )
    }

    fun play() {
        interactor.play()
        _state.value = PlayerState.Playing

        posJob = viewModelScope.launch {
            while (isActive) {
                delay(300)
                val p = interactor.getCurrentPosition()
                _state.postValue(PlayerState.Position(p))
            }
        }
    }

    fun pause() {
        interactor.pause()
        _state.value = PlayerState.Paused
        posJob?.cancel()
    }

    fun release() {
        posJob?.cancel()
        interactor.release()
    }

    fun isPlaying(): Boolean = interactor.isPlaying()

    override fun onCleared() {
        super.onCleared()
        release()
    }

    companion object {
        fun provideFactory(interactor: PlayerInteractor) =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T =
                    PlayerViewModel(interactor) as T
            }
    }
}
