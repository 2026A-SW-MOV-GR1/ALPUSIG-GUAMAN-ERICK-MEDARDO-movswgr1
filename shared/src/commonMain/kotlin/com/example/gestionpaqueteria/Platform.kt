package com.example.gestionpaqueteria

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform