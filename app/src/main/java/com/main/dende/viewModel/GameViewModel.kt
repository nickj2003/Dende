package com.main.dende.viewModel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.main.dende.utils.AppConfig
import com.main.dende.utils.LinkedList
import com.main.dende.utils.Node
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.json.JSONObject
import kotlin.random.Random

class GameViewModel(application: Application) : AndroidViewModel(application) {

    private val context = getApplication<Application>().applicationContext
    private var _allTasks: Map<String, List<String>> = emptyMap()
    val allCategories: List<String> get() = _allTasks.keys.toList()

    var gameTasks: LinkedList<String> = LinkedList<String>()

    private val _selectedCategories = MutableStateFlow<List<String>>(emptyList())
    val selectedCategories: StateFlow<List<String>> = _selectedCategories

    private val _players = MutableStateFlow<List<String>>(emptyList())
    val players = _players.asStateFlow()

    private val _currentTask = MutableStateFlow<Node<String>?>(null)
    val currentTask = _currentTask.asStateFlow()

    private val _gameEnded = MutableStateFlow(false)
    val gameEnded = _gameEnded.asStateFlow()

    private val _categoriesConfirmed = MutableStateFlow(false)
    val categoriesConfirmed: StateFlow<Boolean> = _categoriesConfirmed

    init {
        loadAllTasks()
    }

    fun confirmCategories() {
        _categoriesConfirmed.value = true
        startGame()
    }

    fun setPlayers(playerList: List<String>) {
        _players.value = playerList
    }

    fun loadAllTasks() {
        viewModelScope.launch {
            val inputStream = context.assets.open("tasks.json")
            val jsonStr = inputStream.bufferedReader().use { it.readText() }

            val jsonObj = JSONObject(jsonStr)
            val loaded = mutableMapOf<String, List<String>>()

            for (key in jsonObj.keys()) {
                val list = jsonObj.getJSONArray(key)
                loaded[key] = List(list.length()) { list.getString(it) }
            }

            _allTasks = loaded
        }
    }

    fun toggleCategory(category: String) {
        _selectedCategories.value = _selectedCategories.value.toMutableList().apply {
            if (contains(category)) remove(category) else add(category)
        }
    }

    private fun startGame() {
        gameTasks.clear()
        _gameEnded.value = false

        val selected = _selectedCategories.value
        val tasks = selected.flatMap { category -> _allTasks[category] ?: emptyList() }.shuffled()

        createGameTasks(tasks.take(AppConfig.TASKS_PER_ROUND))
        _currentTask.value = gameTasks.tail
    }

    fun showNextTask() {
        if (_currentTask.value?.next == null) {
            _gameEnded.value = true
            return
        } else {
            _currentTask.value = _currentTask.value?.next
        }
    }

    private fun createGameTasks(tasks: List<String>) {
        for (task in tasks) {
            gameTasks.addValue(replacePlaceholders(task))
        }
    }

    // TODO {player} placeholder is not replaced when there are not enough players
    private fun replacePlaceholders(task: String): String {
        val players = _players.value
        val taskPlayerCount = task.split("player").size - 1
        val chosenPlayers = players.shuffled().take(taskPlayerCount)
        var transformedTask = task
        for ((index, playerName) in chosenPlayers.withIndex()) {
            transformedTask = replacePlayer(transformedTask, playerName, index)
        }

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
        val shotText = if (shots == 1) "$shots shot" else "$shots shots"
        return task.replace("{shot$number}", shotText)
    }

    fun showPreviousTask() {
        if (_gameEnded.value == true) {
            _gameEnded.value = false
        } else if (_currentTask.value?.previous != null) {
            _gameEnded.value = false
            _currentTask.value = _currentTask.value!!.previous
        }
    }

    fun restartGame() {
        startGame()
    }
}