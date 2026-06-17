package com.example.comunicacion_inter_app

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform