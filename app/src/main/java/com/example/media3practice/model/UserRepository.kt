package com.example.media3practice.model

class UserRepository {
    fun getUser(id: Int): UserModel {
        return dummyUser(id = id)
    }

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
}