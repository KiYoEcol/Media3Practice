package com.example.media3practice.viewmodel

import androidx.lifecycle.ViewModel
import com.example.media3practice.model.UserRepository
import com.example.media3practice.model.VideoRepository

class MainViewModel : ViewModel() {
    val videoRepository = VideoRepository()
    val userRepository = UserRepository()

    val video = videoRepository.getVideo(1)
}