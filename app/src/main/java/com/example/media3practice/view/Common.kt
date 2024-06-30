package com.example.media3practice.view

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.media3practice.R

@Composable
fun UserIconSmall(@DrawableRes userImageRes: Int = R.drawable.shell){
    Image(
        painter = painterResource(id = userImageRes),
        contentDescription = "User Icon",
        modifier = Modifier
            .size(32.dp)
            .clip(CircleShape)
    )
}