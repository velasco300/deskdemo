package com.zzz.common

abstract class BaseVO {
    var pageNum = 1
    var pageSize = 10
    val offset: Int
        get() {
            pageNum -= 1
            if (pageNum < 0) {
                pageNum = 0
            }
            return pageNum * pageSize
        }
}