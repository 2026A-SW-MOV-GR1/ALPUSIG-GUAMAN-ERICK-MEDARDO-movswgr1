package com.example.dualpersistence_e1b.model

import kotlinx.serialization.Serializable

@Serializable
data class Item(
    val id: String,
    val title: String,
    val description: String,
    val createdAt: Long
)

