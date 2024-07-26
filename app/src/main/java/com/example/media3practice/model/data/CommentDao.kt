package com.example.media3practice.model.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface CommentDao {
    @Insert
    suspend fun insert(comment: CommentEntity)

    @Query("SELECT * from comments WHERE videoId = :videoId")
    fun getCommentsByVideo(videoId: Int): Flow<List<CommentEntity>>

    @Update
    suspend fun update(comment: CommentEntity)

    @Delete
    suspend fun delete(comment: CommentEntity)
}