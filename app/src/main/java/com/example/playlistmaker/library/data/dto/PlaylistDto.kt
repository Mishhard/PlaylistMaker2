package com.example.playlistmaker.library.data.dto

import com.example.playlistmaker.library.data.db.entity.PlaylistEntity
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
