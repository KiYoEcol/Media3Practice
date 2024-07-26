package com.example.media3practice.model

import androidx.annotation.DrawableRes
import com.example.media3practice.R
import com.example.media3practice.model.data.UserEntity
import com.example.media3practice.numberFormat

data class UserModel(
    val id: Int,
    val accountName: String,
    val userName: String,
    @DrawableRes val iconRes: Int = R.drawable.shell,
    val description: String? = null,
    val links: List<LinkModel> = emptyList(),
    val location: String,
    val registeredTimestamp: Long,
    val videos: List<VideoModel> = emptyList(),
    val channelRegisteredCount: Int = 0,
    val isRegistered: Boolean = false
) {
    fun formattedChannelRegisteredCount(): String = numberFormat(channelRegisteredCount)

    fun toUserEntity(): UserEntity {
        return UserEntity(
            id = id,
            accountName = accountName,
            userName = userName,
            iconRes = iconRes,
            description = description,
            location = location,
            registeredTimestamp = registeredTimestamp,
            channelRegisteredCount = channelRegisteredCount,
            isRegistered = isRegistered
        )
    }
}
