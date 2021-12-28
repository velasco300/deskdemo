package com.zzz.entity

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

class Menu(
    val id: Int, var name: String, val level: Int, val isLeaf: Boolean, var parent: Menu?, var root: Menu?
) {
    var children: List<Menu> by mutableStateOf(emptyList())
    val items: List<Menu> get() = toItems()

    private fun dirAdd(rootNode: Menu, list: MutableList<Menu>) {
        list.add(rootNode)
        rootNode.children.forEach {
            dirAdd(it, list)
        }
    }

    private fun toItems(): List<Menu> {
        var arr = mutableListOf<Menu>()
        children.forEach { dirAdd(it, arr) }
        return arr
    }

}