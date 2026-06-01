package com.example.dualpersistence_e1b.repository

import com.example.dualpersistence_e1b.model.StorageMode
import com.example.dualpersistence_e1b.util.logInfo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class StorageToggleManager(initialMode: StorageMode = StorageMode.SQL) {
    private val _mode = MutableStateFlow(initialMode)
    val mode: StateFlow<StorageMode> = _mode.asStateFlow()

    fun setMode(newMode: StorageMode) {
        if (_mode.value != newMode) {
            _mode.value = newMode
            logInfo("Modo activo: $newMode")
        }
    }

    fun toggle() {
        val next = if (_mode.value == StorageMode.SQL) StorageMode.NOSQL else StorageMode.SQL
        setMode(next)
    }
}

