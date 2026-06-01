package com.example.dualpersistence_e1b.util

fun logDebug(message: String) {
    println("[DEBUG] $message")
}

fun logInfo(message: String) {
    println("[INFO] $message")
}

fun logError(message: String, throwable: Throwable? = null) {
    val suffix = if (throwable == null) "" else ": ${throwable.message}"
    println("[ERROR] $message$suffix")
}

