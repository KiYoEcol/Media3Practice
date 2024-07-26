package com.example.media3practice.model.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [CommentEntity::class], version = 1)
abstract class CommentDatabase : RoomDatabase() {
    abstract fun commentDao(): CommentDao

    companion object {
        @Volatile
        private var Instance: CommentDatabase? = null
        fun getDatabase(context: Context): CommentDatabase {
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(
                    context,
                    CommentDatabase::class.java,
                    "comments_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { Instance = it }
            }
        }
    }
}