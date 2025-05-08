package com.main.dende

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.main.dende.viewModel.GameViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.main.dende.navigation.AppNavHost
import com.main.dende.navigation.Screen
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
    val navController = rememberNavController()
    val players by viewModel.players.collectAsState()
    val categoriesConfirmed by viewModel.categoriesConfirmed.collectAsState()

    LaunchedEffect(players, categoriesConfirmed) {
        when {
            players.isEmpty() -> navController.navigate(Screen.PlayerEntry.route) {
                popUpTo(0)
            }
            !categoriesConfirmed -> navController.navigate(Screen.CategorySelection.route) {
                popUpTo(0)
            }
            else -> navController.navigate(Screen.Game.route) {
                popUpTo(0)
            }
        }
    }

    AppNavHost(navController, viewModel)
}