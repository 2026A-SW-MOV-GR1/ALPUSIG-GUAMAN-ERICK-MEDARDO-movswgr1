package com.example.comunicacion_inter_app

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

data class AppUiState(
    val dialNumber: String = "",
    val statusMessage: String = "Esperando datos externos...",
    val incomingText: String? = null,
    val incomingImage: ImageBitmap? = null,
)

@Composable
@Preview
fun App(
    uiState: AppUiState = AppUiState(),
    onDialNumberChange: (String) -> Unit = {},
    onDialClick: () -> Unit = {},
    onTakePhotoClick: () -> Unit = {},
) {
    MaterialTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.background)
                    .padding(16.dp)
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                Text(
                    text = "MÓDULO: INTENTS SALIENTES",
                    style = MaterialTheme.typography.headlineSmall,
                )

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                    ) {
                        OutlinedTextField(
                            modifier = Modifier.fillMaxWidth(),
                            value = uiState.dialNumber,
                            onValueChange = onDialNumberChange,
                            label = { Text("Teléfono") },
                            singleLine = true,
                        )

                        Button(
                            onClick = onDialClick,
                            enabled = uiState.dialNumber.isNotBlank(),
                        ) {
                            Text("Iniciar dial")
                        }
                    }
                }

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .aspectRatio(1f)
                                .background(MaterialTheme.colorScheme.surface),
                            contentAlignment = Alignment.Center,
                        ) {
                            if (uiState.incomingImage != null) {
                                Image(
                                    bitmap = uiState.incomingImage,
                                    contentDescription = "Miniatura de cámara o imagen compartida",
                                    modifier = Modifier.fillMaxSize(),
                                    contentScale = ContentScale.Crop,
                                )
                            } else {
                                Text(
                                    text = "[Miniatura]",
                                    textAlign = TextAlign.Center,
                                )
                            }
                        }

                        Button(onClick = onTakePhotoClick) {
                            Text("Tomar foto")
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "MÓDULO: INTENTS ENTRANTES",
                    style = MaterialTheme.typography.headlineSmall,
                )

                Text(
                    text = "Estado: ${uiState.statusMessage}",
                    style = MaterialTheme.typography.bodyLarge,
                )

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                    ) {
                        Text(
                            text = uiState.incomingText ?: "Caja de texto / label para renderizar texto recibido",
                            style = MaterialTheme.typography.bodyLarge,
                        )
                    }
                }

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(240.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        contentAlignment = Alignment.Center,
                    ) {
                        if (uiState.incomingImage != null) {
                            Image(
                                bitmap = uiState.incomingImage,
                                contentDescription = "Imagen recibida desde otra app",
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Fit,
                            )
                        } else {
                            Text(
                                text = "Contenedor dinámico para imagen recibida",
                                textAlign = TextAlign.Center,
                            )
                        }
                    }
                }
            }
        }
    }
}