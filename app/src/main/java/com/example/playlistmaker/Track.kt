package com.example.playlistmaker

import java.text.SimpleDateFormat
import java.util.Locale

data class Track(
    val trackName: String,
    val artistName: String,
    val trackTimeMillis: Long?,
    val artworkUrl100: String,
    val trackId :Int
) {
    fun getFormattedTime(): String {
        return if (trackTimeMillis != null) {
            SimpleDateFormat("mm:ss", Locale.getDefault()).format(trackTimeMillis)
        } else {
            "00:00" // Возвращаем заглушку, если время не указано
        }
    }
}
