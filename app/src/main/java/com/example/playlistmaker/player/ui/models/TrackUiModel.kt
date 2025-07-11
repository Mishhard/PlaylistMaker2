package com.example.playlistmaker.player.ui.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import com.example.playlistmaker.player.domain.models.Track as DomainTrack

@Parcelize
data class TrackUiModel(
    val trackName: String,
    val artistName: String,
    val trackTimeMillis: Long?,
    val artworkUrl100: String,
    val trackId: Int,
    val collectionName: String?,
    val releaseDate: String,
    val primaryGenreName: String,
    val country: String,
    val previewUrl: String,
    val isFavorite: Boolean

) : Parcelable {
    fun toDomain(): DomainTrack = DomainTrack(
        trackName, artistName, trackTimeMillis,
        artworkUrl100, trackId, collectionName,
        releaseDate, primaryGenreName,
        country, previewUrl
    )

    fun getFormattedTime(): String {
        val minutes = (trackTimeMillis ?: 0L) / 1000 / 60
        val seconds = (trackTimeMillis ?: 0L) / 1000 % 60
        return String.format("%d:%02d", minutes, seconds)
    }

    companion object {
        fun fromDomain(t: DomainTrack, isFavorite: Boolean): TrackUiModel = TrackUiModel(
            t.trackName,
            t.artistName,
            t.trackTimeMillis,
            t.artworkUrl100,
            t.trackId,
            t.collectionName,
            t.releaseDate,
            t.primaryGenreName,
            t.country,
            t.previewUrl,
            isFavorite = isFavorite
        )
    }
}
