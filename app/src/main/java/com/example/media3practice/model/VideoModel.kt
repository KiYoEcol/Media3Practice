package com.example.media3practice.model

import com.example.media3practice.numberFormat
import com.example.media3practice.timestampFormatAgo

data class VideoModel(
    val id: Int,
    val url: String,
    val owner: UserModel,
    val title: String,
    val description: String,
    val registeredTimestamp: Long,
    val goodCount: Int = 0,
    val badCount: Int = 0,
    val viewCount: Long = 0
) {
    fun formattedTimeAgo(): String = timestampFormatAgo(registeredTimestamp)

    fun formattedGoodCount(): String = numberFormat(goodCount)

    fun formattedBadCount(): String = numberFormat(badCount)

    fun formattedViewCount(): String = numberFormat(viewCount)
}
