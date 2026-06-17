package com.example.comunicacion_inter_app

import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.net.Uri
import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.graphics.asImageBitmap

class MainActivity : ComponentActivity() {
    private val uiState = mutableStateOf(AppUiState())

    private val takePictureLauncher = registerForActivityResult(ActivityResultContracts.TakePicturePreview()) { bitmap ->
        uiState.value = if (bitmap != null) {
            uiState.value.copy(
                incomingImage = bitmap.asImageBitmap(),
                incomingText = null,
                statusMessage = "Foto capturada desde la cámara nativa",
            )
        } else {
            uiState.value.copy(
                statusMessage = "Captura de foto cancelada",
            )
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        handleIncomingIntent(intent)

        setContent {
            App(
                uiState = uiState.value,
                onDialNumberChange = { dialNumber ->
                    uiState.value = uiState.value.copy(dialNumber = dialNumber)
                },
                onDialClick = {
                    launchDialer(uiState.value.dialNumber)
                },
                onTakePhotoClick = {
                    takePictureLauncher.launch(null)
                },
            )
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        handleIncomingIntent(intent)
    }

    private fun launchDialer(phoneNumber: String) {
        val sanitizedNumber = phoneNumber.trim()
        if (sanitizedNumber.isBlank()) return

        val dialIntent = Intent(Intent.ACTION_DIAL).apply {
            data = Uri.fromParts("tel", sanitizedNumber, null)
        }

        try {
            startActivity(dialIntent)
            uiState.value = uiState.value.copy(
                statusMessage = "Marcador abierto con el número $sanitizedNumber",
            )
        } catch (_: ActivityNotFoundException) {
            uiState.value = uiState.value.copy(
                statusMessage = "No se encontró una app de teléfono compatible",
            )
        }
    }

    private fun handleIncomingIntent(intent: Intent?) {
        if (intent?.action != Intent.ACTION_SEND) return

        val sharedText = intent.getStringExtra(Intent.EXTRA_TEXT)?.takeIf { it.isNotBlank() }
        if (sharedText != null) {
            uiState.value = uiState.value.copy(
                incomingText = sharedText,
                incomingImage = null,
                statusMessage = "Texto compartido recibido desde otra app",
            )
            return
        }

        val sharedImageUri = intent.extractSharedImageUri()
        if (sharedImageUri != null) {
            val bitmap = loadImageFromUri(sharedImageUri)
            uiState.value = if (bitmap != null) {
                uiState.value.copy(
                    incomingText = null,
                    incomingImage = bitmap,
                    statusMessage = "Imagen compartida recibida desde otra app",
                )
            } else {
                uiState.value.copy(
                    incomingText = null,
                    incomingImage = null,
                    statusMessage = "No se pudo leer la imagen compartida",
                )
            }
        }
    }

    private fun Intent.extractSharedImageUri(): Uri? {
        val fromExtra = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            getParcelableExtra(Intent.EXTRA_STREAM, Uri::class.java)
        } else {
            @Suppress("DEPRECATION")
            getParcelableExtra(Intent.EXTRA_STREAM)
        }

        if (fromExtra != null) return fromExtra

        val clipData = clipData
        if (clipData != null && clipData.itemCount > 0) {
            return clipData.getItemAt(0).uri
        }

        return null
    }

    private fun loadImageFromUri(uri: Uri) = runCatching {
        contentResolver.openInputStream(uri)?.use { inputStream ->
            BitmapFactory.decodeStream(inputStream)?.asImageBitmap()
        }
    }.getOrNull()
}

@Preview
@Composable
fun AppAndroidPreview() {
    App()
}