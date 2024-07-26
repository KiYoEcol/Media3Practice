package com.example.media3practice.model.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [UserEntity::class, VideoEntity::class, LinkEntity::class], version = 1)
abstract class UserWithVideoWithLinkDatabase : RoomDatabase() {
    abstract fun userWithVideoWithLinkDao(): UserWithVideoWithLinkDao

    companion object {
        @Volatile
        private var Instance: UserWithVideoWithLinkDatabase? = null
        fun getDatabase(context: Context): UserWithVideoWithLinkDatabase {
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(
                    context,
                    UserWithVideoWithLinkDatabase::class.java,
                    "users_with_videos_with_links"
                )
                    .createFromAsset("users_database")
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { Instance = it }
            }
        }
    }
}