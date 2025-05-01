package com.main.dende.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.main.dende.viewModel.GameViewModel

@Composable
fun GameScreen(viewModel: GameViewModel) {
    val task by viewModel.currentTask.collectAsState()
    val gameEnded by viewModel.gameEnded.collectAsState()

    Column(modifier = Modifier.fillMaxSize().padding(32.dp), verticalArrangement = Arrangement.Center) {
        if (gameEnded) {
            Text("Game Over!", style = MaterialTheme.typography.headlineMedium)
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = viewModel::restartGame) {
                Text("Play Again")
            }
        } else {
            Text(task ?: "Loading...", style = MaterialTheme.typography.headlineMedium)
            Spacer(modifier = Modifier.height(24.dp))
            Button(onClick = viewModel::showNextTask) {
                Text("Next")
            }
        }
    }
}