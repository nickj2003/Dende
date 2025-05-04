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
import com.main.dende.ui.CategorySelectionScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val viewModel: GameViewModel = viewModel()
            val categories by viewModel.selectedCategories.collectAsState()
            val players by viewModel.players.collectAsState()
            val categoriesConfirmed by viewModel.categoriesConfirmed.collectAsState()

            when {
                !categoriesConfirmed -> {
                    CategorySelectionScreen(
                        allCategories = viewModel.allCategories,
                        selectedCategories = categories,
                        onCategoryToggle = viewModel::toggleCategory,
                        onConfirm = { viewModel.confirmCategories() }
                    )
                }

                players.isEmpty() -> {
                    PlayerEntryScreen(onPlayersEntered = viewModel::setPlayers)
                }

                else -> {
                    GameScreen(viewModel)
                }
            }
        }
    }
}