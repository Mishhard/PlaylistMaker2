package com.example.playlistmaker.player.domain.impl

import com.example.playlistmaker.player.data.MediaPlayerWrapper
import com.example.playlistmaker.player.domain.api.PlayerInteractor

class PlayerInteractorImpl(
    private val player: MediaPlayerWrapper
) : PlayerInteractor {
    override fun prepare(
        url: String,
        onPrepared: () -> Unit,
        onCompletion: () -> Unit
    ) {
        player.setDataSource(url)
        player.setOnPreparedListener(onPrepared)
        player.setOnCompletionListener(onCompletion)
        player.prepareAsync()
    }

    override fun play() = player.start()
    override fun pause() = player.pause()
    override fun release() = player.release()
    override fun getCurrentPosition(): Int = player.getCurrentPosition()
    override fun isPlaying(): Boolean = player.isPlaying()
}
