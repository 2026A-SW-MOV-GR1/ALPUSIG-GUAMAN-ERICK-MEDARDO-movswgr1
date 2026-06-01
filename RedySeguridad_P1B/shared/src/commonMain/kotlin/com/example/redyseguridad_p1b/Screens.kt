package com.example.redyseguridad_p1b

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

@Composable
fun PostScreen(viewModel: PostViewModel) {
    val uiState by viewModel.uiState.collectAsState()
    val isLoading = uiState is PostUiState.Loading
    val hasPost = uiState is PostUiState.Success || uiState is PostUiState.UpdateSuccess

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("Consulta POST", style = MaterialTheme.typography.titleMedium)

        Row(verticalAlignment = Alignment.CenterVertically) {
            OutlinedTextField(
                value = viewModel.postId,
                onValueChange = { input ->
                    viewModel.postId = input.filter { it.isDigit() }
                },
                label = { Text("Post ID") },
                singleLine = true,
                enabled = !isLoading,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Button(
                onClick = viewModel::fetchPost,
                enabled = !isLoading && viewModel.postId.isNotBlank()
            ) {
                Text("GET")
            }
        }

        OutlinedTextField(
            value = viewModel.editTitle,
            onValueChange = { viewModel.editTitle = it },
            label = { Text("Title") },
            enabled = !isLoading && hasPost,
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = viewModel.editBody,
            onValueChange = { viewModel.editBody = it },
            label = { Text("Body") },
            enabled = !isLoading && hasPost,
            modifier = Modifier.fillMaxWidth()
        )

        Button(
            onClick = viewModel::updatePost,
            enabled = !isLoading && hasPost
        ) {
            Text("PUT")
        }

        Text(postStateMessage(uiState))
    }
}

@Composable
fun SecretScreen(viewModel: SecretViewModel) {
    val uiState by viewModel.uiState.collectAsState()
    val isBusy = uiState is SecretUiState.Saving

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("Guardar secreto", style = MaterialTheme.typography.titleMedium)

        OutlinedTextField(
            value = viewModel.key,
            onValueChange = { viewModel.key = it },
            label = { Text("Llave") },
            enabled = !isBusy,
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = viewModel.value,
            onValueChange = { viewModel.value = it },
            label = { Text("Valor") },
            enabled = !isBusy,
            modifier = Modifier.fillMaxWidth()
        )

        StorageSelector(
            selected = viewModel.selectedStorage,
            onSelected = { viewModel.selectedStorage = it },
            enabled = !isBusy
        )

        Button(onClick = viewModel::saveSecret, enabled = !isBusy) {
            Text("GUARDAR")
        }

        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

        Text("Recuperar secreto", style = MaterialTheme.typography.titleMedium)

        OutlinedTextField(
            value = viewModel.retrieveKey,
            onValueChange = { viewModel.retrieveKey = it },
            label = { Text("Llave") },
            enabled = !isBusy,
            modifier = Modifier.fillMaxWidth()
        )

        StorageSelector(
            selected = viewModel.retrieveStorage,
            onSelected = { viewModel.retrieveStorage = it },
            enabled = !isBusy
        )

        Button(onClick = viewModel::retrieveSecret, enabled = !isBusy) {
            Text("RECUPERAR")
        }

        Text(secretStateMessage(uiState))
    }
}

@Composable
private fun StorageSelector(
    selected: StorageType,
    onSelected: (StorageType) -> Unit,
    enabled: Boolean
) {
    Column {
        StorageOption(
            label = "SharedPreferences",
            selected = selected == StorageType.SHARED_PREFERENCES,
            enabled = enabled,
            onClick = { onSelected(StorageType.SHARED_PREFERENCES) }
        )
        StorageOption(
            label = "DataStore",
            selected = selected == StorageType.DATA_STORE,
            enabled = enabled,
            onClick = { onSelected(StorageType.DATA_STORE) }
        )
        StorageOption(
            label = "EncryptedSharedPreferences",
            selected = selected == StorageType.ENCRYPTED_SHARED_PREFERENCES,
            enabled = enabled,
            onClick = { onSelected(StorageType.ENCRYPTED_SHARED_PREFERENCES) }
        )
    }
}

@Composable
private fun StorageOption(
    label: String,
    selected: Boolean,
    enabled: Boolean,
    onClick: () -> Unit
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        RadioButton(selected = selected, onClick = onClick, enabled = enabled)
        Spacer(modifier = Modifier.width(4.dp))
        Text(label)
    }
}

private fun postStateMessage(state: PostUiState): String = when (state) {
    PostUiState.Idle -> "Estado: listo"
    PostUiState.Loading -> "Estado: cargando..."
    is PostUiState.Success -> "Estado: post cargado"
    PostUiState.UpdateSuccess -> "Estado: actualizado (200 OK)"
    is PostUiState.Error -> "Error: ${state.message}"
}

private fun secretStateMessage(state: SecretUiState): String = when (state) {
    SecretUiState.Idle -> "Estado: listo"
    SecretUiState.Saving -> "Estado: procesando..."
    SecretUiState.Saved -> "Estado: guardado"
    is SecretUiState.Retrieved -> "Resultado: ${state.value}"
    SecretUiState.NotFound -> "Resultado: no encontrado"
    is SecretUiState.Error -> "Error: ${state.message}"
}
