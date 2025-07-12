package com.example.playlistmaker.library.domain.db

import com.example.playlistmaker.player.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface FavoriteTracksRepository {
    fun getFavoriteTracks(): Flow<List<Track>>
    suspend fun addToFavorites(track: Track)
    suspend fun removeFromFavorites(trackId: Int)
    suspend fun isFavorite(trackId: Int): Boolean

}