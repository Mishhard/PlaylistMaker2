package com.example.playlistmaker.player.ui

sealed class PlayerState(open val progress: Int = 0) {
    object Loading : PlayerState()
    object Prepared : PlayerState()
    data class Playing(override val progress: Int) : PlayerState(progress)
    data class Paused(override val progress: Int) : PlayerState(progress)
    object Completed : PlayerState(0)
    object Liked : PlayerState()
    object NotLiked : PlayerState()
}
