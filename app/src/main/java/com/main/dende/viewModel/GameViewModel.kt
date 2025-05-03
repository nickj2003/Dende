package com.main.dende.viewModel

import android.app.Application
import android.util.Log
import android.util.Log.i
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.json.JSONObject
import kotlin.collections.randomOrNull

class GameViewModel(application: Application) : AndroidViewModel(application) {

    private val context = getApplication<Application>().applicationContext
    private var allTasks: Map<String, List<String>> = emptyMap()
    private var taskList: List<String> = emptyList()

    private val _players = MutableStateFlow<List<String>>(emptyList())
    val players = _players.asStateFlow()

    private val _currentTask = MutableStateFlow<String?>(null)
    val currentTask = _currentTask.asStateFlow()

    private val _taskIndex = MutableStateFlow(-1)
    val taskIndex: StateFlow<Int> = _taskIndex

    private val _gameEnded = MutableStateFlow(false)
    val gameEnded = _gameEnded.asStateFlow()

    private val usedTasks = mutableSetOf<String>()
    private val gameTasks = mutableListOf<String>()

    fun setPlayers(playerList: List<String>) {
        _players.value = playerList
        loadTasks()
    }

    private fun loadTasks() {
        viewModelScope.launch {
            val inputStream = context.assets.open("tasks.json")
            val jsonStr = inputStream.bufferedReader().use { it.readText() }

            val jsonObj = JSONObject(jsonStr)
            val loaded = mutableMapOf<String, List<String>>()

            for (key in jsonObj.keys()) {
                val list = jsonObj.getJSONArray(key)
                loaded[key] = List(list.length()) { list.getString(it) }
            }

            allTasks = loaded
            startGame()
        }
    }

    private fun startGame() {
        gameTasks.clear()
        usedTasks.clear()
        _gameEnded.value = false

        val allAvailable = allTasks.values.flatten().shuffled()

        gameTasks.addAll(allAvailable.take(20))
        showNextTask()
    }

    fun showNextTask() {
        if (gameTasks.isEmpty()) {
            _gameEnded.value = true
            return
        }

        _taskIndex.value += 1
        var task = gameTasks.removeAt(0)
        task = replacePlaceholders(task)
        usedTasks.add(task)
        _currentTask.value = task
    }

    private fun replacePlaceholders(task: String): String {
        val players = _players.value
        val taskPlayerCount = task.split("player").size - 1

        val chosenPlayers = players.shuffled().take(taskPlayerCount)
        Log.d("chosenPlayers", chosenPlayers.toString())
        var transformedTask = task
        for ((index, playerName) in chosenPlayers.withIndex()) {
            Log.d("playername", index.toString())
            transformedTask = replacePlayer(transformedTask, playerName, index)
            Log.d("task", transformedTask)
        }
        Log.d("task", transformedTask)
        return transformedTask
    }

    private fun replacePlayer(task: String, player: String?, number: Int): String {
        return task.replace("{player$number}", player ?: "someone")
    }

    fun showPreviousTask() {
        if (_taskIndex.value > 0) {
            _taskIndex.value -= 1
            _currentTask.value = gameTasks[_taskIndex.value]
            _gameEnded.value = false
        }
    }

    fun restartGame() {
        _taskIndex.value = -1
        startGame()
    }
}