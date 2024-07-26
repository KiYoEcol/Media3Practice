package com.example.media3practice.model.data

import androidx.room.Embedded
import androidx.room.Relation
import com.example.media3practice.model.UserModel

data class UserWithVideosWithLinks(
    @Embedded val user: UserEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "ownerUserId"
    )
    val videos: List<VideoEntity>,
    @Relation(
        parentColumn = "id",
        entityColumn = "userId"
    )
    val links: List<LinkEntity>
) {
    fun toUserModel(): UserModel {
        return UserModel(
            id = user.id,
            accountName = user.accountName,
            userName = user.userName,
            iconRes = user.iconRes,
            description = user.description,
            links = links.map { it.toLinkModel() },
            location = user.location,
            registeredTimestamp = user.registeredTimestamp,
            videos = videos.map { it.toVideoModel() },
            channelRegisteredCount = user.channelRegisteredCount,
            isRegistered = user.isRegistered
        )
    }
}
