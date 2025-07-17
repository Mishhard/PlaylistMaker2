package com.example.playlistmaker.library.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.library.data.dto.PlaylistDto
import com.example.playlistmaker.library.domain.api.PlaylistInteractor
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

class PlaylistsViewModel(
    private val interactor: PlaylistInteractor
) : ViewModel() {

    val playlists: StateFlow<List<PlaylistDto>> =
        interactor.getPlaylists()
            .stateIn(
                viewModelScope,
                SharingStarted.Lazily,
                emptyList()
            )
}
