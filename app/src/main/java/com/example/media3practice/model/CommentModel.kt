package com.example.media3practice.model

import com.example.media3practice.model.data.CommentEntity
import com.example.media3practice.numberFormat
import com.example.media3practice.timestampFormatAgo

data class CommentModel(
    val id: Int,
    val videoId: Int,
    val user: UserModel,
    val comment: String,
    val idOfOriginalReply: Int? = null,
    val registeredTimestamp: Long,
    val goodCount: Int = 0,
    val badCount: Int = 0,
    val replyCount: Int = 0
) {
    fun formattedTimeAgo(): String = timestampFormatAgo(registeredTimestamp)

    fun formattedGoodCount(): String = numberFormat(goodCount)

    fun formattedBadCount(): String = numberFormat(badCount)

    fun toCommentEntity(): CommentEntity {
        return CommentEntity(
            id = id,
            videoId = videoId,
            userId = user.id,
            comment = comment,
            idOfOriginalReply = idOfOriginalReply,
            registeredTimestamp = registeredTimestamp,
            goodCount = goodCount,
            badCount = badCount,
            replyCount = replyCount
        )
    }
}
