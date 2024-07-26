package com.example.media3practice.model.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.media3practice.model.VideoModel

@Entity(tableName = "videos")
data class VideoEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val ownerUserId: Int,
    val url: String,
    val title: String,
    val description: String? = null,
    val registeredTimestamp: Long,
    val goodCount: Int = 0,
    val badCount: Int = 0,
    val viewCount: Long = 0
) {
    fun toVideoModel(): VideoModel {
        return VideoModel(
            id = id,
            ownerUserId = ownerUserId,
            url = url,
            title = title,
            description = description,
            registeredTimestamp = registeredTimestamp,
            goodCount = goodCount,
            badCount = badCount,
            viewCount = viewCount
        )
    }
}
