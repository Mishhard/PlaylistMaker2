package com.example.playlistmaker.library.data.converters

import com.example.playlistmaker.player.domain.models.Track
import com.example.playlistmaker.library.data.db.entity.FavoriteTrackEntity

class TrackDbConverter {
    fun map(entity: FavoriteTrackEntity): Track = Track(
        trackId = entity.trackId,
        trackName = entity.trackName,
        artistName = entity.artistName,
        artworkUrl100 = entity.artworkUrl100,
        previewUrl = entity.previewUrl,
        collectionName = entity.collectionName,
        releaseDate = entity.releaseDate,
        primaryGenreName = entity.primaryGenreName,
        country = entity.country,
        trackTimeMillis = entity.trackTimeMillis
    )

    fun map(track: Track): FavoriteTrackEntity = FavoriteTrackEntity(
        trackId = track.trackId,
        trackName = track.trackName,
        artistName = track.artistName,
        artworkUrl100 = track.artworkUrl100,
        previewUrl = track.previewUrl,
        collectionName = track.collectionName,
        releaseDate = track.releaseDate,
        primaryGenreName = track.primaryGenreName,
        country = track.country,
        trackTimeMillis = track.trackTimeMillis  ?: 0L
    )
}
