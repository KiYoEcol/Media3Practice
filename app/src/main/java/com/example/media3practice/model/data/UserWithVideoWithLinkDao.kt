package com.example.media3practice.model.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface UserWithVideoWithLinkDao {
    @Query("SELECT * from users WHERE id = :id")
    fun getUserWithVideosWithLinks(id: Int): Flow<UserWithVideosWithLinks>

    @Query("SELECT * from videos WHERE id = :id")
    fun getVideoWithOwnerUserWithLinks(id: Int): Flow<VideoWithOwnerUserWithLinks>
}