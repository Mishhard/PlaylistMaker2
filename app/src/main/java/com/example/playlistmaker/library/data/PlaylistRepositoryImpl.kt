package com.example.playlistmaker.library.data

import com.example.playlistmaker.library.data.db.dao.PlaylistDao
import com.example.playlistmaker.library.data.db.entity.PlaylistEntity
import com.example.playlistmaker.library.domain.db.PlaylistRepository
import com.example.playlistmaker.player.domain.models.Track
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class PlaylistRepositoryImpl(
    private val playlistDao: PlaylistDao
) : PlaylistRepository {

    override suspend fun createPlaylist(name: String, description: String?, imagePath: String?) {
        val playlist = PlaylistEntity(
            name = name,
            description = description,
            imagePath = imagePath
        )
        playlistDao.insert(playlist)
    }

    override suspend fun getPlaylists(): List<PlaylistEntity> {
        return playlistDao.getAllPlaylists()
    }

    override suspend fun addTrackToPlaylist(track: Track, playlistId: Long): String {
        val playlist = playlistDao.getPlaylistById(playlistId)
            ?: return "Плейлист не найден"

        val gson = Gson()
        val listType = object : TypeToken<List<Int>>() {}.type
        val trackIds: MutableList<Int> =
            gson.fromJson(playlist.trackIds, listType) ?: mutableListOf()

        if (trackIds.contains(track.trackId)) {
            return "Трек уже есть в плейлисте"
        }

        trackIds.add(track.trackId)

        val updatedPlaylist = playlist.copy(
            trackIds = gson.toJson(trackIds),
            trackCount = playlist.trackCount + 1
        )
        playlistDao.updatePlaylist(updatedPlaylist)

        return "Добавлено в ${playlist.name}"
    }
}
