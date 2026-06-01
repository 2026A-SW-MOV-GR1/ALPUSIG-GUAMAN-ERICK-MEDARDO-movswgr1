package com.example.dualpersistence_e1b.repository

import com.example.dualpersistence_e1b.model.Item
import com.example.dualpersistence_e1b.util.logDebug
import com.example.dualpersistence_e1b.util.logError
import com.example.dualpersistence_e1b.util.logInfo
import io.github.xxfast.kstore.KStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart

class NoSqlItemRepository(
    private val store: KStore<List<Item>>
) : ItemRepository {

    override val items: Flow<List<Item>> = store.updates
        .map { it ?: emptyList() }
        .onStart { logDebug("Leyendo items desde NoSQL") }

    override suspend fun upsert(item: Item) {
        runCatching {
            val current = store.get().orEmpty().toMutableList()
            val index = current.indexOfFirst { it.id == item.id }
            if (index >= 0) {
                current[index] = item
            } else {
                current.add(item)
            }
            store.set(current)
            logInfo("NoSQL insert/update: ${item.id}")
        }.onFailure {
            logError("NoSQL insert/update fallo", it)
            throw it
        }
    }

    override suspend fun update(item: Item) {
        runCatching {
            val current = store.get().orEmpty().toMutableList()
            val index = current.indexOfFirst { it.id == item.id }
            if (index >= 0) {
                current[index] = item
                store.set(current)
            }
            logInfo("NoSQL update: ${item.id}")
        }.onFailure {
            logError("NoSQL update fallo", it)
            throw it
        }
    }

    override suspend fun delete(id: String) {
        runCatching {
            val current = store.get().orEmpty()
            val updated = current.filterNot { it.id == id }
            store.set(updated)
            logInfo("NoSQL delete: $id")
        }.onFailure {
            logError("NoSQL delete fallo", it)
            throw it
        }
    }
}

