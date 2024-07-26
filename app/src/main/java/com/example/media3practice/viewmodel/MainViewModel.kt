package com.example.media3practice.viewmodel

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import com.example.media3practice.model.CommentModel
import com.example.media3practice.model.CommentRepository
import com.example.media3practice.model.UserWithVideoWithLinkRepository
import com.example.media3practice.model.data.CommentDatabase
import com.example.media3practice.model.data.UserWithVideoWithLinkDatabase
import kotlinx.coroutines.flow.first

class MainViewModel(app: Application, private val videoId: Int) : AndroidViewModel(app) {
    private val userWithVideoWithLinkRepository =
        UserWithVideoWithLinkRepository(UserWithVideoWithLinkDatabase.getDatabase(app))
    private val commentRepository = CommentRepository(
        CommentDatabase.getDatabase(getApplication()),
        UserWithVideoWithLinkDatabase.getDatabase(getApplication())
    )

    val loginUser = userWithVideoWithLinkRepository.getUser(2)
    val videoAndOwnerUser = userWithVideoWithLinkRepository.getVideoAndOwnerUser(videoId)
    val commentsOfVideo = commentRepository.getComments(videoId)
    var commentState by mutableStateOf("")

    fun refreshNewComment() {
        commentState = ""
    }

    fun validateInputComment(): Boolean = commentState.isNotBlank()

    suspend fun saveNewComment(idOfOriginalReply: Int? = null) {
        if (validateInputComment()) {
            val newComment = CommentModel(
                id = 0,
                videoId = videoId,
                idOfOriginalReply = idOfOriginalReply,
                comment = commentState,
                registeredTimestamp = System.currentTimeMillis(),
                goodCount = 0,
                badCount = 0,
                replyCount = 0,
                user = loginUser.first()
            )
            commentRepository.insertComment(newComment)
        }
    }
}