package com.example.redyseguridad_p1b

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.MutableStateFlow

class SecretViewModel(private val repo: SecretRepository) {
    var key by mutableStateOf("")
    var value by mutableStateOf("")
    var selectedStorage by mutableStateOf(StorageType.SHARED_PREFERENCES)

    var retrieveKey by mutableStateOf("")
    var retrieveStorage by mutableStateOf(StorageType.SHARED_PREFERENCES)

    val uiState = MutableStateFlow<SecretUiState>(SecretUiState.Idle)

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    fun saveSecret() {
        val cleanKey = key.trim()
        if (cleanKey.isEmpty()) {
            uiState.value = SecretUiState.Error("Llave requerida")
            return
        }

        uiState.value = SecretUiState.Saving
        scope.launch {
            repo.saveSecret(SecretEntry(cleanKey, value, selectedStorage))
                .onSuccess {
                    uiState.value = SecretUiState.Saved
                }
                .onFailure { error ->
                    uiState.value = SecretUiState.Error(error.message ?: "Error al guardar")
                }
        }
    }

    fun retrieveSecret() {
        val cleanKey = retrieveKey.trim()
        if (cleanKey.isEmpty()) {
            uiState.value = SecretUiState.Error("Llave requerida")
            return
        }

        uiState.value = SecretUiState.Saving
        scope.launch {
            repo.getSecret(cleanKey, retrieveStorage)
                .onSuccess { value ->
                    uiState.value = if (value == null) {
                        SecretUiState.NotFound
                    } else {
                        SecretUiState.Retrieved(value)
                    }
                }
                .onFailure { error ->
                    uiState.value = SecretUiState.Error(error.message ?: "Error al recuperar")
                }
        }
    }

    fun close() {
        scope.cancel()
    }
}

