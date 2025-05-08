package com.main.dende.navigation

sealed class Screen(val route: String) {
    object PlayerEntry : Screen("player_entry")
    object CategorySelection : Screen("category_selection")
    object Game : Screen("game")
}