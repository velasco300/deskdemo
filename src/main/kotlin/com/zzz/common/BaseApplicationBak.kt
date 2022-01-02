package com.zzz.common
/*
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import kotlin.reflect.KMutableProperty

abstract class BaseApplication<T : BaseTable>(private val table: T) {

    private fun <E> fillValue(k: String, v: E, obj: Table) {
        table::class.members.forEach {
            if (it.name == k) {
                when (it) {
                    is KMutableProperty -> it.setter.call(obj, v)
                    else -> println(it.name)
                }
            }
        }
    }

    fun <E> add(entity: E) {
        table.insert {
            entity!!::class.members.forEach {
                fillValue(it.name, it.call(entity), this)
            }
        }
    }

    fun delete(id: Int) {
        table.deleteWhere {
            table.id eq id
        }
    }

}
 */