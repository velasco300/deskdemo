package com.zzz.home

import com.zzz.home.domain.Menu

class HomeApplication {

    fun addMenu(m: Menu): List<Menu> {
        return Menus.runinsession {
            add(m)
            filterFlush(m.parent!!.parent!!, listOf(m.parent!!.id, 0), 2)
        }
    }

    fun deleteMenu(m: Menu) {
        Menus.runinsession {
            remove(m.id, m.parent!!.id)
            if (m.parent!!.children.size < 2) {
                if (m.parent!!.id > 0) {
                    m.parent!!.parent?.let { it.children = filterFlush(it, listOf(m.parent!!.id, 0), 2) }
                } else {
                    m.parent!!.children = getChildren(m.parent!!)
                }
            } else {
                m.parent!!.children = getChildren(m.parent!!)
            }
        }
    }

    fun updateMenu(m: Menu): List<Menu> {
        return Menus.runinsession {
            modify(m)
            getChildren(m.parent!!)
        }
    }

    fun moveMenu(m: Menu, oldParent: Menu): List<Menu> {
        return Menus.runinsession {
            var pids = modifyParent(m.id, m.parent!!.id, oldParent.id)
            filterFlush(m.root!!, pids, pids.size - 1, 1)
        }
    }

    fun toggleExpandMenu(m: Menu): List<Menu> {
        return Menus.runinsession {
            if (m.children.isEmpty()) {
                getChildren(m)
            } else {
                emptyList<Menu>()
            }
        }
    }
}