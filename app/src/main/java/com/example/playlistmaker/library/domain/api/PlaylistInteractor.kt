package com.example.playlistmaker.library.domain.api

import com.example.playlistmaker.library.domain.models.Playlist
import com.example.playlistmaker.player.domain.models.Track
import com.example.playlistmaker.player.ui.models.TrackUiModel
import kotlinx.coroutines.flow.Flow

interface PlaylistInteractor {
    fun getPlaylists(): Flow<List<Playlist>>
    suspend fun createPlaylist(name: String, description: String?, imagePath: String?)
    suspend fun addTrackToPlaylist(track: Track, playlist: Playlist): Boolean
    suspend fun getPlaylistById(id: Long): Playlist?
    suspend fun deletePlaylist(id: Long)
    suspend fun getTracksForPlaylist(playlistId: Long): List<TrackUiModel>
    suspend fun updatePlaylist(id: Long, name: String, description: String?, imagePath: String?)
    suspend fun removeTrackFromPlaylist(playlistId: Long, trackId: Int)
}
