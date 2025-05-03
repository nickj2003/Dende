package com.main.dende.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun PlayerEntryScreen(onPlayersEntered: (List<String>) -> Unit) {
    var currentName by remember { mutableStateOf("") }
    val players = remember { mutableStateListOf<String>() }

    Column(
        modifier = Modifier
            .padding(16.dp)
            .statusBarsPadding()
    ) {
        Text("Enter Player Names", style = MaterialTheme.typography.headlineSmall)
        Row {
            TextField(value = currentName, onValueChange = { currentName = it }, label = { Text("Name") })
            Spacer(modifier = Modifier.width(8.dp))
            Button(onClick = {
                if (currentName.isNotBlank()) {
                    players.add(currentName.trim())
                    currentName = ""
                }
            }) {
                Text("Add")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        players.forEach { Text(it) }

        Spacer(modifier = Modifier.height(24.dp))
        Button(onClick = { onPlayersEntered(players) }, enabled = players.isNotEmpty()) {
            Text("Start Game")
        }
    }
}