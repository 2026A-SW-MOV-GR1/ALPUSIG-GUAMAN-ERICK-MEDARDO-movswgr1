package com.example.dualpersistence_e1b

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform