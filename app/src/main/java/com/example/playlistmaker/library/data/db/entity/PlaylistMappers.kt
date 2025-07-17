package com.example.playlistmaker.library.data.db.entity

import com.example.playlistmaker.library.data.dto.PlaylistDto
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

fun PlaylistEntity.toDto(): PlaylistDto {
    val gson = Gson()
    val ids: List<Int> = gson.fromJson(
        this.trackIds,
        object : TypeToken<List<Int>>() {}.type
    ) ?: emptyList()

    return PlaylistDto(
        id = this.id,
        name = this.name,
        description = this.description,
        imagePath = this.imagePath,
        trackCount = this.trackCount,
        trackIds = ids
    )
}
