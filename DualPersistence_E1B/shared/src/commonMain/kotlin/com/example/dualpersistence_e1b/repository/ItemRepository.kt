package com.example.dualpersistence_e1b.repository

import com.example.dualpersistence_e1b.model.Item
import kotlinx.coroutines.flow.Flow

interface ItemRepository {
    val items: Flow<List<Item>>

    suspend fun upsert(item: Item)
    suspend fun update(item: Item)
    suspend fun delete(id: String)
}

