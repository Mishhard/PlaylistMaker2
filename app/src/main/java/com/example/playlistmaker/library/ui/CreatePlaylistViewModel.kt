package com.example.playlistmaker.library.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.library.domain.models.Playlist
import kotlinx.coroutines.launch
import com.example.playlistmaker.library.domain.api.PlaylistInteractor


class CreatePlaylistViewModel(private val interactor: PlaylistInteractor) : ViewModel() {
    private val _playlist = MutableLiveData<Playlist>()
    val playlist: LiveData<Playlist> get() = _playlist


    private val _playlistUpdated = MutableLiveData<Boolean>()
    val playlistUpdated: LiveData<Boolean> get() = _playlistUpdated

    fun loadPlaylist(id: Long) {
        viewModelScope.launch {
            interactor.getPlaylistById(id)?.let { playlist ->
                _playlist.postValue(playlist)
            }
        }
    }

    fun createPlaylist(name: String, description: String?, imagePath: String?) {
        viewModelScope.launch { interactor.createPlaylist(name, description, imagePath) }
    }

    fun updatePlaylist(id: Long, name: String, description: String?, imagePath: String?) {
        viewModelScope.launch {
            interactor.updatePlaylist(id, name, description, imagePath)
            _playlistUpdated.postValue(true)
        }
    }
}

