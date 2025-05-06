package com.main.dende

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.main.dende.viewModel.GameViewModel
import com.main.dende.ui.GameScreen
import com.main.dende.ui.PlayerEntryScreen
import androidx.lifecycle.viewmodel.compose.viewModel
import com.main.dende.ui.CategorySelectionScreen
import com.main.dende.ui.theme.DendeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DendeTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    App()
                }
            }
        }
    }
}

@Composable
fun App() {
    val viewModel: GameViewModel = viewModel()
    val categories by viewModel.selectedCategories.collectAsState()
    val players by viewModel.players.collectAsState()
    val categoriesConfirmed by viewModel.categoriesConfirmed.collectAsState()

    when {
        players.isEmpty() -> {
            PlayerEntryScreen(onPlayersEntered = viewModel::setPlayers)
        }

        !categoriesConfirmed -> {
            CategorySelectionScreen(
                allCategories = viewModel.allCategories,
                selectedCategories = categories,
                onCategoryToggle = viewModel::toggleCategory,
                onConfirm = { viewModel.confirmCategories() }
            )
        }

        else -> {
            GameScreen(viewModel)
        }
    }
}