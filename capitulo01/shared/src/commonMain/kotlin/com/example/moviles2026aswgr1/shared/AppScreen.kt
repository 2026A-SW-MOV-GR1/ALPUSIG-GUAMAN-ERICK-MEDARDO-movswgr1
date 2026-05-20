package com.example.moviles2026aswgr1.shared

import
androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private data class CrudItem(
    val id: Int,
    val title: String,
    val subtitle: String,
    val active: Boolean
)

private sealed interface Screen {
    data object List : Screen
    data class Form(val itemId: Int?) : Screen
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppScreen(useCommonResources: Boolean = false) {
    val items = remember {
        mutableStateListOf(
            CrudItem(1, "Cliente 1", "Quito", true),
            CrudItem(2, "Cliente 2", "Guayaquil", false),
            CrudItem(3, "Cliente 3", "Cuenca", true)
        )
    }
    var nextId by remember { mutableIntStateOf(4) }
    var screen by remember { mutableStateOf<Screen>(Screen.List) }
    var deleteCandidate by remember { mutableStateOf<CrudItem?>(null) }

    val showToast = rememberToast()
    val header = if (useCommonResources) commonSaludo() else appSaludo()
    val textoColor = appTextColor()
    val fondoColor = appBackgroundColor()

    MaterialTheme {
        Scaffold(
            containerColor = fondoColor,
            topBar = {
                TopAppBar(
                    title = { Text(text = header, color = textoColor) },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = fondoColor)
                )
            },
            floatingActionButton = {
                if (screen is Screen.List) {
                    FloatingActionButton(onClick = { screen = Screen.Form(itemId = null) }) {
                        Text(text = "+")
                    }
                }
            }
        ) { padding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
            ) {
                when (val current = screen) {
                    Screen.List -> {
                        CrudList(
                            items = items,
                            onEdit = { id -> screen = Screen.Form(itemId = id) },
                            onDelete = { item -> deleteCandidate = item }
                        )
                    }
                    is Screen.Form -> {
                        val editingItem = current.itemId?.let { id ->
                            items.firstOrNull { it.id == id }
                        }
                        CrudForm(
                            item = editingItem,
                            onSave = { updated ->
                                if (editingItem == null) {
                                    items.add(updated.copy(id = nextId))
                                    nextId += 1
                                } else {
                                    val index = items.indexOfFirst { it.id == editingItem.id }
                                    if (index >= 0) {
                                        items[index] = updated.copy(id = editingItem.id)
                                    }
                                }
                                showToast("Registro guardado")
                                screen = Screen.List
                            },
                            onCancel = { screen = Screen.List }
                        )
                    }
                }

                if (deleteCandidate != null) {
                    AlertDialog(
                        onDismissRequest = { deleteCandidate = null },
                        title = { Text("Confirmar eliminacion") },
                        text = { Text("Esta seguro de eliminar este registro?") },
                        confirmButton = {
                            TextButton(
                                onClick = {
                                    val target = deleteCandidate
                                    if (target != null) {
                                        items.remove(target)
                                        showToast("Registro eliminado")
                                    }
                                    deleteCandidate = null
                                }
                            ) {
                                Text("Eliminar", color = Color(0xFFD32F2F))
                            }
                        },
                        dismissButton = {
                            TextButton(onClick = { deleteCandidate = null }) {
                                Text("Cancelar")
                            }
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun CrudList(
    items: List<CrudItem>,
    onEdit: (Int) -> Unit,
    onDelete: (CrudItem) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(items, key = { it.id }) { item ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .background(Color(0xFFE3F2FD), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = item.title.take(1).uppercase(),
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF1565C0)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(text = item.title, fontWeight = FontWeight.SemiBold)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(text = item.subtitle, color = Color(0xFF5F6368))
                    }
                    TextButton(onClick = { onEdit(item.id) }) {
                        Text("Editar")
                    }
                    Spacer(modifier = Modifier.width(4.dp))
                    Button(
                        onClick = { onDelete(item) },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD32F2F))
                    ) {
                        Text("Eliminar", color = Color.White, fontSize = 12.sp)
                    }
                }
            }
        }
    }
}

@Composable
private fun CrudForm(
    item: CrudItem?,
    onSave: (CrudItem) -> Unit,
    onCancel: () -> Unit
) {
    var title by remember { mutableStateOf(item?.title ?: "") }
    var subtitle by remember { mutableStateOf(item?.subtitle ?: "") }
    var active by remember { mutableStateOf(item?.active ?: true) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = if (item == null) "Nuevo registro" else "Editar registro",
            style = MaterialTheme.typography.titleLarge
        )
        OutlinedTextField(
            value = title,
            onValueChange = { title = it },
            label = { Text("Titulo") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = subtitle,
            onValueChange = { subtitle = it },
            label = { Text("Subtitulo") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Activo")
            Spacer(modifier = Modifier.width(12.dp))
            Switch(checked = active, onCheckedChange = { active = it })
        }
        Spacer(modifier = Modifier.weight(1f))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedButton(
                onClick = onCancel,
                modifier = Modifier.weight(1f)
            ) {
                Text("Cancelar")
            }
            Button(
                onClick = {
                    if (title.isNotBlank()) {
                        onSave(CrudItem(item?.id ?: 0, title, subtitle, active))
                    }
                },
                modifier = Modifier.weight(1f)
            ) {
                Text("Guardar")
            }
        }
    }
}
