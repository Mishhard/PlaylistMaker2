package com.example.playlistmaker.domain.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Track(
    val trackName: String,
    val artistName: String,
    val trackTimeMillis: Long,
    val artworkUrl100: String,
    val trackId :Int,
    val collectionName: String?,
    val releaseDate: String,
    val primaryGenreName: String,
    val country: String,
    val previewUrl: String
) : Parcelable {

    fun getFullCover(): String = artworkUrl100.replace("100x100bb", "600x600bb")

    fun getFormattedTime(): String {
        val minutes = (trackTimeMillis / 60000)
        val seconds = (trackTimeMillis % 60000 / 1000)
        return String.format("%02d:%02d", minutes, seconds)
    }

    fun getReleaseYear(): String? = releaseDate?.substringBefore('-')
}