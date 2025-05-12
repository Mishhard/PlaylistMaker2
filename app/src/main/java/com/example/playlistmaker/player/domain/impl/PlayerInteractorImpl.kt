package com.example.playlistmaker.player.domain.impl

import android.media.MediaPlayer
import com.example.playlistmaker.player.domain.api.PlayerInteractor


class PlayerInteractorImpl : PlayerInteractor {
    private var mediaPlayer: MediaPlayer? = null

    override fun prepare(previewUrl: String, onPrepared: () -> Unit) {
        mediaPlayer = MediaPlayer().apply {
            setDataSource(previewUrl)
            prepareAsync()
            setOnPreparedListener {
                onPrepared()
            }
        }
    }
    override fun play() {
        mediaPlayer?.start()
    }

    override fun pause() {
        mediaPlayer?.pause()
    }

    override fun release() {
        mediaPlayer?.release()
        mediaPlayer = null
    }

    override fun getCurrentPosition(): Int = mediaPlayer?.currentPosition ?: 0
}