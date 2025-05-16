package com.example.playlistmaker.player.data

interface MediaPlayerWrapper {
    fun setDataSource(url: String)
    fun setOnPreparedListener(listener: () -> Unit)
    fun setOnCompletionListener(listener: () -> Unit)
    fun prepareAsync()
    fun start()
    fun pause()
    fun release()
    fun getCurrentPosition(): Int
    fun isPlaying(): Boolean
}