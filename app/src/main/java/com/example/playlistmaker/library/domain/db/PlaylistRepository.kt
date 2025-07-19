package com.example.playlistmaker.library.domain.db

import com.example.playlistmaker.library.data.db.entity.PlaylistEntity
import com.example.playlistmaker.library.data.db.entity.TrackEntity
import com.example.playlistmaker.library.data.dto.PlaylistDto
import com.example.playlistmaker.player.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface PlaylistRepository {
    suspend fun createPlaylist(name: String, description: String?, imagePath: String?)
    fun getPlaylists(): Flow<List<PlaylistEntity>>
    suspend fun addTrackToPlaylist(track: Track, playlistId: Long): Boolean
    suspend fun getPlaylistById(id: Long): PlaylistEntity?
    suspend fun deletePlaylist(id: Long)
    suspend fun getTracksForPlaylist(trackIds: List<Int>): List<TrackEntity>
    suspend fun updatePlaylist(id: Long, name: String, description: String?, imagePath: String?)
    suspend fun removeTrackFromPlaylist(playlistId: Long, trackId: Int)
}

