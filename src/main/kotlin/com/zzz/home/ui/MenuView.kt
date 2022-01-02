package com.zzz.home.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.zzz.home.HomeViewModel

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MenuView(vm: HomeViewModel) {
    Box {
        val state = rememberLazyListState()
        LazyColumn(modifier = Modifier.fillMaxSize(), state = state) {
            items(vm.menu.items.size) {
                var node = vm.menu.items[it]
                Spacer(Modifier.height(1.dp))
                Row(modifier = Modifier.fillMaxWidth().padding(start = 10.dp * node.level)
                    .clickable { vm.toggleExpandMenu(node) }) {
                    Text(text = node.name)
                }
            }
        }
        VerticalScrollbar(adapter = rememberScrollbarAdapter(state), modifier = Modifier.align(Alignment.CenterEnd))
    }
}
