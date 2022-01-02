package com.zzz.home

import com.zzz.home.domain.Menu
import com.zzz.util.ViewType
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

object Menus : Table() {
    val id = integer("id").autoIncrement()
    val pid = integer("pid").default(0)
    val name = varchar("menu_name", 255)
    val isLeaf = bool("is_leaf").default(true)
    val url = varchar("url", 255).default("")

    override val primaryKey = PrimaryKey(id)

    fun <R> runinsession(block: Menus.() -> R): R {
        Database.connect("jdbc:mysql://localhost:3306/zzz", "com.mysql.jdbc.Driver", "root", "")
        return transaction {
            addLogger(StdOutSqlLogger)
            block()
        }
    }

    fun add(m: Menu) {
        insert {
            it[name] = m.name
            it[pid] = m.parent!!.id
            it[url] = m.url.name
            it[isLeaf] = m.isLeaf
        }
        m.parent?.let {
            if (it.isLeaf) {
                modifyIsLeaf(it.id, false)
            }
        }
    }

    fun remove(id: Int, pid: Int) {
        dirDelete(id)
        deleteWhere {
            Menus.id eq id
        }
        select { Menus.pid eq pid }.firstOrNull() ?: modifyIsLeaf(pid, true)
    }

    fun modify(m: Menu) {
        update({ Menus.id eq m.id }) {
            it[name] = m.name
            it[url] = m.url.name
        }
    }

    fun modifyParent(id: Int, newPid: Int, oldPid: Int): List<Int> {
        if (id == newPid) {
            throw RuntimeException("parent id eq self id")
        }
        var pids = mutableListOf<Int>()
        dirGet(newPid, pids)
        pids.add(id)
        if (pids[0] != 0) {
            throw RuntimeException("树形关系错误，没有找到根节点")
        }
        update({ Menus.id eq id }) {
            it[pid] = newPid
        }
        modifyIsLeaf(newPid, false)
        select { Menus.pid eq oldPid }.firstOrNull() ?: modifyIsLeaf(oldPid, true)
        return pids
    }

    fun getChildren(rootNode: Menu, lv: Int = 1): List<Menu> {
        var deep = lv - 1
        return select { pid eq rootNode.id }.map { rs ->
            Menu(
                rs[id], rs[name], rootNode.level + 1, rs[isLeaf], ViewType.valueOf(rs[url]), rootNode, rootNode.root
            ).also {
                if (deep > 0) {
                    it.children = getChildren(it, deep)
                }
            }
        }
    }

    fun filterFlush(rootNode: Menu, arr: List<Int>, lv: Int, index: Int = 0): List<Menu> {
        if (arr.size < lv) {
            throw RuntimeException("the num is not eq")
        }
        var deep = lv - 1
        return select { pid eq rootNode.id }.map { rs ->
            Menu(
                rs[id], rs[name], rootNode.level + 1, rs[isLeaf], ViewType.valueOf(rs[url]), rootNode, rootNode.root
            ).also {
                if (deep > 0) {
                    var filterId = arr[index]
                    if (filterId > 0) {
                        if (filterId == it.id) {
                            it.children = filterFlush(it, arr, deep, index + 1)
                        }
                    } else {
                        it.children = filterFlush(it, arr, deep, index + 1)
                    }
                }
            }
        }
    }

    private fun expand(rootNode: Menu, loopValues: MutableSet<Int> = mutableSetOf()): List<Menu> {
        if (!loopValues.add(rootNode.id)) {
            throw RuntimeException("循环依赖值：${rootNode.id}")
        }
        return select { pid eq rootNode.id }.map { rs ->
            Menu(
                rs[id], rs[name], rootNode.level + 1, rs[isLeaf], ViewType.valueOf(rs[url]), rootNode, rootNode.root
            ).also {
                it.children = expand(it, loopValues)
            }
        }
    }

    private fun dirDelete(menuId: Int, loopValues: MutableSet<Int> = mutableSetOf()) {
        if (!loopValues.add(menuId)) {
            throw RuntimeException("循环依赖值：${menuId}")
        }
        select { Menus.pid eq menuId }.forEach {
            dirDelete(it[id], loopValues)
            deleteWhere {
                Menus.id eq it[id]
            }
        }
    }

    private fun dirGet(id: Int, pids: MutableList<Int>, loopValues: MutableSet<Int> = mutableSetOf()) {
        if (!loopValues.add(id)) {
            throw RuntimeException("循环依赖值：${id}")
        }
        select { Menus.id eq id }.map {
            it[pid]
        }.firstOrNull()?.let {
            dirGet(it, pids, loopValues)
        }
        pids.add(id)
    }

    private fun modifyIsLeaf(id: Int, flag: Boolean) {
        update({ Menus.id eq id }) {
            it[isLeaf] = flag
        }
    }

}
