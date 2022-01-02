package com.zzz.user

import com.zzz.common.BaseTable
import org.jetbrains.exposed.sql.*

object Users : BaseTable() {
    val userName = varchar("user_name", 255)
    val age = integer("age").default(0)

    fun add(vo: User): Int {
        return insert {
            it[userName] = vo.userName
            it[age] = vo.age
        } get id
    }

    fun modify(vo: User) {
        update({ id eq vo.id }) {
            it[userName] = vo.userName
            it[age] = vo.age
        }
    }

    fun get(id: Int): User? {
        return select { Users.id eq id }.map { User(it[Users.id], it[userName], it[age]) }.firstOrNull()
    }

    fun getAll(pageNum: Int = 1, pageSize: Int = 10): List<User> {
        var page = pageNum - 1
        if (page < 0) {
            page = 0
        }
        return selectAll().orderBy(id, SortOrder.DESC).limit(pageSize, (page * pageSize).toLong())
            .map { User(it[id], it[userName], it[age]) }
    }

}