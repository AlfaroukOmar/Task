package com.alfarouk.task.util

enum class ErrorType(var message: String) {
    UNKNOWN("Unknown error occurred"),
    NO_CONTENT("Content not found"),
    NETWORK("Network error occurred"),
    MSG("");

    companion object {
        fun fromMessage(message: String?): ErrorType {
            return when (message) {
                "No Content" -> NO_CONTENT
                "Network Error" -> NETWORK
                "Unknown error occurred" -> UNKNOWN
                else -> MSG.apply { this.message = message ?: "" }
            }
        }
    }
}