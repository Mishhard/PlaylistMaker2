package com.example.playlistmaker.library.ui

import androidx.lifecycle.ViewModel
import com.example.playlistmaker.library.domain.db.PlaylistRepository
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class CreatePlaylistViewModel(private val repository: PlaylistRepository) : ViewModel() {
    fun createPlaylist(name: String, description: String?, imagePath: String?) {
        viewModelScope.launch {
            repository.createPlaylist(name, description, imagePath)
        }
    }

}
