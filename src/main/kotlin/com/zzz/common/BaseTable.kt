package com.zzz.common

import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

abstract class BaseTable : Table() {
    val id = integer("id").autoIncrement()
    override val primaryKey: PrimaryKey = PrimaryKey(id)

    fun <T, R> T.inSession(block: T.() -> R): R {
        Database.connect("jdbc:mysql://localhost:3306/zzz", "com.mysql.jdbc.Driver", "root", "")
        return transaction {
            addLogger(StdOutSqlLogger)
            block()
        }
    }

    fun remove(id: Int) {
        deleteWhere {
            this@BaseTable.id eq id
        }
    }

}