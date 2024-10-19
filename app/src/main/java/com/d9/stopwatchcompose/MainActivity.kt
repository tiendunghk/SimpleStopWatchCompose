package com.d9.stopwatchcompose

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.d9.stopwatchcompose.ui.theme.StopWatchComposeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            StopWatchComposeTheme {
                val viewModel: StopWatchViewModel by viewModels()
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    StopWatch(
                        modifier = Modifier.padding(innerPadding), viewModel = viewModel
                    )
                }
            }
        }
    }
}

@SuppressLint("DefaultLocale")
fun Long.millisecondsToString(): String {
    val seconds = this / 1000
    val minutes = seconds / 60
    val remainingSeconds = seconds % 60
    val remainingMilliseconds = this % 1000
    val centiSeconds = remainingMilliseconds / 10
    return String.format("%02d:%02d,%02d", minutes, remainingSeconds, centiSeconds)
}


@Composable
fun StopWatch(modifier: Modifier = Modifier, viewModel: StopWatchViewModel) {
    val isPlay = remember {
        mutableStateOf(false)
    }

    val timeRange = viewModel.timeRange.collectAsState()

    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .aspectRatio(1f)
                .background(
                    shape = CircleShape, color = Color.Blue.copy(alpha = 0.45f)
                ), contentAlignment = Alignment.Center
        ) {
            if (!isPlay.value && timeRange.value != 0L) {
                IconButton(onClick = {
                    viewModel.stop()
                }, modifier = Modifier.align(Alignment.TopCenter)) {
                    Icon(
                        modifier = Modifier.size(100.dp),
                        imageVector = Icons.Default.Clear,
                        contentDescription = "Clear",
                        tint = Color.LightGray
                    )
                }
            }
            Text(
                text = timeRange.value.millisecondsToString(), style = TextStyle(
                    fontSize = 50.sp, color = Color.White
                )
            )
            IconButton(modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 30.dp),
                onClick = {

                    if (isPlay.value) {
                        viewModel.pause()
                    } else {
                        viewModel.start(timeRange.value == 0L)
                    }
                    isPlay.value = !isPlay.value
                }) {
                Icon(
                    modifier = Modifier.size(100.dp),
                    painter = painterResource(id = if (isPlay.value) R.drawable.ic_pause else R.drawable.ic_play),
                    contentDescription = "Play/Resume",
                    tint = Color.LightGray
                )
            }
        }
    }
}