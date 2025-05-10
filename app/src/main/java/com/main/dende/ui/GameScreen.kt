package com.main.dende.ui

import android.content.pm.ActivityInfo
import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.navigation.NavHostController
import com.main.dende.navigation.Screen
import com.main.dende.ui.component.DefaultButton
import com.main.dende.utils.LockOrientation
import com.main.dende.viewModel.GameViewModel

@Composable
fun GameScreen(
    viewModel: GameViewModel,
    navController: NavHostController
) {
    val task by viewModel.currentTask.collectAsState()
    val gameEnded by viewModel.gameEnded.collectAsState()
    val totalTasks = viewModel.gameTasks.size

    LockOrientation(
        orientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE,
        immersive = true
    )

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
                Row {
                    Button(onClick = viewModel::restartGame) {
                        Text("Play Again")
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Button(onClick = viewModel::restartGame) {
                        Text("Edit game")
                    }
                }
            }
        } else {
            Text(
                text = task?.value ?: "Loading... (Hopefully)",
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
            Text(text = "${task?.index?.plus(1) ?: 0}/${totalTasks}", style = MaterialTheme.typography.bodyLarge)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.BottomStart
    ) {
        DefaultButton(
            onClick = { viewModel.showPreviousTask() },
            enabled = (task?.index ?: 0) > 0
        ) {
            Text("Back")
        }
    }
}