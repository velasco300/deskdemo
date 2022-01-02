package com.zzz.home.domain

import androidx.compose.runtime.mutableStateListOf
import com.zzz.util.SingleSelection
import com.zzz.util.ViewType

class Tabs {
    private val selection = SingleSelection()
    var items = mutableStateListOf<Tab>()
        private set
    val active: Tab? get() = selection.selected as Tab?

    fun open(id: Int, header: String, body: ViewType) {
        items.filter { it.id == id }.firstOrNull().let {
            if (it == null) {
                val tab = Tab(id, header, body, selection)
                tab.close = {
                    close(tab)
                }
                items.add(tab)
                tab.activate()
            } else {
                it.activate()
            }
        }
    }

    private fun close(tab: Tab) {
        val index = items.indexOf(tab)
        items.remove(tab)
        if (tab.isActive) {
            selection.selected = items.getOrNull(index.coerceAtMost(items.lastIndex))
        }
    }
}