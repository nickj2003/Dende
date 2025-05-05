package com.main.dende.utils

data class Node<T>(
    var value: T,
    var index: Int,
    var previous: Node<T>? = null,
    var next: Node<T>? = null,
)
