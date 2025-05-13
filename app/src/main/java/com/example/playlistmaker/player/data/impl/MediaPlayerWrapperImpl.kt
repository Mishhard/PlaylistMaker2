package com.example.playlistmaker.player.data.impl

import android.media.MediaPlayer
import com.example.playlistmaker.player.data.MediaPlayerWrapper

class MediaPlayerWrapperImpl : MediaPlayerWrapper {
    private val mediaPlayer = MediaPlayer()

    override fun setDataSource(url: String) = mediaPlayer.setDataSource(url)
    override fun setOnPreparedListener(listener: () -> Unit) {
        mediaPlayer.setOnPreparedListener { listener() }
    }
    override fun setOnCompletionListener(listener: () -> Unit) {
        mediaPlayer.setOnCompletionListener { listener() }
    }
    override fun prepareAsync() = mediaPlayer.prepareAsync()
    override fun start() = mediaPlayer.start()
    override fun pause() = mediaPlayer.pause()
    override fun release() = mediaPlayer.release()
    override fun getCurrentPosition(): Int = mediaPlayer.currentPosition
    override fun isPlaying(): Boolean = mediaPlayer.isPlaying
}
