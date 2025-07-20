package com.example.playlistmaker.library.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.library.domain.api.PlaylistInteractor
import com.example.playlistmaker.library.domain.models.Playlist
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

class PlaylistsViewModel(
    private val interactor: PlaylistInteractor
) : ViewModel() {

    val playlists: StateFlow<List<Playlist>> =
        interactor.getPlaylists()
            .stateIn(
                viewModelScope,
                SharingStarted.Lazily,
                emptyList()
            )
}
