package com.example.playlistmaker.player.domain.db

import androidx.lifecycle.ViewModel
import com.example.playlistmaker.library.domain.db.FavoriteTracksRepository
import com.example.playlistmaker.library.presentation.FavoritesStates
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.player.ui.models.TrackUiModel
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class FavoriteTracksViewModel(
    private val repository: FavoriteTracksRepository
) : ViewModel() {

    private val state =
        MutableStateFlow<FavoritesStates.FavoritesState>(FavoritesStates.FavoritesState.Loading)
    val uiState: StateFlow<FavoritesStates.FavoritesState> = state.asStateFlow()

    init {
        viewModelScope.launch {
            repository.getFavoriteTracks()
                .collect { tracks ->
                    state.value = if (tracks.isEmpty()) {
                        FavoritesStates.FavoritesState.Empty("Нет избранных треков")
                    } else {
                        val uiModels = tracks.map { TrackUiModel.fromDomain(it, isFavorite = true) }
                        FavoritesStates.FavoritesState.Content(uiModels)
                    }
                }

        }
    }

    fun removeFromFavorites(trackId: Int) {
        viewModelScope.launch {
            repository.removeFromFavorites(trackId)
        }
    }
}