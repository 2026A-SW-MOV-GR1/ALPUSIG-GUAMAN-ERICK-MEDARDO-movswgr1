package com.example.dualpersistence_e1b.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.dualpersistence_e1b.model.Item
import com.example.dualpersistence_e1b.model.StorageMode
import com.example.dualpersistence_e1b.viewmodel.ItemViewModel

@Composable
fun DualPersistenceApp(viewModel: ItemViewModel) {
    val items by viewModel.items.collectAsState()
    val mode by viewModel.mode.collectAsState()

    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var editingId by remember { mutableStateOf<String?>(null) }
    var editingCreatedAt by remember { mutableStateOf<Long?>(null) }

    val canSubmit = title.isNotBlank() && description.isNotBlank()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Persistencia dual") },
                actions = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(if (mode == StorageMode.SQL) "SQLite" else "NoSQL")
                        Switch(
                            checked = mode == StorageMode.NOSQL,
                            onCheckedChange = { checked ->
                                val newMode = if (checked) StorageMode.NOSQL else StorageMode.SQL
                                viewModel.onModeChanged(newMode)
                            }
                        )
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            StorageIndicator(mode = mode)

            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Titulo") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Descripcion") },
                modifier = Modifier.fillMaxWidth()
            )

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(
                    onClick = {
                        val currentId = editingId
                        val currentCreatedAt = editingCreatedAt
                        if (currentId == null || currentCreatedAt == null) {
                            viewModel.addItem(title, description)
                        } else {
                            viewModel.updateItem(currentId, title, description, currentCreatedAt)
                        }
                        title = ""
                        description = ""
                        editingId = null
                        editingCreatedAt = null
                    },
                    enabled = canSubmit
                ) {
                    Text(if (editingId == null) "Agregar" else "Guardar cambios")
                }
                if (editingId != null) {
                    TextButton(
                        onClick = {
                            title = ""
                            description = ""
                            editingId = null
                            editingCreatedAt = null
                        }
                    ) {
                        Text("Cancelar")
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(items, key = { it.id }) { item ->
                    ItemCard(
                        item = item,
                        onEdit = {
                            editingId = item.id
                            editingCreatedAt = item.createdAt
                            title = item.title
                            description = item.description
                        },
                        onDelete = { viewModel.deleteItem(item.id) }
                    )
                }
            }
        }
    }
}

@Composable
private fun StorageIndicator(mode: StorageMode) {
    val label = if (mode == StorageMode.SQL) "Origen activo: SQLite" else "Origen activo: NoSQL"
    val color = if (mode == StorageMode.SQL) {
        MaterialTheme.colorScheme.primaryContainer
    } else {
        MaterialTheme.colorScheme.tertiaryContainer
    }
    Text(
        text = label,
        modifier = Modifier
            .background(color, MaterialTheme.shapes.small)
            .padding(horizontal = 12.dp, vertical = 6.dp),
        style = MaterialTheme.typography.labelLarge
    )
}

@Composable
private fun ItemCard(
    item: Item,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Text(text = item.title, style = MaterialTheme.typography.titleMedium)
            Text(text = item.description, style = MaterialTheme.typography.bodyMedium)
            Text(text = "Creado: ${item.createdAt}", style = MaterialTheme.typography.labelSmall)
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                TextButton(onClick = onEdit) { Text("Editar") }
                TextButton(onClick = onDelete) { Text("Eliminar") }
            }
        }
    }
}

