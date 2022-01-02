package com.zzz.home.ui

import androidx.compose.runtime.Composable
import com.zzz.role.RoleView
import com.zzz.user.UserView
import com.zzz.util.ViewType
import com.zzz.role.RoleViewModel
import com.zzz.user.UserViewModel

@Composable
fun PageFactoryView(vt: ViewType) {
    when (vt) {
        ViewType.USERVIEW -> UserView(UserViewModel())
        ViewType.ROLEVIEW -> {
            println("---------role view create-------------")
            RoleView(RoleViewModel())
        }
        ViewType.PERMISSIONVIEW -> UserView(UserViewModel())
        else -> {
            println("--------no match any view type-----------")
        }
    }
}