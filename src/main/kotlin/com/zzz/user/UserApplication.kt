package com.zzz.user

import com.zzz.user.Users.inSession

class UserApplication {

    fun add(vo: User): List<User> {
        return Users.inSession {
            add(vo)
            getAll()
        }
    }

    fun delete(id: Int): List<User> {
        return Users.inSession {
            remove(id)
            getAll()
        }
    }

    fun update(vo: User) {
        Users.inSession { modify(vo) }
    }

    fun get(id: Int): User? {
        return Users.inSession { get(id) }
    }

    fun getAll(pageNum: Int = 1, pageSize: Int = 10): List<User> {
        return Users.inSession { getAll(pageNum, pageSize) }
    }

}
