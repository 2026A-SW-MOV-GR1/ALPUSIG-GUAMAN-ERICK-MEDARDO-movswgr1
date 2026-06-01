package com.example.dualpersistence_e1b.repository

import com.example.dualpersistence_e1b.model.StorageMode
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest

@OptIn(ExperimentalCoroutinesApi::class)
class DualRepositoryProvider(
    private val sqlRepository: ItemRepository,
    private val noSqlRepository: ItemRepository,
    private val toggleManager: StorageToggleManager
) {
    val items: Flow<List<com.example.dualpersistence_e1b.model.Item>> = toggleManager.mode.flatMapLatest { mode ->
        when (mode) {
            StorageMode.SQL -> sqlRepository.items
            StorageMode.NOSQL -> noSqlRepository.items
        }
    }

    fun activeRepository(): ItemRepository {
        return when (toggleManager.mode.value) {
            StorageMode.SQL -> sqlRepository
            StorageMode.NOSQL -> noSqlRepository
        }
    }
}
