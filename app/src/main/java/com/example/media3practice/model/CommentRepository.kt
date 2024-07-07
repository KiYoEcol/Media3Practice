package com.example.media3practice.model

import kotlin.random.Random

class CommentRepository {
    fun getComments(videoId: Int): List<CommentModel> {
        return dummyComments.filter { it.videoId == videoId }
    }

    suspend fun insertComment(comment: CommentModel) {
        dummyComments.add(comment)
    }

    val dummyComments: MutableList<CommentModel> = mutableListOf<CommentModel>().apply {
        addAll(createDummyComments(1))
        addAll(createDummyComments(2))
        addAll(createDummyComments(3))
    }

    fun createDummyComment(id: Int, videoId: Int, comment: String = "dummy"): CommentModel {
        return CommentModel(
            id = id,
            videoId = videoId,
            user = UserRepository().dummyUser(),
            comment = comment,
            idOfOriginalReplay = null,
            registeredTimestamp = 1187654400L,
            goodCount = Random.nextInt(0, 2147483647),
            badCount = Random.nextInt(0, 2147483647),
            replyCount = 0
        )
    }

    fun createDummyComments(videoId: Int): List<CommentModel> {
        val list = mutableListOf<CommentModel>()
        for (i in 1..10) {
            val comment = createDummyComment(id = i, videoId = videoId, comment = "dummy $i")
            list.add(comment)
        }
        return list
    }
}