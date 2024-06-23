package com.example.media3practice.view

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ThumbUp
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import com.example.media3practice.R

@Composable
fun HomeScreen() {
    Column {
        VideoPlayerScreen()
        InfoContentScreen(
            modifier = Modifier
                .fillMaxHeight()
                .padding(horizontal = 12.dp, vertical = 12.dp)
        )
    }
}

@Composable
fun VideoPlayerScreen() {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    var playWhenReady by rememberSaveable { mutableStateOf(true) }
    var currentItem by rememberSaveable { mutableIntStateOf(0) }
    var playbackPosition by rememberSaveable { mutableLongStateOf(0L) }

    val exoPlayer = remember {
        ExoPlayer.Builder(context)
            .build()
            .also { exoPlayer ->
                val mediaItem = MediaItem.Builder()
                    .setUri("https://storage.googleapis.com/exoplayer-test-media-0/BigBuckBunny_320x180.mp4")
                    .build()
                exoPlayer.setMediaItem(mediaItem)

                exoPlayer.playWhenReady = playWhenReady
                exoPlayer.seekTo(currentItem, playbackPosition)
                exoPlayer.prepare()
            }
    }

    DisposableEffect(Unit) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_START -> {
                    exoPlayer.playWhenReady = playWhenReady
                    exoPlayer.seekTo(currentItem, playbackPosition)
                }

                Lifecycle.Event.ON_STOP -> {
                    playbackPosition = exoPlayer.currentPosition
                    currentItem = exoPlayer.currentMediaItemIndex
                    playWhenReady = exoPlayer.playWhenReady
                }

                Lifecycle.Event.ON_DESTROY -> exoPlayer.release()

                else -> Unit
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
            playbackPosition = exoPlayer.currentPosition
            currentItem = exoPlayer.currentMediaItemIndex
            playWhenReady = exoPlayer.playWhenReady
            exoPlayer.release()
        }
    }

    AndroidView(
        factory = {
            PlayerView(it)
        },
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(16 / 9f)
    ) {
        it.player = exoPlayer
    }
}

@Composable
fun InfoContentScreen(modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        Text(text = "一緒に正解したい！！！！【高校生クイズのアレ】", fontSize = 20.sp)
        Row {
            Text(text = "171万回視聴", fontSize = 12.sp, color = Color.Gray)
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = "4年前", fontSize = 12.sp, color = Color.Gray)
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = "…その他", fontSize = 12.sp)
        }
        Spacer(modifier = Modifier.height(12.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Image(
                painter = painterResource(id = R.drawable.shell),
                contentDescription = "User Icon",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(text = "じゃぱんぱん")
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = "2760", fontSize = 12.sp, color = Color.Gray)
            Spacer(modifier = Modifier.weight(1f))
            Button(
                onClick = { /*TODO*/ },
                colors = ButtonDefaults.buttonColors(containerColor = Color.Black)
            ) {
                Text(text = "チャンネル登録", color = Color.White)
            }
        }
        Spacer(modifier = Modifier.height(12.dp))
        val horizontalScrollState = rememberScrollState()
        Row(modifier = Modifier.horizontalScroll(horizontalScrollState)) {
            AssistChip(
                onClick = { /*TODO*/ },
                label = {
                    Row(modifier = Modifier.clickable { Log.d("TEST", "Click Good") }) {
                        Row {
                            Icon(
                                imageVector = Icons.Outlined.ThumbUp,
                                contentDescription = "Good",
                                tint = MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier.size(AssistChipDefaults.IconSize)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(text = "2274")
                            Spacer(modifier = Modifier.width(4.dp))
                            Divider(
                                modifier = Modifier
                                    .height(AssistChipDefaults.IconSize)
                                    .width(1.dp),
                                color = Color.Gray
                            )
                        }
                        Spacer(modifier = Modifier.width(4.dp))
                        Icon(
                            imageVector = ImageVector.vectorResource(id = R.drawable.thumb_down_24px),
                            contentDescription = "Bad",
                            tint = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier
                                .size(AssistChipDefaults.IconSize)
                                .clickable { Log.d("TEST", "Click Bad") }
                        )
                    }
                },
                shape = CircleShape
            )
            Spacer(modifier = Modifier.width(12.dp))
            AssistChip(
                onClick = { /*TODO*/ },
                leadingIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.share),
                        contentDescription = "Share",
                        tint = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.size(AssistChipDefaults.IconSize)
                    )
                },
                label = { Text(text = "共有") },
                shape = CircleShape
            )
            Spacer(modifier = Modifier.width(12.dp))
            AssistChip(
                onClick = { /*TODO*/ },
                leadingIcon = {
                    Icon(
                        imageVector = ImageVector.vectorResource(id = R.drawable.design_services_24px),
                        contentDescription = "Remix",
                        tint = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.size(AssistChipDefaults.IconSize)
                    )
                },
                label = { Text(text = "リミックス") },
                shape = CircleShape
            )
            Spacer(modifier = Modifier.width(12.dp))
            AssistChip(
                onClick = { /*TODO*/ },
                leadingIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.download),
                        contentDescription = "Offline",
                        tint = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.size(AssistChipDefaults.IconSize)
                    )
                },
                label = { Text(text = "オフライン") },
                shape = CircleShape
            )
            Spacer(modifier = Modifier.width(12.dp))
            AssistChip(
                onClick = { /*TODO*/ },
                leadingIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.icons8_scissors_50),
                        contentDescription = "Clip",
                        tint = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.size(AssistChipDefaults.IconSize)
                    )
                },
                label = { Text(text = "クリップ") },
                shape = CircleShape
            )
            Spacer(modifier = Modifier.width(12.dp))
            AssistChip(
                onClick = { /*TODO*/ },
                leadingIcon = {
                    Icon(
                        imageVector = ImageVector.vectorResource(id = R.drawable.bookmark_24px),
                        contentDescription = "Save",
                        tint = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.size(AssistChipDefaults.IconSize)
                    )
                },
                label = { Text(text = "保存") },
                shape = CircleShape
            )
            Spacer(modifier = Modifier.width(12.dp))
            AssistChip(
                onClick = { /*TODO*/ },
                leadingIcon = {
                    Icon(
                        imageVector = ImageVector.vectorResource(id = R.drawable.flag_24px),
                        contentDescription = "Report",
                        tint = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.size(AssistChipDefaults.IconSize)
                    )
                },
                label = { Text(text = "報告") },
                shape = CircleShape
            )
        }
        Spacer(modifier = Modifier.height(12.dp))
        Column(
            modifier = Modifier
                .background(
                    color = Color.LightGray,
                    shape = RoundedCornerShape(12.dp)
                )
                .padding(8.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = "コメント")
                Spacer(modifier = Modifier.width(4.dp))
                Text(text = "718", fontSize = 12.sp, color = Color.Gray)
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    painter = painterResource(id = R.drawable.shell),
                    contentDescription = "User Icon",
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "海外で「作者は上質なコカ◯ンをきめている」→「コカ◯ンきめたくらいでボーボボが書けると思うな」って流れになった話好き",
                    fontSize = 14.sp,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}