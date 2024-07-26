package com.example.media3practice.viewmodel

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.media3practice.R
import com.example.media3practice.model.CommentModel
import com.example.media3practice.model.CommentRepository
import com.example.media3practice.model.UserModel
import com.example.media3practice.model.UserWithVideoWithLinkRepository
import com.example.media3practice.model.VideoModel
import com.example.media3practice.model.data.CommentDatabase
import com.example.media3practice.model.data.UserWithVideoWithLinkDatabase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class MainViewModel(app: Application, private val videoId: Int) : AndroidViewModel(app) {
    private val userWithVideoWithLinkRepository =
        UserWithVideoWithLinkRepository(UserWithVideoWithLinkDatabase.getDatabase(app))
    private val commentRepository = CommentRepository(
        CommentDatabase.getDatabase(getApplication()),
        UserWithVideoWithLinkDatabase.getDatabase(getApplication())
    )

    val loginUser = UserModel(
        id = 2,
        accountName = "User Name",
        userName = "user_1",
        iconRes = R.drawable.shell,
        description = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.\n",
        links = emptyList(),
        location = "日本",
        registeredTimestamp = 1725062400L,
        videos = emptyList(),
        channelRegisteredCount = 0,
        isRegistered = false
    )

    val videoAndOwnerUser = userWithVideoWithLinkRepository.getVideoAndOwnerUser(videoId)
    val commentsOfVideo = commentRepository.getComments(videoId)

    var newComment by mutableStateOf(
        CommentModel(
            id = 0,
            videoId = videoId,
            user = loginUser,
            comment = "",
            idOfOriginalReply = null,
            registeredTimestamp = 0L,
            goodCount = 0,
            badCount = 0,
            replyCount = 0
        )
    )

    fun updateCommentOfNewComment(text: String) {
        newComment = newComment.copy(comment = text)
    }

    fun refreshNewComment() {
        newComment = CommentModel(
            id = 0,
            videoId = videoId,
            user = loginUser,
            comment = "",
            idOfOriginalReply = null,
            registeredTimestamp = 0L,
            goodCount = 0,
            badCount = 0,
            replyCount = 0
        )
    }

    fun validateInputComment(): Boolean = newComment.comment.isNotBlank()

    suspend fun saveNewComment() {
        if (validateInputComment()) {
            commentRepository.insertComment(newComment)
        }
    }
}