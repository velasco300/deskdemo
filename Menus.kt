package com.zzz.entity

import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

object Menus : Table() {
    val id = integer("id").autoIncrement()
    val pid = integer("pid").default(0)
    val mname = varchar("mname", 255)
    val isLeaf = bool("is_leaf").default(true)

    override val primaryKey = PrimaryKey(id)

    fun runinsession(ct: () -> Unit) {
        Database.connect("jdbc:mysql://localhost:3306/zzz", "com.mysql.jdbc.Driver", "root", "")
        transaction {
            addLogger(StdOutSqlLogger)
            ct()
        }
    }

    fun add(m: Menu) {
        insert {
            it[mname] = m.name
            it[pid] = m.parent!!.id
        }
        m.parent?.let {
            if (it.isLeaf) {
                modifyIsLeaf(it, false)
            }
        }
        m.parent!!.parent!!.children = filterFlush(m.parent!!.parent!!, listOf(m.parent, null), 2)
    }

    fun remove(m: Menu) {
        dirDelete(m)
        deleteWhere {
            Menus.id eq m.id
        }
        if (m.parent!!.children.size < 2) {
            modifyIsLeaf(m.parent!!, true)
            if (m.parent!!.id > 0) {
                m.parent!!.parent?.let { flush(it) }
            } else {
                flush(m.parent!!)
            }
        } else {
            flush(m.parent!!)
        }
    }

    fun modify(m: Menu) {
        update({ Menus.id eq m.id }) {
            it[mname] = m.name
        }
        flush(m.parent!!)
    }

    fun modifyParent(m: Menu, oldParent: Menu) {
        m.parent?.let {
            if (it.id == m.id) {
                throw RuntimeException("parent id eq self id")
            }
        }
        var arr = mutableListOf<Menu>()
        dirGet(m, arr)
        if (arr[0].parent!!.id != 0) {
            throw RuntimeException("树形关系错误，没有找到根节点")
        }
        update({ Menus.id eq m.id }) {
            it[pid] = m.parent!!.id
        }
        modifyIsLeaf(m.parent!!, false)
        select { Menus.pid eq oldParent.id }.firstOrNull() ?: modifyIsLeaf(oldParent, true)

        m.root!!.children = filterFlush(m.root!!, arr, arr.size)
    }

    fun toggleExpand(m: Menu) {
        if (m.children.isEmpty()) {
            m.children = getChildren(m)
        } else {
            m.children = emptyList()
        }
    }

    fun flush(m: Menu, deep: Int = 1) {
        m.children = getChildren(m, deep)
    }

    private fun expand(rootNode: Menu): List<Menu> {
        return select { Menus.pid eq rootNode.id }.map { rs ->
            Menu(rs[id], rs[mname], rootNode.level + 1, rs[isLeaf], rootNode, rootNode.root).also {
                it.children = expand(it)
            }
        }
    }

    private fun getChildren(rootNode: Menu, lv: Int = 1): List<Menu> {
        var deep = lv - 1
        return select { Menus.pid eq rootNode.id }.map { rs ->
            Menu(rs[id], rs[mname], rootNode.level + 1, rs[isLeaf], rootNode, rootNode.root).also {
                if (deep > 0) {
                    it.children = getChildren(it, deep)
                }
            }
        }
    }

    private fun dirDelete(m: Menu) {
        select { Menus.pid eq m.id }.forEach {
            dirDelete(Menu(it[id], it[mname], m.level + 1, it[isLeaf], m, m.root))
            deleteWhere {
                Menus.id eq it[id]
            }

        }
    }

    private fun dirGet(m: Menu, arr: MutableList<Menu>) {
        select { Menus.id eq m.parent!!.id }.map {
            Menu(
                it[id], it[mname], m.level - 1, it[isLeaf], Menu(it[pid], "", m.level - 2, false, null, m.root), m.root
            )
        }.firstOrNull()?.let {
            dirGet(it, arr)
        }
        arr.add(m)
    }

    private fun modifyIsLeaf(m: Menu, flag: Boolean) {
        if (m.id > 0) {
            update({ Menus.id eq m.id }) {
                it[isLeaf] = flag
            }
        }
    }

    private fun filterFlush(rootNode: Menu, arr: List<Menu?>, lv: Int = 1, index: Int = 0): List<Menu> {
        if (arr.size < lv) {
            throw RuntimeException("the num is not eq")
        }
        println("************ ${index} ${arr[index]?.let { it.id }}")
        var deep = lv - 1
        return select { Menus.pid eq rootNode.id }.map { rs ->
            Menu(rs[id], rs[mname], rootNode.level + 1, rs[isLeaf], rootNode, rootNode.root).also {
                if (deep > 0) {
                    var m = arr[index]
                    if (m != null) {
                        if (m.id == it.id) {
                            it.children = filterFlush(it, arr, deep, index + 1)
                        }
                    } else {
                        it.children = filterFlush(it, arr, deep, index + 1)
                    }
                }
            }
        }
    }

}