package com.example.playlistmaker.player.ui

sealed class PlayerState {
    object Loading   : PlayerState()
    object Prepared  : PlayerState()
    object Playing   : PlayerState()
    object Paused    : PlayerState()
    object Completed : PlayerState()
}