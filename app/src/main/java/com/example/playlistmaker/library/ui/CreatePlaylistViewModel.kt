package com.example.playlistmaker.library.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.library.domain.db.PlaylistRepository
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.library.data.dto.PlaylistDto
import kotlinx.coroutines.launch

class CreatePlaylistViewModel(private val repository: PlaylistRepository) : ViewModel() {
    private val _playlist = MutableLiveData<PlaylistDto>()
    val playlist: LiveData<PlaylistDto> get() = _playlist

    private val _playlistUpdated = MutableLiveData<Boolean>()
    val playlistUpdated: LiveData<Boolean> get() = _playlistUpdated

    fun loadPlaylist(id: Long) {
        viewModelScope.launch {
            repository.getPlaylistById(id)?.let { _playlist.postValue(it) }
        }
    }

    fun createPlaylist(name: String, description: String?, imagePath: String?) {
        viewModelScope.launch { repository.createPlaylist(name, description, imagePath) }
    }

    fun updatePlaylist(id: Long, name: String, description: String?, imagePath: String?) {
        viewModelScope.launch {
            repository.updatePlaylist(id, name, description, imagePath)
            _playlistUpdated.postValue(true)
        }
    }
}

