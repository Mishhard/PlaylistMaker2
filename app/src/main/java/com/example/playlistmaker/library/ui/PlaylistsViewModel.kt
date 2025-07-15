package com.example.playlistmaker.library.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.library.domain.api.PlaylistInteractor
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.library.data.dto.PlaylistDto
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


class PlaylistsViewModel(private val interactor: PlaylistInteractor) : ViewModel() {

    private val _playlists = MutableStateFlow<List<PlaylistDto>>(emptyList())
    val playlists: StateFlow<List<PlaylistDto>> = _playlists

    fun loadPlaylists() {
        viewModelScope.launch {
            _playlists.value = interactor.getPlaylists()
        }
    }
}
