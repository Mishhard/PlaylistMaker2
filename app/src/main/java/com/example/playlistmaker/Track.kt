package com.example.playlistmaker

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.text.SimpleDateFormat
import java.util.Locale

@Parcelize
data class Track(
    val trackName: String,
    val artistName: String,
    val trackTimeMillis: Long?,
    val artworkUrl100: String,
    val trackId :Int,
    val collectionName: String?,
    val releaseDate: String,
    val primaryGenreName: String,
    val country: String,
    val previewUrl: String,
    ) : Parcelable {
    fun getFormattedTime(): String {
        return if (trackTimeMillis != null) {
            SimpleDateFormat("mm:ss", Locale.getDefault()).format(trackTimeMillis)
        } else {
            "00:00" // Возвращаем заглушку, если время не указано
        }
    }

    fun getFullCover() = artworkUrl100.replaceAfterLast('/', "512x512bb.jpg")

    fun getReleaseYear(): String {
        return releaseDate?.take(4) ?: "Unknown"
    }
}
