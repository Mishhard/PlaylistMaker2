package com.example.playlistmaker.library.domain.impl

import com.example.playlistmaker.library.data.dto.PlaylistDto
import com.example.playlistmaker.library.data.dto.toDto
import com.example.playlistmaker.library.domain.api.PlaylistInteractor
import com.example.playlistmaker.library.domain.db.PlaylistRepository
import com.example.playlistmaker.player.domain.models.Track
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class PlaylistInteractorImpl(
    private val repository: PlaylistRepository
) : PlaylistInteractor {

    override fun getPlaylists(): Flow<List<PlaylistDto>> {
        return repository.getPlaylists().map { entities ->
            entities.map { entity ->
                PlaylistDto(
                    id = entity.id,
                    name = entity.name,
                    description = entity.description,
                    imagePath = entity.imagePath,
                    trackCount = entity.trackCount,
                    trackIds = Gson().fromJson(entity.trackIds, object : TypeToken<List<Int>>() {}.type) ?: emptyList()
                )
            }
        }
    }

    override suspend fun createPlaylist(name: String, description: String?, imagePath: String?) {
        repository.createPlaylist(name, description, imagePath)
    }

    override suspend fun addTrackToPlaylist(track: Track, playlist: PlaylistDto): Boolean {
        return repository.addTrackToPlaylist(track, playlist.id)
    }
}
