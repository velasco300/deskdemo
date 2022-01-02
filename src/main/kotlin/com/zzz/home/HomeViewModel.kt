package com.zzz.home

import com.zzz.home.domain.Menu
import com.zzz.home.domain.Tabs
import com.zzz.util.ViewType

class HomeViewModel {
//    var pageFuns = mutableMapOf<String, (vm: MainViewModel) -> Unit>().apply {
//        put("UserView", ::UserView)
//    }

    var menu = Menu(0, "根节点", 0, true, ViewType.NONE, null, null).apply {
        root = this
        this.children = Menus.runinsession { getChildren(this@apply) }
    }
    var tabs = Tabs()
    private val homeApplication = HomeApplication()

    fun addMenu(m: Menu) {
        m.parent!!.parent!!.children = homeApplication.addMenu(m)
    }

    fun deleteMenu(m: Menu) {
        homeApplication.deleteMenu(m)
    }

    fun updateMenu(m: Menu) {
        m.parent!!.children = homeApplication.updateMenu(m)
    }

    fun moveMenu(m: Menu, oldParent: Menu) {
        m.root!!.children = homeApplication.moveMenu(m, oldParent)
    }

    fun toggleExpandMenu(m: Menu) {
        if (m.isLeaf) {
            tabs.open(m.id, m.name, m.url)
        } else {
            m.children = homeApplication.toggleExpandMenu(m)
        }
    }
}