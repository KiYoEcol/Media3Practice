package com.example.media3practice.view

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.shape.ZeroCornerSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.Send
import androidx.compose.material.icons.outlined.ThumbUp
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.BottomSheetScaffoldState
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import com.example.media3practice.R
import com.example.media3practice.viewmodel.MainViewModel
import kotlinx.coroutines.launch

@Composable
fun HomeScreen() {
    CommentListModal()
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommentListModal() {
    val mainViewModel = viewModel(modelClass = MainViewModel::class)
    val commentListBottomSheetState =
        rememberStandardBottomSheetState(initialValue = SheetValue.Hidden, skipHiddenState = false)
    val commentListScaffoldState = rememberBottomSheetScaffoldState(commentListBottomSheetState)
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    val screenHeight = LocalConfiguration.current.screenHeightDp.dp
    val peekHeight = screenHeight - screenWidth * (9f / 16)
    val itemsList = List(10) { "Item #$it" }
    var showInputForm by rememberSaveable { mutableStateOf(false) }
    val inputFormSheetState = rememberModalBottomSheetState()

    BottomSheetScaffold(
        scaffoldState = commentListScaffoldState,
        sheetPeekHeight = peekHeight,
        sheetContainerColor = MaterialTheme.colorScheme.background,
        sheetContent = {
            Column {
                Text(
                    modifier = Modifier.padding(horizontal = 12.dp),
                    text = "コメント",
                    fontSize = 20.sp
                )
                Spacer(modifier = Modifier.height(12.dp))
                CommentListSortButtons()
                Divider(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(1.dp)
                )
                LazyColumn(modifier = Modifier.weight(1f)) {
                    items(itemsList) { item ->
                        CommentSection()
                    }
                }
                CommentInputSection(onClickInput = {
                    showInputForm = true
                })
            }
            if (showInputForm) {
                ModalBottomSheet(
                    sheetState = inputFormSheetState,
                    shape = MaterialTheme.shapes.medium.copy(ZeroCornerSize),
                    onDismissRequest = { showInputForm = false }
                ) {
                    CommentInputForm()
                }
            }
        }
    ) {
        Column {
            VideoPlayerScreen(mainViewModel)
            InfoContentScreen(
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(horizontal = 12.dp, vertical = 12.dp),
                commentListBottomSheetScaffoldState = commentListScaffoldState,
                viewModel = mainViewModel
            )
        }
    }
}

@Preview
@Composable
fun CommentListModalPreview() {
    CommentListModal()
}

@Composable
fun CommentListSortButtons() {
    var isSelectedPopular by rememberSaveable { mutableStateOf(true) }
    val backgroundTintPopular = if (isSelectedPopular) Color.Black else Color.LightGray
    var isSelectedNew by rememberSaveable { mutableStateOf(false) }
    val backgroundTintNew = if (isSelectedNew) Color.Black else Color.LightGray
    Row(modifier = Modifier.padding(horizontal = 12.dp)) {
        Button(
            modifier = Modifier
                .toggleable(
                    value = isSelectedPopular,
                    enabled = true,
                    onValueChange = {}
                ),
            colors = ButtonDefaults.buttonColors(containerColor = backgroundTintPopular),
            shape = RoundedCornerShape(6.dp),
            contentPadding = PaddingValues(12.dp),
            onClick = {
                if (!isSelectedPopular) {
                    isSelectedPopular = !isSelectedPopular
                    isSelectedNew = !isSelectedNew
                }
            },
        ) {
            Text(text = "人気順")
        }
        Spacer(modifier = Modifier.width(4.dp))
        Button(
            modifier = Modifier
                .toggleable(
                    value = isSelectedNew,
                    enabled = true,
                    onValueChange = {

                    }
                ),
            colors = ButtonDefaults.buttonColors(containerColor = backgroundTintNew),
            shape = RoundedCornerShape(6.dp),
            contentPadding = PaddingValues(12.dp),
            onClick = {
                if (!isSelectedNew) {
                    isSelectedNew = !isSelectedNew
                    isSelectedPopular = !isSelectedPopular
                }
            }
        ) {
            Text(text = "新しい順")
        }
    }
}

@Preview
@Composable
fun CommentListSortButtonsPreview() {
    CommentListSortButtons()
}

@Composable
fun CommentSection() {
    Row(modifier = Modifier.padding(8.dp)) {
        UserIconSmall()
        Spacer(modifier = Modifier.width(4.dp))
        Column(modifier = Modifier.weight(1f)) {
            Row {
                Text(text = "@", color = Color.Gray, fontSize = 14.sp)
                Text(text = "aya_nu0211", color = Color.Gray, fontSize = 14.sp)
                Text(text = "・", color = Color.Gray, fontSize = 14.sp)
                Text(text = "3年前", color = Color.Gray, fontSize = 14.sp)
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "ワンツーツーワンもあるよwwwそんなポロリもあるよみたいなノリでww")
            Spacer(modifier = Modifier.height(12.dp))
            Row {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Outlined.ThumbUp,
                        contentDescription = "",
                        tint = Color.Gray,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = "1057", color = Color.Gray)
                }
                Spacer(modifier = Modifier.width(8.dp))
                Icon(
                    imageVector = ImageVector.vectorResource(id = R.drawable.thumb_down_24px),
                    contentDescription = "",
                    tint = Color.Gray,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(50.dp))
                Icon(
                    imageVector = ImageVector.vectorResource(id = R.drawable.chat_24px),
                    contentDescription = "",
                    tint = Color.Gray,
                    modifier = Modifier.size(18.dp)
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text(text = "11件の返信")
        }
        Icon(
            imageVector = Icons.Default.MoreVert,
            contentDescription = "",
            modifier = Modifier.size(20.dp)
        )
    }
}

@Preview(backgroundColor = 0xFFFFFFFF, showBackground = true)
@Composable
fun CommentSectionPreview() {
    CommentSection()
}

@Composable
fun CommentInputSection(
    modifier: Modifier = Modifier,
    onClickInput: () -> Unit = {}
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        UserIconSmall()
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = Color.LightGray,
                    shape = RoundedCornerShape(4.dp)
                )
                .padding(horizontal = 8.dp, vertical = 8.dp)
                .clickable { onClickInput() },
            text = "コメントする…",
            color = Color.Gray
        )
    }
}

@Preview(backgroundColor = 0xFFFFFFFF, showBackground = true)
@Composable
fun CommentInputSectionPreview() {
    CommentInputSection()
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun CommentInputForm() {
    var text by rememberSaveable { mutableStateOf("") }
    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(key1 = Unit) {
        focusRequester.requestFocus()
    }

    Row(
        modifier = Modifier.padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        UserIconSmall()
        Spacer(modifier = Modifier.width(4.dp))
        OutlinedTextField(
            modifier = Modifier
                .weight(1f)
                .focusRequester(focusRequester),
            shape = RoundedCornerShape(4.dp),
            placeholder = { Text(text = "コメントする…") },
            value = text,
            onValueChange = { newText ->
                text = newText
            }
        )
        Spacer(modifier = Modifier.width(4.dp))
        Icon(
            modifier = Modifier.size(32.dp),
            imageVector = Icons.Outlined.Send,
            contentDescription = "Send Comment"
        )
    }
}

@Preview(backgroundColor = 0xFFFFFFFF, showBackground = true)
@Composable
fun CommentInputFormPreview() {
    CommentInputForm()
}

@Composable
fun VideoPlayerScreen(viewModel: MainViewModel) {
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
                    .setUri(viewModel.video.url)
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InfoContentScreen(
    modifier: Modifier = Modifier,
    commentListBottomSheetScaffoldState: BottomSheetScaffoldState,
    viewModel: MainViewModel
) {
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = modifier
    ) {
        Text(text = viewModel.video.title, fontSize = 20.sp)
        Row {
            Text(
                text = "${viewModel.video.formattedViewCount()}回視聴",
                fontSize = 12.sp,
                color = Color.Gray
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = viewModel.video.formattedTimeAgo(), fontSize = 12.sp, color = Color.Gray)
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = "…その他", fontSize = 12.sp)
        }
        Spacer(modifier = Modifier.height(12.dp))
        PostedUser(viewModel)
        Spacer(modifier = Modifier.height(12.dp))
        ActionButtons(viewModel)
        Spacer(modifier = Modifier.height(12.dp))
        TopComment(
            onClick = {
                coroutineScope.launch {
                    if (commentListBottomSheetScaffoldState.bottomSheetState.currentValue == SheetValue.Hidden) {
                        commentListBottomSheetScaffoldState.bottomSheetState.show()
                    } else {
                        commentListBottomSheetScaffoldState.bottomSheetState.hide()
                    }
                }
            }
        )
    }
}

@Composable
fun PostedUser(viewModel: MainViewModel) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Image(
            painter = painterResource(id = viewModel.video.owner.iconRes),
            contentDescription = "User Icon",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(36.dp)
                .clip(CircleShape)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(text = viewModel.video.owner.accountName)
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = viewModel.video.owner.formattedChannelRegisteredCount(),
            fontSize = 12.sp,
            color = Color.Gray
        )
        Spacer(modifier = Modifier.weight(1f))
        Button(
            onClick = { /*TODO*/ },
            colors = ButtonDefaults.buttonColors(containerColor = Color.Black)
        ) {
            Text(text = "チャンネル登録", color = Color.White)
        }
    }
}

@Composable
fun ActionButtons(viewModel: MainViewModel) {
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
                        Text(text = viewModel.video.formattedGoodCount())
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
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(backgroundColor = 0xFFFFFFFF, showBackground = true)
@Composable
fun InfoContentScreenPreview() {
    val viewModel = viewModel(modelClass = MainViewModel::class)
    InfoContentScreen(
        commentListBottomSheetScaffoldState = rememberBottomSheetScaffoldState(),
        viewModel = viewModel
    )
}

@Composable
fun TopComment(onClick: () -> Unit = {}) {
    Column(
        modifier = Modifier
            .background(
                color = Color.LightGray,
                shape = RoundedCornerShape(12.dp)
            )
            .padding(8.dp)
            .clickable { onClick() }
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(text = "コメント")
            Spacer(modifier = Modifier.width(4.dp))
            Text(text = "718", fontSize = 12.sp, color = Color.Gray)
        }
        Spacer(modifier = Modifier.height(8.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            UserIconSmall()
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

@Preview
@Composable
fun TopCommentPreview() {
    TopComment()
}