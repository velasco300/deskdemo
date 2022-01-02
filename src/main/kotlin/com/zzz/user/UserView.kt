package com.zzz.user

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun UserView(vm: UserViewModel) {
    LazyColumn {
        items(vm.items.size) {
            var vo = vm.items[it]
            Spacer(modifier = Modifier.height(1.dp))
            Row(modifier = Modifier.fillMaxWidth().background(color = Color.LightGray).clickable {
                vm.add(
                    User(
                        userName = "${(1000..1000000).random()}", age = (1..30).random()
                    )
                )
            }) {
                Text(text = "${vo.userName} is ${vo.age}", modifier = Modifier.clickable { vm.delete(vo) })
            }
        }
    }
}