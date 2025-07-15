package com.example.playlistmaker.library.domain.api

import com.example.playlistmaker.library.data.dto.PlaylistDto
import com.example.playlistmaker.player.domain.models.Track

interface PlaylistInteractor {
    suspend fun createPlaylist(name: String, description: String?, imagePath: String?)
    suspend fun getPlaylists(): List<PlaylistDto>
    suspend fun addTrackToPlaylist(track: Track, playlist: PlaylistDto): String
}
