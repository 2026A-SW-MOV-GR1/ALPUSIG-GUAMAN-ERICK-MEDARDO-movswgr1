package com.example.ciclo_de_vida

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform