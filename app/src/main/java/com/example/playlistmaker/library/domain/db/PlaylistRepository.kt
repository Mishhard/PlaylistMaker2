package com.example.playlistmaker.library.domain.db

import com.example.playlistmaker.library.data.db.entity.PlaylistEntity
import com.example.playlistmaker.player.domain.models.Track

interface PlaylistRepository {
    suspend fun createPlaylist(name: String, description: String?, imagePath: String?)
    suspend fun getPlaylists(): List<PlaylistEntity>
    suspend fun addTrackToPlaylist(track: Track, playlistId: Long): String
}
