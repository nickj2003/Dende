package com.main.dende

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.main.dende.viewModel.GameViewModel
import com.main.dende.ui.GameScreen
import com.main.dende.ui.PlayerEntryScreen
import androidx.lifecycle.viewmodel.compose.viewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val viewModel: GameViewModel = viewModel()
            val players by viewModel.players.collectAsState()

            if (players.isEmpty()) {
                PlayerEntryScreen(onPlayersEntered = viewModel::setPlayers)
            } else {
                GameScreen(viewModel)
            }
        }
    }
}