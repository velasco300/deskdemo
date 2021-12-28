package com.zzz

import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import com.zzz.entity.Menu
import com.zzz.entity.Menus
import com.zzz.ui.RootView

fun main() {
    application {
        Window(
            title = "shoping",
            onCloseRequest = ::exitApplication,
            state = rememberWindowState(width = 900.dp, height = 500.dp)
        ) {
            MainView()
        }
    }
}

@Composable
fun MainView() {

    var m = remember {
        var obj = Menu(0, "", 0, true, null, null)
        obj.root = obj
        obj
    }
    Menus.runinsession { Menus.flush(m) }

    MaterialTheme {
        Surface {
            RootView(m)
        }
    }
}


