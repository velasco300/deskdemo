package com.zzz.ui

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.zzz.entity.Menu
import com.zzz.entity.Menus

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MenuView(m: Menu) {
    Box {
        val state = rememberLazyListState()
        LazyColumn(modifier = Modifier.fillMaxSize(), state = state) {
            items(m.items.size) {
                var node = m.items[it]
                Spacer(Modifier.height(1.dp))
                Row(
                    modifier = Modifier.fillMaxWidth().padding(start = 10.dp * node.level)
                ) {
                    Text(text = "好", modifier = Modifier.background(color = Color.Blue).width(30.dp).clickable {
                        println("==========--expand---${node.name}--${node.isLeaf}--${node.root}------")
                        Menus.runinsession {
                            Menus.toggleExpand(node)
                        }
                    })

                    Text(text = node.name, modifier = Modifier.background(color = Color.Red).clickable {
                        println("-------------add---${node.name}----${node.isLeaf}-----")
                        Menus.runinsession {
                            Menus.add(
                                Menu(
                                    -1, "${node.name}${(1..10).random()}", node.level + 1, true, node, node.root
                                )
                            )
                        }
                    })

                    Text(text = "对", modifier = Modifier.background(color = Color.Blue).width(30.dp).clickable {
                        println("==========delete-----${node.name}--${node.isLeaf}--------")
                        Menus.runinsession {
//                            node.name = "M${(1..10).random()}"
//                            Menus.modify(node)

                            Menus.remove(node)

//                            var oldParent = node.parent
//                            node.parent = Menu(0, "", 1, false, null, node.root)
//                            Menus.modifyParent(node, oldParent!!)
                        }
                    })
                }
            }
        }
        VerticalScrollbar(adapter = rememberScrollbarAdapter(state), modifier = Modifier.align(Alignment.CenterEnd))
    }
}
