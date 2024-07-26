package com.example.media3practice.model.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.media3practice.model.LinkModel

@Entity(tableName = "links")
data class LinkEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val userId: Int,
    val imageUrl: String,
    val url: String
){
    fun toLinkModel():LinkModel{
        return LinkModel(
            imageUrl = imageUrl,
            url = url
        )
    }
}
