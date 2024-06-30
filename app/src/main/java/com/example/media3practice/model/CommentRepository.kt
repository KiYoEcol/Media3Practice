package com.example.media3practice.model

import kotlin.random.Random

class CommentRepository {
    fun getComments(videoId: Int): List<CommentModel> {
        val list = mutableListOf<CommentModel>()
        for (i in 1..10) {
            val comment = dummyComment(id = i, comment = "dummy $i")
            list.add(comment)
        }
        return list
    }

    fun dummyComment(id: Int, comment: String = "dummy"): CommentModel {
        return CommentModel(
            id = id,
            user = UserRepository().dummyUser(),
            comment = comment,
            idOfOriginalReplay = null,
            registeredTimestamp = 1187654400L,
            goodCount = Random.nextInt(0, 2147483647),
            badCount = Random.nextInt(0, 2147483647),
            replyCount = 0
        )
    }
}