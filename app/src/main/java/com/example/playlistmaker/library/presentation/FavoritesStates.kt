package com.example.playlistmaker.library.presentation

import com.example.playlistmaker.player.ui.models.TrackUiModel

class FavoritesStates {
    sealed interface FavoritesState {
        object Loading : FavoritesState
        data class Content(val tracks: List<TrackUiModel>) : FavoritesState
        data class Empty(val message: String) : FavoritesState
    }
}