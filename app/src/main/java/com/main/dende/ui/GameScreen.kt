package com.main.dende.ui

import android.app.Activity
import android.content.pm.ActivityInfo
import android.os.Build
import android.view.View
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.main.dende.viewModel.GameViewModel

@Composable
fun GameScreen(viewModel: GameViewModel) {
    val activity = LocalActivity.current
    val window = activity?.window

    if (window != null) {
        val windowInsetsController = WindowCompat.getInsetsController(window, window.decorView)
        windowInsetsController.hide(WindowInsetsCompat.Type.systemBars())
    }

    // Force landscape orientation
    DisposableEffect(Unit) {
        // Lock orientation to landscape
        activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE

        // Enable immersive mode once
        WindowCompat.setDecorFitsSystemWindows(window, false)
        val controller = WindowCompat.getInsetsController(window, window?.decorView)
        controller.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        controller.hide(WindowInsetsCompat.Type.systemBars())

        onDispose {
            // Unlock orientation and restore system bars
            activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
            WindowCompat.setDecorFitsSystemWindows(window, true)
            controller.show(WindowInsetsCompat.Type.systemBars())
        }
    }

    val task by viewModel.currentTask.collectAsState()
    val gameEnded by viewModel.gameEnded.collectAsState()
    val taskIndex by viewModel.taskIndex.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp)
            .clickable(
                enabled = !gameEnded,
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) { viewModel.showNextTask() },
        contentAlignment = Alignment.Center
    ) {
        if (gameEnded) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("Game Over!", style = MaterialTheme.typography.headlineMedium)
                Spacer(modifier = Modifier.height(16.dp))
                Button(onClick = viewModel::restartGame) {
                    Text("Play Again")
                }
            }
        } else {
            Text(
                text = task ?: "Loading...",
                style = MaterialTheme.typography.headlineMedium
            )
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.TopStart
    ) {
        if (!gameEnded) {
            Text(text = "${taskIndex + 1}/20", style = MaterialTheme.typography.bodyLarge)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.BottomStart
    ) {
        Button(
            onClick = { viewModel.showPreviousTask() },
            enabled = taskIndex > 0
        ) {
            Text("Back")
        }
    }
}