package com.zzz.role

import org.jetbrains.exposed.sql.Table

object Roles : Table() {
    val id = integer("id").autoIncrement()
    val name = varchar("role_name", 255)
    override val primaryKey: PrimaryKey = PrimaryKey(id)
}