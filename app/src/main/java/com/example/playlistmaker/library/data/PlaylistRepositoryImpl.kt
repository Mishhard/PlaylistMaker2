package com.example.playlistmaker.library.data

import com.example.playlistmaker.library.data.db.dao.PlaylistDao
import com.example.playlistmaker.library.data.db.entity.PlaylistEntity
import com.example.playlistmaker.library.domain.db.PlaylistRepository
import com.example.playlistmaker.player.domain.models.Track
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.Flow

class PlaylistRepositoryImpl(
    private val playlistDao: PlaylistDao
) : PlaylistRepository {

    override suspend fun createPlaylist(
        name: String,
        description: String?,
        imagePath: String?
    ) {
        val playlist = PlaylistEntity(
            name = name,
            description = description,
            imagePath = imagePath,
            trackIds = "[]",
            trackCount = 0
        )
        playlistDao.insert(playlist)
    }

    override fun getPlaylists(): Flow<List<PlaylistEntity>> {
        return playlistDao.getAllPlaylists()
    }

    override suspend fun addTrackToPlaylist(track: Track, playlistId: Long): Boolean {
        val playlist = playlistDao.getPlaylistById(playlistId) ?: return false

        val gson = Gson()
        val listType = object : TypeToken<MutableList<Int>>() {}.type
        val trackIds: MutableList<Int> =
            gson.fromJson(playlist.trackIds, listType) ?: mutableListOf()

        if (trackIds.contains(track.trackId)) return false

        trackIds.add(track.trackId)

        val updatedPlaylist = playlist.copy(
            trackIds = gson.toJson(trackIds),
            trackCount = playlist.trackCount + 1
        )
        playlistDao.updatePlaylist(updatedPlaylist)

        return true
    }

}
