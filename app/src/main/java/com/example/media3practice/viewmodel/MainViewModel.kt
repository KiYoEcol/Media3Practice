package com.example.media3practice.viewmodel

import androidx.lifecycle.ViewModel
import com.example.media3practice.model.CommentRepository
import com.example.media3practice.model.UserRepository
import com.example.media3practice.model.VideoRepository

class MainViewModel(videoId:Int) : ViewModel() {
    private val videoRepository = VideoRepository()
    private val userRepository = UserRepository()
    private val commentRepository = CommentRepository()

    val video = videoRepository.getVideo(videoId)
    val commentsOfVideo = commentRepository.getComments(videoId)
}