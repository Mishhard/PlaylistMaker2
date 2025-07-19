package com.example.playlistmaker.library.data.dto

import com.example.playlistmaker.library.data.db.entity.PlaylistEntity
import com.example.playlistmaker.library.domain.models.Playlist
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

data class PlaylistDto(
    val id: Long,
    val name: String,
    val description: String?,
    val imagePath: String?,
    val trackCount: Int,
    val trackIds: List<Int> = emptyList()
)

private val gson = Gson()
private val listType = object : TypeToken<List<Int>>() {}.type

fun PlaylistEntity.toDto(): PlaylistDto {
    val parsedIds: List<Int> = gson.fromJson(trackIds, listType) ?: emptyList()
    return PlaylistDto(
        id = id,
        name = name,
        description = description,
        imagePath = imagePath,
        trackCount = trackCount,
        trackIds = parsedIds
    )
}

fun PlaylistDto.toEntity(): PlaylistEntity {
    val jsonIds = gson.toJson(trackIds)
    return PlaylistEntity(
        id = id,
        name = name,
        description = description,
        imagePath = imagePath,
        trackIds = jsonIds,
        trackCount = trackCount
    )
}

fun PlaylistDto.toDomain(): Playlist {
    return Playlist(
        id = this.id,
        name = this.name,
        description = this.description,
        imagePath = this.imagePath,
        trackIds = this.trackIds,
        trackCount = this.trackCount
    )
}

