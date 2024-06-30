package com.example.media3practice.model

class VideoRepository {
    fun getVideo(id: Int): VideoModel {
        return dummyVideo(id)
    }

    fun dummyVideo(id: Int = 1): VideoModel {
        return VideoModel(
            id = id,
            url = "https://storage.googleapis.com/exoplayer-test-media-0/BigBuckBunny_320x180.mp4",
            owner = UserRepository().dummyUser(id = 1),
            title = "Funny Bunny",
            description = "ウサギ vs モモンガ\n\n勝つのはどっちだ！？",
            registeredTimestamp = 1187654400L,
            goodCount = 52389983,
            badCount = 233424,
            viewCount = 23847237
        )
    }
}