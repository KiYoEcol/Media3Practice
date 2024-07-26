package com.example.media3practice.model.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.media3practice.model.CommentModel
import com.example.media3practice.model.UserModel

@Entity(tableName = "comments")
data class CommentEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val videoId: Int,
    val userId: Int,
    val comment: String,
    val idOfOriginalReply: Int? = null,
    val registeredTimestamp: Long,
    val goodCount: Int = 0,
    val badCount: Int = 0,
    val replyCount: Int = 0
)