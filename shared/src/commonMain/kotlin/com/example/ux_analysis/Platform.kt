package com.example.ux_analysis

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform