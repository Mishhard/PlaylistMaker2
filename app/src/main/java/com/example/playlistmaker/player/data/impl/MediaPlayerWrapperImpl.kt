package com.example.playlistmaker.player.data.impl

import android.media.MediaPlayer
import com.example.playlistmaker.player.data.MediaPlayerWrapper

class MediaPlayerWrapperImpl(
    private val player: MediaPlayer
) : MediaPlayerWrapper {
    override fun setDataSource(url: String) {
        player.reset()
        player.setDataSource(url)
    }
    override fun setOnPreparedListener(listener: () -> Unit) {
        player.setOnPreparedListener { listener() }
    }
    override fun setOnCompletionListener(listener: () -> Unit) {
        player.setOnCompletionListener { listener() }
    }
    override fun prepareAsync() = player.prepareAsync()
    override fun start() = player.start()
    override fun pause() = player.pause()
    override fun release() = player.reset()
    override fun getCurrentPosition(): Int = player.currentPosition
    override fun isPlaying(): Boolean = player.isPlaying
}
