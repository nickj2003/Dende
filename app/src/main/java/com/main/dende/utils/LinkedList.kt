package com.main.dende.utils

class LinkedList<T> {
    var tail: Node<T>? = null
    var head: Node<T>? = null
    var size: Int = 0

    fun isEmpty(): Boolean {
        return tail == null
    }

    fun addValue(valueToAdd: T) {
        if (tail == null) {
            var newNode = Node<T>(valueToAdd, 0)
            tail = newNode
            head = newNode
        } else {
            var newNode = Node<T>(valueToAdd, head?.index?.plus(1) ?: 0, head)
            head?.next = newNode
            head = newNode
        }
        size++
    }

    fun clear() {
        tail = null
        head = null
        size = 0
    }
}