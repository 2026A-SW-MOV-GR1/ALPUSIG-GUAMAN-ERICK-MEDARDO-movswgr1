package com.example.dualpersistence_e1b.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dualpersistence_e1b.model.Item
import com.example.dualpersistence_e1b.model.StorageMode
import com.example.dualpersistence_e1b.repository.DualRepositoryProvider
import com.example.dualpersistence_e1b.repository.StorageToggleManager
import com.example.dualpersistence_e1b.util.nowEpochMillis
import com.example.dualpersistence_e1b.util.randomId
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ItemViewModel(
    private val repositoryProvider: DualRepositoryProvider,
    private val toggleManager: StorageToggleManager
) : ViewModel() {

    val items: StateFlow<List<Item>> = repositoryProvider.items
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    val mode: StateFlow<StorageMode> = toggleManager.mode

    fun onModeChanged(newMode: StorageMode) {
        toggleManager.setMode(newMode)
    }

    fun addItem(title: String, description: String) {
        val item = Item(
            id = randomId(),
            title = title.trim(),
            description = description.trim(),
            createdAt = nowEpochMillis()
        )
        viewModelScope.launch {
            repositoryProvider.activeRepository().upsert(item)
        }
    }

    fun updateItem(id: String, title: String, description: String, createdAt: Long) {
        val item = Item(
            id = id,
            title = title.trim(),
            description = description.trim(),
            createdAt = createdAt
        )
        viewModelScope.launch {
            repositoryProvider.activeRepository().update(item)
        }
    }

    fun deleteItem(id: String) {
        viewModelScope.launch {
            repositoryProvider.activeRepository().delete(id)
        }
    }
}
