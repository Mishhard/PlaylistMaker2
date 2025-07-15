package com.example.playlistmaker.library.domain.impl

import com.example.playlistmaker.library.data.dto.PlaylistDto
import com.example.playlistmaker.library.data.dto.toDto
import com.example.playlistmaker.library.domain.api.PlaylistInteractor
import com.example.playlistmaker.library.domain.db.PlaylistRepository
import com.example.playlistmaker.player.domain.models.Track

class PlaylistInteractorImpl(
    private val repository: PlaylistRepository
) : PlaylistInteractor {

    override suspend fun createPlaylist(name: String, description: String?, imagePath: String?) {
        repository.createPlaylist(name, description, imagePath)
    }

    override suspend fun getPlaylists(): List<PlaylistDto> {
        return repository.getPlaylists().map { it.toDto() }
    }

    override suspend fun addTrackToPlaylist(track: Track, playlist: PlaylistDto): String {
        return if (playlist.trackIds.contains(track.trackId)) {
            "Трек уже есть в плейлисте ${playlist.name}"
        } else {
            repository.addTrackToPlaylist(track, playlist.id)
        }
    }
}
