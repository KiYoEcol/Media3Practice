package com.example.media3practice.model

import com.example.media3practice.numberFormat
import java.time.Duration
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import kotlin.math.round

data class VideoModel(
    val id: Int,
    val url: String,
    val owner: UserModel,
    val title: String,
    val description: String,
    val registeredTimestamp: Long,
    val goodCount: Int,
    val badCount: Int,
    val viewCount: Long
) {
    fun formattedTimeAgo(): String {
        val now = LocalDateTime.now()
        val dateTime = LocalDateTime.ofInstant(
            Instant.ofEpochSecond(registeredTimestamp),
            ZoneId.systemDefault()
        )
        val duration = Duration.between(dateTime, now)

        return when {
            duration.toHours() < 24 -> "${duration.toHours()}時間前"
            duration.toDays() < 30 -> "${duration.toDays()}日前"
            duration.toDays() < 365 -> "${duration.toDays() / 30}ヶ月前"
            else -> "${duration.toDays() / 365}年前"
        }
    }

    fun formattedGoodCount(): String = numberFormat(goodCount)

    fun formattedBadCount(): String = numberFormat(badCount)

    fun formattedViewCount(): String = numberFormat(viewCount)
}
