package com.example.dualpersistence_e1b.repository

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.example.dualpersistence_e1b.db.AppDatabase
import com.example.dualpersistencee1b.db.Item as DbItem
import com.example.dualpersistence_e1b.model.Item
import com.example.dualpersistence_e1b.util.logDebug
import com.example.dualpersistence_e1b.util.logError
import com.example.dualpersistence_e1b.util.logInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.withContext

class SQLiteItemRepository(
    private val database: AppDatabase
) : ItemRepository {

    override val items: Flow<List<Item>> = database.itemQueries
        .getAllItems()
        .asFlow()
        .mapToList(Dispatchers.IO)
        .map { list -> list.map { it.toDomain() } }
        .onStart { logDebug("Leyendo items desde SQLite") }

    override suspend fun upsert(item: Item) {
        runCatching {
            withContext(Dispatchers.IO) {
                database.itemQueries.insertItem(
                    id = item.id,
                    title = item.title,
                    description = item.description,
                    createdAt = item.createdAt
                )
            }
            logInfo("SQLite insert/update: ${item.id}")
        }.onFailure {
            logError("SQLite insert/update fallo", it)
            throw it
        }
    }

    override suspend fun update(item: Item) {
        runCatching {
            withContext(Dispatchers.IO) {
                database.itemQueries.updateItem(
                    title = item.title,
                    description = item.description,
                    id = item.id
                )
            }
            logInfo("SQLite update: ${item.id}")
        }.onFailure {
            logError("SQLite update fallo", it)
            throw it
        }
    }

    override suspend fun delete(id: String) {
        runCatching {
            withContext(Dispatchers.IO) {
                database.itemQueries.deleteItem(id)
            }
            logInfo("SQLite delete: $id")
        }.onFailure {
            logError("SQLite delete fallo", it)
            throw it
        }
    }

    private fun DbItem.toDomain(): Item {
        return Item(
            id = id,
            title = title,
            description = description,
            createdAt = createdAt
        )
    }
}
