package com.main.dende.viewModel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.main.dende.utils.AppConfig
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.json.JSONObject
import kotlin.random.Random

class GameViewModel(application: Application) : AndroidViewModel(application) {

    private val context = getApplication<Application>().applicationContext
    private var allTasks: Map<String, List<String>> = emptyMap()
    private var taskList: List<String> = emptyList()

    private val _selectedCategories = MutableStateFlow<List<String>>(emptyList())
    val selectedCategories: StateFlow<List<String>> = _selectedCategories

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
    }

    private fun loadAllTasks() {
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
        }
    }

    fun toggleCategory(category: String) {
        _selectedCategories.value = _selectedCategories.value.toMutableList().apply {
            if (contains(category)) remove(category) else add(category)
        }
    }

    private fun startGame() {
        gameTasks.clear()
        usedTasks.clear()
        _gameEnded.value = false

        val selected = _selectedCategories.value
        val tasks = selected.flatMap { category -> allTasks[category] ?: emptyList() }.shuffled()

        gameTasks.addAll(tasks.take(20))
        _taskIndex.value = -1
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
        // Replace player placeholder
        val players = _players.value
        val taskPlayerCount = task.split("player").size - 1
        val chosenPlayers = players.shuffled().take(taskPlayerCount)
        var transformedTask = task
        for ((index, playerName) in chosenPlayers.withIndex()) {
            transformedTask = replacePlayer(transformedTask, playerName, index)
        }

        // Replace shots placeholder
        val maxShots = AppConfig.MAX_SHOTS
        val taskShotsCount = task.split("shot").size - 1
        var index = 0
        while (index < taskShotsCount) {
            val shots = Random.nextInt(from = 1, until = maxShots + 1)
            transformedTask = replaceShot(transformedTask, shots, index)
            index++
        }

        return transformedTask
    }

    private fun replacePlayer(task: String, player: String?, number: Int): String {
        return task.replace("{player$number}", player ?: "someone")
    }

    private fun replaceShot(task: String, shots: Int, number: Int): String {
        var shotText = if (shots == 1) { "$shots shot"} // one shot
        else { "$shots shots"}           // multiple shots

        return task.replace("{shot$number}", shotText)
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