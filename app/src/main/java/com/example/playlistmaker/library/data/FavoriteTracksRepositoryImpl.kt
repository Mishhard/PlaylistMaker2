package com.example.playlistmaker.library.data

import com.example.playlistmaker.library.data.converters.TrackDbConverter
import com.example.playlistmaker.library.data.db.dao.TrackDao
import com.example.playlistmaker.player.domain.models.Track
import com.example.playlistmaker.library.domain.db.FavoriteTracksRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class FavoriteTracksRepositoryImpl(
    private val dao: TrackDao,
    private val converter: TrackDbConverter
) : FavoriteTracksRepository {

    override suspend fun addToFavorites(track: Track) {
        dao.insert(converter.map(track))
    }

    override suspend fun removeFromFavorites(trackId: Int) {
        dao.delete(trackId)
    }

    override suspend fun isFavorite(trackId: Int): Boolean {
        return dao.isTrackFavorite(trackId)
    }

    override fun getFavoriteTracks(): Flow<List<Track>> {
        return dao.getAll().map { list -> list.map { converter.map(it) } }
    }
}
