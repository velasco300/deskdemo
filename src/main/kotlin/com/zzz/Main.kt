package com.zzz

import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import com.zzz.common.AppTheme
import com.zzz.home.HomeViewModel
import com.zzz.home.ui.HomeView

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
    MaterialTheme(colors = AppTheme.colors.material) {
        Surface {
            HomeView(HomeViewModel())
        }
    }
}


