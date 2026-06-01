package com.example.redyseguridad_p1b

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform