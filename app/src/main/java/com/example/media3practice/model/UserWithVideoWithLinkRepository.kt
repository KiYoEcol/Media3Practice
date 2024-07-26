package com.example.media3practice.model

import com.example.media3practice.model.data.UserWithVideoWithLinkDatabase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.transform

class UserWithVideoWithLinkRepository(private val userWithVideoWithLinkDatabase: UserWithVideoWithLinkDatabase) {
    fun getUser(id: Int): Flow<UserModel> {
        return userWithVideoWithLinkDatabase.userWithVideoWithLinkDao()
            .getUserWithVideosWithLinks(id).transform { entity ->
                emit(entity.toUserModel())
            }
    }

    fun getVideoAndOwnerUser(id: Int): Flow<Pair<VideoModel, UserModel>> {
        return userWithVideoWithLinkDatabase.userWithVideoWithLinkDao()
            .getVideoWithOwnerUserWithLinks(id).transform { entity ->
                emit(entity.video.toVideoModel() to entity.getUserModel())
            }
    }

    companion object {
        fun dummyUser(id: Int = 1): UserModel {
            return UserModel(
                id = 1,
                accountName = "VideoOwner",
                userName = "video_owner",
                description = "動画配信してます。\nがんばります！\n\nよろしくお願いします！",
                links = emptyList(),
                location = "日本",
                registeredTimestamp = 1187654400L,
                videos = emptyList(),
                channelRegisteredCount = 15807335,
                isRegistered = false
            )
        }

        fun dummyVideo(id: Int = 1): VideoModel {
            return VideoModel(
                id = id,
                url = "https://storage.googleapis.com/exoplayer-test-media-0/BigBuckBunny_320x180.mp4",
                ownerUserId = 1,
                title = "Funny Bunny",
                description = "ウサギ vs モモンガ\n\n勝つのはどっちだ！？",
                registeredTimestamp = 1187654400L,
                goodCount = 52389983,
                badCount = 233424,
                viewCount = 23847237
            )
        }
    }
}