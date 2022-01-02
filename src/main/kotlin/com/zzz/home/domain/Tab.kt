package com.zzz.home.domain

import com.zzz.util.SingleSelection

class Tab(val id: Int, val header: String, val body: Any, var selection: SingleSelection) {

    val isActive: Boolean get() = selection.selected === this
    var close: (() -> Unit)? = null

    fun activate() {
        selection.selected = this
    }

}