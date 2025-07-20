package com.example.playlistmaker.library.domain.impl


import com.example.playlistmaker.library.domain.api.PlaylistInteractor
import com.example.playlistmaker.library.domain.db.PlaylistRepository
import com.example.playlistmaker.player.domain.models.Track
import kotlinx.coroutines.flow.Flow
import com.example.playlistmaker.player.ui.models.TrackUiModel
import com.example.playlistmaker.library.data.db.entity.toUiModel
import com.example.playlistmaker.library.domain.models.Playlist

class PlaylistInteractorImpl(
    private val repository: PlaylistRepository
) : PlaylistInteractor {

    override fun getPlaylists(): Flow<List<Playlist>> = repository.getPlaylists()

    override suspend fun createPlaylist(name: String, description: String?, imagePath: String?) {
        repository.createPlaylist(name, description, imagePath)
    }

    override suspend fun addTrackToPlaylist(track: Track, playlist: Playlist): Boolean {
        return repository.addTrackToPlaylist(track, playlist.id)
    }

    override suspend fun getPlaylistById(id: Long): Playlist? = repository.getPlaylistById(id)

    override suspend fun deletePlaylist(id: Long) {
        repository.deletePlaylist(id)
    }

    override suspend fun getTracksForPlaylist(playlistId: Long): List<TrackUiModel> {
        val playlist = repository.getPlaylistById(playlistId) ?: return emptyList()
        val trackEntities = repository.getTracksForPlaylist(playlist.trackIds)
        return trackEntities.map { it.toUiModel() }
    }


    override suspend fun removeTrackFromPlaylist(playlistId: Long, trackId: Int) {
        repository.removeTrackFromPlaylist(playlistId, trackId)
    }


    override suspend fun updatePlaylist(id: Long, name: String, description: String?, imagePath: String?) {
        repository.updatePlaylist(id, name, description, imagePath)
    }

}
