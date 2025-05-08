package com.main.dende.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.main.dende.ui.CategorySelectionScreen
import com.main.dende.ui.GameScreen
import com.main.dende.ui.PlayerEntryScreen
import com.main.dende.viewModel.GameViewModel

@Composable
fun AppNavHost(
    navController: NavHostController,
    viewModel: GameViewModel
) {
    NavHost(navController, startDestination = Screen.PlayerEntry.route) {
        composable(Screen.PlayerEntry.route) {
            PlayerEntryScreen(onPlayersEntered = viewModel::setPlayers)
        }
        composable(Screen.CategorySelection.route) {
            val selectedCategories by viewModel.selectedCategories.collectAsState()
            CategorySelectionScreen(
                allCategories = viewModel.allCategories,
                selectedCategories = selectedCategories,
                onCategoryToggle = viewModel::toggleCategory,
                onConfirm = viewModel::confirmCategories
            )
        }
        composable(Screen.Game.route) {
            GameScreen(viewModel, navController)
        }
    }
}