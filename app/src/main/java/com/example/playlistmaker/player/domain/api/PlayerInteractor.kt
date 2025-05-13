package com.example.playlistmaker.player.domain.api

interface PlayerInteractor {

    fun prepare(previewUrl: String, onPrepared: () -> Unit)

    fun play()

    fun pause()

    fun release()

    fun getCurrentPosition(): Int

    fun isPlaying(): Boolean
}