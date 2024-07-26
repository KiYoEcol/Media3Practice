package com.example.media3practice.model

import com.example.media3practice.model.data.CommentDatabase
import com.example.media3practice.model.data.UserWithVideoWithLinkDatabase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlin.random.Random

class CommentRepository(
    private val commentDatabase: CommentDatabase,
    private val userWithVideoWithLinkDatabase: UserWithVideoWithLinkDatabase
) {
    fun getComments(videoId: Int): Flow<List<CommentModel>> {
        return commentDatabase.commentDao().getCommentsByVideo(videoId).map { list ->
            list.map { entity ->
                val user =
                    userWithVideoWithLinkDatabase.userWithVideoWithLinkDao().getUserWithVideosWithLinks(entity.userId).first()
                        .toUserModel()


                CommentModel(
                    id = entity.id,
                    videoId = entity.videoId,
                    user = user,
                    comment = entity.comment,
                    idOfOriginalReply = entity.idOfOriginalReply,
                    registeredTimestamp = entity.registeredTimestamp,
                    goodCount = entity.goodCount,
                    badCount = entity.badCount,
                    replyCount = entity.replyCount
                )
            }
        }
    }

    suspend fun insertComment(comment: CommentModel) {
        commentDatabase.commentDao().insert(comment.toCommentEntity())
    }

    val dummyComments: MutableList<CommentModel> = mutableListOf<CommentModel>().apply {
        addAll(createDummyComments(1))
        addAll(createDummyComments(2))
        addAll(createDummyComments(3))
    }

    companion object {
        fun createDummyComment(id: Int, videoId: Int, comment: String = "dummy"): CommentModel {
            return CommentModel(
                id = id,
                videoId = videoId,
                user = UserWithVideoWithLinkRepository.dummyUser(),
                comment = comment,
                idOfOriginalReply = null,
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
}