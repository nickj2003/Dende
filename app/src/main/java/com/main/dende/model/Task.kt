package com.main.dende.model

data class Task(
    val category: String,
    val tasks: List<String>
)

data class TaskData(
    val tasksByCategory: Map<String, List<String>>
)
