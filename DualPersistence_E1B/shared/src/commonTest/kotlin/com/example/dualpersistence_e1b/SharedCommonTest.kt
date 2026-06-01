package com.example.dualpersistence_e1b

import com.example.dualpersistence_e1b.model.Item
import com.example.dualpersistence_e1b.model.StorageMode
import com.example.dualpersistence_e1b.repository.DualRepositoryProvider
import com.example.dualpersistence_e1b.repository.ItemRepository
import com.example.dualpersistence_e1b.repository.StorageToggleManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class SharedCommonTest {

    @Test
    fun inMemoryRepositoryUpsertPersists() = runTest {
        val repo = FakeItemRepository()
        val item = Item("1", "Titulo", "Descripcion", 123L)

        repo.upsert(item)

        val result = repo.items.first()
        assertTrue(result.any { it.id == "1" && it.title == "Titulo" })
    }

    @Test
    fun toggleChangesActiveSource() = runTest {
        val sqlItem = Item("sql", "SQL", "Solo SQL", 10L)
        val noSqlItem = Item("nosql", "NoSQL", "Solo NoSQL", 20L)
        val sqlRepo = FakeItemRepository(initial = listOf(sqlItem))
        val noSqlRepo = FakeItemRepository(initial = listOf(noSqlItem))
        val toggle = StorageToggleManager(StorageMode.SQL)
        val provider = DualRepositoryProvider(sqlRepo, noSqlRepo, toggle)

        val first = provider.items.first()
        assertEquals(listOf(sqlItem), first)

        toggle.setMode(StorageMode.NOSQL)
        val second = provider.items.first()
        assertEquals(listOf(noSqlItem), second)
    }
}

private class FakeItemRepository(
    initial: List<Item> = emptyList()
) : ItemRepository {
    private val state = MutableStateFlow(initial)
    override val items: Flow<List<Item>> = state

    override suspend fun upsert(item: Item) {
        val current = state.value.toMutableList()
        val index = current.indexOfFirst { it.id == item.id }
        if (index >= 0) current[index] = item else current.add(item)
        state.value = current
    }

    override suspend fun update(item: Item) {
        val current = state.value.toMutableList()
        val index = current.indexOfFirst { it.id == item.id }
        if (index >= 0) {
            current[index] = item
            state.value = current
        }
    }

    override suspend fun delete(id: String) {
        state.value = state.value.filterNot { it.id == id }
    }
}