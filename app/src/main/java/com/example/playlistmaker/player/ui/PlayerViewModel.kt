package com.example.playlistmaker.player.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.library.data.dto.PlaylistDto
import com.example.playlistmaker.library.domain.api.PlaylistInteractor
import com.example.playlistmaker.library.domain.db.FavoriteTracksRepository
import com.example.playlistmaker.player.domain.api.PlayerInteractor
import com.example.playlistmaker.player.domain.models.Track
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class PlayerViewModel(
    private val interactor: PlayerInteractor,
    private val favoriteTracksRepository: FavoriteTracksRepository,
    private val playlistInteractor: PlaylistInteractor
) : ViewModel() {

    private val _state = MutableLiveData<PlayerState>()
    val state: LiveData<PlayerState> = _state

    private val _position = MutableLiveData<Int>()
    val positionLiveData: LiveData<Int> = _position

    private var posJob: Job? = null

    private var currentTrack: Track? = null
    private var isFavorite: Boolean = false

    private val _playlists = MutableLiveData<List<PlaylistDto>>()
    val playlists: LiveData<List<PlaylistDto>> = _playlists

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
        posJob = viewModelScope.launch {
            while (isActive) {
                delay(300)
                val position = interactor.getCurrentPosition()
                _state.postValue(PlayerState.Playing(position))
            }
        }
    }

    fun pause() {
        interactor.pause()
        posJob?.cancel()
        _state.value = PlayerState.Paused(interactor.getCurrentPosition())
    }

    fun release() {
        posJob?.cancel()
        interactor.release()
    }

    fun isPlaying(): Boolean = interactor.isPlaying()

    fun setCurrentTrack(track: Track) {
        currentTrack = track
        checkIsFavorite()
    }

    private fun checkIsFavorite() {
        viewModelScope.launch {
            currentTrack?.let { track ->
                isFavorite = favoriteTracksRepository.isFavorite(track.trackId)
                updateLikeIcon()
            }
        }
    }

    fun toggleFavorite() {
        currentTrack?.let { track ->
            viewModelScope.launch {
                if (isFavorite) {
                    favoriteTracksRepository.removeFromFavorites(track.trackId)
                    isFavorite = false
                } else {
                    favoriteTracksRepository.addToFavorites(track)
                    isFavorite = true
                }
                updateLikeIcon()
            }
        }
    }

    private fun updateLikeIcon() {
        _state.postValue(
            if (isFavorite) PlayerState.Liked else PlayerState.NotLiked
        )
    }

    fun loadPlaylists() {
        viewModelScope.launch {
            _playlists.value = playlistInteractor.getPlaylists()
        }
    }

    fun addTrackToPlaylist(playlist: PlaylistDto, onResult: (String) -> Unit) {
        val track = currentTrack ?: return
        viewModelScope.launch {
            val result = playlistInteractor.addTrackToPlaylist(track, playlist)
            onResult(result)
            loadPlaylists()
        }
    }

}
