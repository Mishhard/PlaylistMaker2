package com.example.playlistmaker.library.data

import com.example.playlistmaker.library.data.db.dao.PlaylistDao
import com.example.playlistmaker.library.data.db.dao.TrackDao
import com.example.playlistmaker.library.data.db.entity.PlaylistEntity
import com.example.playlistmaker.library.domain.db.PlaylistRepository
import com.example.playlistmaker.player.domain.models.Track
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.Flow
import com.example.playlistmaker.library.data.db.entity.TrackEntity
import com.example.playlistmaker.library.domain.models.Playlist
import com.example.playlistmaker.library.data.db.entity.toDomain
import kotlinx.coroutines.flow.map

class PlaylistRepositoryImpl(
    private val playlistDao: PlaylistDao,
    private val trackDao: TrackDao
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

    override suspend fun addTrackToPlaylist(track: Track, playlistId: Long): Boolean {
        val playlist = playlistDao.getPlaylistById(playlistId) ?: return false

        val gson = Gson()
        val listType = object : TypeToken<MutableList<Int>>() {}.type
        val trackIds: MutableList<Int> =
            gson.fromJson(playlist.trackIds, listType) ?: mutableListOf()

        if (trackIds.contains(track.trackId)) return false

        val existingTracks = trackDao.getTracksByIds(listOf(track.trackId))
        if (existingTracks.isEmpty()) {
            val trackEntity = TrackEntity(
                trackId = track.trackId,
                trackName = track.trackName,
                artistName = track.artistName,
                artworkUrl100 = track.artworkUrl100,
                trackTimeMillis = track.trackTimeMillis ?: 0,
                collectionName = track.collectionName,
                releaseDate = track.releaseDate,
                primaryGenreName = track.primaryGenreName,
                country = track.country,
                previewUrl = track.previewUrl
            )
            trackDao.insertTrack(trackEntity)
        }

        trackIds.add(track.trackId)

        val updatedPlaylist = playlist.copy(
            trackIds = gson.toJson(trackIds),
            trackCount = playlist.trackCount + 1
        )
        playlistDao.updatePlaylist(updatedPlaylist)

        return true
    }

    override fun getPlaylists(): Flow<List<Playlist>> {
        return playlistDao.getAllPlaylists()
            .map { entities -> entities.map { it.toDomain() } }
    }

    override suspend fun getPlaylistById(id: Long): Playlist? {
        return playlistDao.getPlaylistById(id)?.toDomain()
    }


    override suspend fun deletePlaylist(id: Long) {
        playlistDao.deletePlaylist(id)

        val allPlaylists = playlistDao.getAllPlaylistsOnce()

        val gson = Gson()
        val usedTrackIds = mutableSetOf<Int>()
        allPlaylists.forEach { playlist ->
            val ids = gson.fromJson<List<Int>>(playlist.trackIds, object : TypeToken<List<Int>>() {}.type) ?: emptyList()
            usedTrackIds.addAll(ids)
        }

        if (usedTrackIds.isEmpty()) {
            trackDao.clearAllTracks()
        } else {
            trackDao.deleteTracksNotInIds(usedTrackIds.toList())
        }
    }

    override suspend fun getTracksForPlaylist(trackIds: List<Int>): List<TrackEntity> {
        val tracks = trackDao.getTracksByIds(trackIds)
        val sorted = trackIds.asReversed().mapNotNull { id ->
            tracks.find { it.trackId == id }
        }
        return sorted
    }


    override suspend fun updatePlaylist(id: Long, name: String, description: String?, imagePath: String?) {
        val playlist = playlistDao.getPlaylistById(id) ?: return
        val updated = playlist.copy(
            name = name,
            description = description,
            imagePath = imagePath
        )
        playlistDao.updatePlaylist(updated)
    }

    override suspend fun removeTrackFromPlaylist(playlistId: Long, trackId: Int) {
        val playlist = playlistDao.getPlaylistById(playlistId) ?: return

        val gson = Gson()
        val type = object : TypeToken<MutableList<Int>>() {}.type
        val trackIds: MutableList<Int> = gson.fromJson(playlist.trackIds, type) ?: mutableListOf()

        var removed = false
        if (trackIds.remove(trackId)) {
            val updatedPlaylist = playlist.copy(
                trackIds = gson.toJson(trackIds),
                trackCount = trackIds.size
            )
            playlistDao.updatePlaylist(updatedPlaylist)
            removed = true
        }

        if (removed) {
            val allPlaylists = playlistDao.getAllPlaylistsOnce()
            val stillUsed = allPlaylists.any { pl ->
                val ids: List<Int> = gson.fromJson(pl.trackIds, type) ?: emptyList()
                ids.contains(trackId)
            }

            if (!stillUsed) {
                trackDao.deleteTrackById(trackId)
            }
        }
    }


}
