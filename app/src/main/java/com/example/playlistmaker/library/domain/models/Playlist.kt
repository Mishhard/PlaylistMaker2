package com.example.playlistmaker.library.domain.models

data class Playlist(
    val id: Long,
    val name: String,
    val description: String?,
    val imagePath: String?,
    val trackCount: Int,
    val trackIds: List<Int>
)
