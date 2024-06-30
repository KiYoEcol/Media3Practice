package com.example.media3practice.model

import com.example.media3practice.numberFormat
import com.example.media3practice.timestampFormatAgo

data class CommentModel(
    val id: Int,
    val user: UserModel,
    val comment: String,
    val idOfOriginalReplay: Int? = null,
    val registeredTimestamp: Long,
    val goodCount: Int = 0,
    val badCount: Int = 0,
    val replyCount: Int = 0
) {
    fun formattedTimeAgo(): String = timestampFormatAgo(registeredTimestamp)

    fun formattedGoodCount(): String = numberFormat(goodCount)

    fun formattedBadCount(): String = numberFormat(badCount)
}
