package com.example.media3practice.model.data

import androidx.annotation.DrawableRes
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.media3practice.R
import com.example.media3practice.model.UserModel

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val accountName: String,
    val userName: String,
    @DrawableRes val iconRes: Int = R.drawable.shell,
    val description: String? = null,
    val location: String,
    val registeredTimestamp: Long,
    val channelRegisteredCount: Int = 0,
    val isRegistered: Boolean = false
)