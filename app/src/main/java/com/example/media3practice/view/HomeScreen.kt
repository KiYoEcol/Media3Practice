package com.example.media3practice.view

import android.app.Application
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
import androidx.compose.material3.IconButton
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
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
import com.example.media3practice.model.CommentModel
import com.example.media3practice.model.CommentRepository
import com.example.media3practice.model.UserModel
import com.example.media3practice.model.UserWithVideoWithLinkRepository
import com.example.media3practice.model.VideoModel
import com.example.media3practice.numberFormat
import com.example.media3practice.viewmodel.MainVIewModelFactory
import com.example.media3practice.viewmodel.MainViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

@Composable
fun HomeScreen(videoId: Int) {
    CommentListModal(videoId)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommentListModal(videoId: Int) {
    val app = LocalContext.current.applicationContext as Application
    val mainViewModel: MainViewModel = viewModel(factory = MainVIewModelFactory(app, videoId))
    val loginUser by mainViewModel.loginUser.collectAsState(initial = null)
    val videoAndOwnerUser by mainViewModel.videoAndOwnerUser.collectAsState(initial = null)
    val comments by mainViewModel.commentsOfVideo.collectAsState(initial = null)
    val commentCount = comments?.size ?: 0

    val commentListBottomSheetState =
        rememberStandardBottomSheetState(initialValue = SheetValue.Hidden, skipHiddenState = false)
    val commentListScaffoldState = rememberBottomSheetScaffoldState(commentListBottomSheetState)
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    val screenHeight = LocalConfiguration.current.screenHeightDp.dp
    val peekHeight = screenHeight - screenWidth * (9f / 16)
    var showInputForm by rememberSaveable { mutableStateOf(false) }
    val inputFormSheetState = rememberModalBottomSheetState()

    val coroutineScope = rememberCoroutineScope()

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

                if (commentCount > 0) {
                    LazyColumn(modifier = Modifier.weight(1f)) {
                        items(comments!!) { comment ->
                            CommentSection(comment)
                        }
                    }
                } else {
                    Spacer(modifier = Modifier.size(32.dp))
                    Text(
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally),
                        text = "コメントはまだありません",
                        fontSize = 16.sp
                    )
                    Text(
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally),
                        text = "コメントを追加して会話を始めましょう",
                        color = Color.Gray
                    )
                    Spacer(modifier = Modifier.weight(1f))
                }
                CommentInputSection(
                    viewModel = mainViewModel,
                    onClickInput = {
                        showInputForm = true
                    }
                )
            }
            if (showInputForm) {
                ModalBottomSheet(
                    sheetState = inputFormSheetState,
                    shape = MaterialTheme.shapes.medium.copy(ZeroCornerSize),
                    onDismissRequest = { showInputForm = false }
                ) {
                    CommentInputForm(
                        viewModel = mainViewModel,
                        onClickSend = {
                            coroutineScope.launch {
                                mainViewModel.saveNewComment()
                                mainViewModel.refreshNewComment()
                                showInputForm = false
                            }
                        }
                    )
                }
            }
        }
    ) {
        Column {
            videoAndOwnerUser?.let {
                VideoPlayerScreen(videoAndOwnerUser!!.first)
                comments?.let {
                    loginUser?.let {
                        InfoContentScreen(
                            modifier = Modifier
                                .fillMaxHeight()
                                .padding(horizontal = 12.dp, vertical = 12.dp),
                            commentListBottomSheetScaffoldState = commentListScaffoldState,
                            video = videoAndOwnerUser!!.first,
                            videoOwnerUser = videoAndOwnerUser!!.second,
                            comments = comments!!,
                            loginUser = loginUser!!
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun CommentListModalPreview() {
    CommentListModal(1)
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
fun CommentSection(
    comment: CommentModel
) {
    Row(modifier = Modifier.padding(8.dp)) {
        UserIconSmall(comment.user.iconRes)
        Spacer(modifier = Modifier.width(4.dp))
        Column(modifier = Modifier.weight(1f)) {
            Row {
                Text(text = "@", color = Color.Gray, fontSize = 14.sp)
                Text(text = comment.user.userName, color = Color.Gray, fontSize = 14.sp)
                Text(text = "・", color = Color.Gray, fontSize = 14.sp)
                Text(text = comment.formattedTimeAgo(), color = Color.Gray, fontSize = 14.sp)
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = comment.comment)
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
                    Text(text = comment.formattedGoodCount(), color = Color.Gray)
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
            if (comment.replyCount > 0) {
                Text(text = "${comment.replyCount}件の返信")
            }
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
    val comment = CommentRepository.createDummyComment(1, 1)
    CommentSection(comment)
}

@Composable
fun CommentInputSection(
    modifier: Modifier = Modifier,
    viewModel: MainViewModel,
    onClickInput: () -> Unit = {}
) {
    val commentedUser by viewModel.loginUser.collectAsState(initial = null)
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        commentedUser?.let {
            UserIconSmall(commentedUser!!.iconRes)
        }
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
            text = if (viewModel.validateInputComment()) viewModel.commentState else "コメントする…",
            color = Color.Gray
        )
    }
}

@Preview(backgroundColor = 0xFFFFFFFF, showBackground = true)
@Composable
fun CommentInputSectionPreview() {
    val app = LocalContext.current.applicationContext as Application
    val viewModel = MainViewModel(app, 1)
    CommentInputSection(viewModel = viewModel)
}

@Composable
fun CommentInputForm(
    viewModel: MainViewModel,
    onClickSend: () -> Unit
) {
    val commentedUser by viewModel.loginUser.collectAsState(initial = null)
    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(key1 = Unit) {
        focusRequester.requestFocus()
    }

    Row(
        modifier = Modifier.padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        commentedUser?.let {
            UserIconSmall(userImageRes = commentedUser!!.iconRes)
        }
        Spacer(modifier = Modifier.width(4.dp))
        OutlinedTextField(
            modifier = Modifier
                .weight(1f)
                .focusRequester(focusRequester),
            shape = RoundedCornerShape(4.dp),
            placeholder = { },
            value = viewModel.commentState,
            onValueChange = { newComment -> viewModel.commentState = newComment }
        )
        Spacer(modifier = Modifier.width(4.dp))
        IconButton(
            onClick = onClickSend,
            enabled = viewModel.validateInputComment()
        ) {
            Icon(
                modifier = Modifier.size(32.dp),
                imageVector = Icons.Outlined.Send,
                contentDescription = "Send Comment"
            )
        }
    }
}

@Preview(backgroundColor = 0xFFFFFFFF, showBackground = true)
@Composable
fun CommentInputFormPreview() {
    val app = LocalContext.current.applicationContext as Application
    val viewModel = viewModel { MainViewModel(app, 1) }
    CommentInputForm(viewModel, {})
}

@Composable
fun VideoPlayerScreen(video: VideoModel) {
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
                    .setUri(video.url)
                    .build()
                Log.d("test", "test: ${video}")
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
    video: VideoModel,
    videoOwnerUser: UserModel,
    comments: List<CommentModel>,
    loginUser: UserModel
) {
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = modifier
    ) {
        Text(text = video.title ?: "", fontSize = 20.sp)
        Row {
            Text(
                text = "${video.formattedViewCount()}回視聴",
                fontSize = 12.sp,
                color = Color.Gray
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = video.formattedTimeAgo() ?: "", fontSize = 12.sp, color = Color.Gray)
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = "…その他", fontSize = 12.sp)
        }
        Spacer(modifier = Modifier.height(12.dp))
        PostedUser(videoOwnerUser)
        Spacer(modifier = Modifier.height(12.dp))
        ActionButtons(video)
        Spacer(modifier = Modifier.height(12.dp))

        val onClickTopCommentSection: () -> Unit = {
            coroutineScope.launch {
                if (commentListBottomSheetScaffoldState.bottomSheetState.currentValue == SheetValue.Hidden) {
                    commentListBottomSheetScaffoldState.bottomSheetState.show()
                } else {
                    commentListBottomSheetScaffoldState.bottomSheetState.hide()
                }
            }
        }

        if (comments.isNotEmpty()) {
            val formattedCommentsCount = numberFormat(comments.size)
            TopComment(
                formattedCommentsCount = formattedCommentsCount,
                topComment = comments[0],
                onClick = onClickTopCommentSection
            )
        } else {
            NoneTopComment(
                loginUser = loginUser,
                onClick = onClickTopCommentSection
            )
        }
    }
}

@Composable
fun PostedUser(videoOwnerUser: UserModel) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Image(
            painter = painterResource(id = videoOwnerUser.iconRes ?: 0),
            contentDescription = "User Icon",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(36.dp)
                .clip(CircleShape)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(text = videoOwnerUser.accountName ?: "")
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = videoOwnerUser.formattedChannelRegisteredCount() ?: "",
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
fun ActionButtons(video: VideoModel) {
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
                        Text(text = video.formattedGoodCount() ?: "")
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
    val loginUser = UserWithVideoWithLinkRepository.dummyUser(0)
    InfoContentScreen(
        commentListBottomSheetScaffoldState = rememberBottomSheetScaffoldState(),
        video = UserWithVideoWithLinkRepository.dummyVideo(0),
        videoOwnerUser = UserWithVideoWithLinkRepository.dummyUser(0),
        comments = CommentRepository.createDummyComments(0),
        loginUser = loginUser
    )
}

@Composable
fun TopComment(
    formattedCommentsCount: String,
    topComment: CommentModel,
    onClick: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
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
            Text(text = formattedCommentsCount, fontSize = 12.sp, color = Color.Gray)
        }
        Spacer(modifier = Modifier.height(8.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            UserIconSmall(userImageRes = topComment.user.iconRes)
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = topComment.comment,
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
    val comment = CommentRepository.createDummyComment(1, 1)
    val formattedCommentsCount = numberFormat(10)
    TopComment(formattedCommentsCount, comment)
}

@Composable
fun NoneTopComment(
    loginUser: UserModel,
    onClick: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = Color.LightGray,
                shape = RoundedCornerShape(12.dp)
            )
            .padding(8.dp)
            .clickable { onClick() }
    ) {
        Text(text = "コメント")
        Spacer(modifier = Modifier.height(8.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            UserIconSmall(userImageRes = loginUser.iconRes)
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = Color.Gray, shape = CircleShape)
                    .padding(horizontal = 8.dp),
                text = "コメントする…"
            )
        }
    }
}

@Preview
@Composable
fun NoneTopCommentPreview() {
    val loginUser = UserWithVideoWithLinkRepository.dummyUser(0)
    NoneTopComment(loginUser = loginUser)
}