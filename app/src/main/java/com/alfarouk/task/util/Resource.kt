package com.alfarouk.task.util


data class Resource<out T>(val status: Status, val data: T?, val message: ErrorType?) {
    companion object {
        fun <T> success(data: T): Resource<T> =
            Resource(status = Status.SUCCESS, data = data, message = null)

        fun <T> loading(): Resource<T> =
            Resource(status = Status.LOADING, data = null, message = null)

        fun <T> error(data: T?, message: ErrorType): Resource<T> =
            Resource(status = Status.ERROR, data = data, message = message)

    }
}