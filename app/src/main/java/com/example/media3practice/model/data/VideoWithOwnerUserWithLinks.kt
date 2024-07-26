package com.example.media3practice.model.data

import androidx.room.Embedded
import androidx.room.Relation
import com.example.media3practice.model.LinkModel
import com.example.media3practice.model.UserModel

data class VideoWithOwnerUserWithLinks(
    @Embedded val video: VideoEntity,
    @Relation(
        parentColumn = "ownerUserId",
        entityColumn = "id"
    )
    val ownerUser: UserEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "ownerUserId"
    )
    val ownerUserHasVideos: List<VideoEntity>,
    @Relation(
        parentColumn = "ownerUserId",
        entityColumn = "userId"
    )
    val ownerUserHasLinks: List<LinkEntity>
) {
    fun getUserModel(): UserModel {
        return UserModel(
            id = ownerUser.id,
            accountName = ownerUser.accountName,
            userName = ownerUser.userName,
            iconRes = ownerUser.iconRes,
            description = ownerUser.description,
            links = ownerUserHasLinks.map { it.toLinkModel() },
            location = ownerUser.location,
            registeredTimestamp = ownerUser.registeredTimestamp,
            videos = ownerUserHasVideos.map { it.toVideoModel() },
            channelRegisteredCount = ownerUser.channelRegisteredCount,
            isRegistered = ownerUser.isRegistered
        )
    }
}
