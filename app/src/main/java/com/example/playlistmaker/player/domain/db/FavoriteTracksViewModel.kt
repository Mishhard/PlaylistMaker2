package com.example.playlistmaker.player.domain.db

import androidx.lifecycle.ViewModel
import com.example.playlistmaker.library.domain.db.FavoriteTracksRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.library.presentation.FavoritesState
import com.example.playlistmaker.player.ui.models.TrackUiModel
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class FavoriteTracksViewModel(
    private val repository: FavoriteTracksRepository
) : ViewModel() {

    private val state =
        MutableStateFlow<FavoritesState>(FavoritesState.Loading)
    val uiState: StateFlow<FavoritesState> = state.asStateFlow()

    fun loadFavorites(emptyMessage: String) {
        viewModelScope.launch {
            repository.getFavoriteTracks()
                .collect { tracks ->
                    state.value = if (tracks.isEmpty()) {
                        FavoritesState.Empty(emptyMessage)
                    } else {
                        val uiModels = tracks.map { TrackUiModel.fromDomain(it, isFavorite = true) }
                        FavoritesState.Content(uiModels)
                    }
                }
        }
    }

}