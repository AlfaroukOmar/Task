package com.alfarouk.task.util

class Paginator constructor(
    var prevLastPage: Int? = null,
    var lastPage: Int? = null,
    var isPaginationEnd: Boolean = false,
    var isOnline: Boolean = true,
) {
    var isLoading = false

    fun canLoadNextPage(): Boolean = !isLoading && !isPaginationEnd && isOnline

    fun resetPage() {
        lastPage = null
        isPaginationEnd = false
        isOnline = true
        prevLastPage = null
    }


}