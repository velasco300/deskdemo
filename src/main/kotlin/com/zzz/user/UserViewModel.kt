package com.zzz.user

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

class UserViewModel {

    var items by mutableStateOf(emptyList<User>())
    private val userApplication = UserApplication()

    init {
        items = userApplication.getAll()
    }

    fun add(vo: User) {
        items = userApplication.add(vo)
    }

    fun delete(vo: User) {
        items = userApplication.delete(vo.id)
    }

    fun update(vo: User) {
        userApplication.update(vo)
    }

    fun show(vo: User): User {
        return userApplication.get(vo.id) ?: vo
    }

    fun index(vo: User) {
        items = userApplication.getAll(vo.pageNum, vo.pageSize)
    }
}