package com.example.gestionpaqueteria.ui

import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.gestionpaqueteria.model.Paquete
import com.example.gestionpaqueteria.ui.components.*
import com.example.gestionpaqueteria.ui.theme.SurfaceLight
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@Composable
fun FormScreen(navController: NavController) {
    val context = LocalContext.current
    var remitente by rememberSaveable { mutableStateOf("") }
    var destinatario by rememberSaveable { mutableStateOf("") }
    var direccionOrigen by rememberSaveable { mutableStateOf("") }
    
    val savedStateHandle = navController.currentBackStackEntry?.savedStateHandle
    val lat = savedStateHandle?.getStateFlow<Double?>("lat", null)?.collectAsState()
    val lng = savedStateHandle?.getStateFlow<Double?>("lng", null)?.collectAsState()
    
    var showSummary by remember { mutableStateOf(false) }
    var paqueteCreated by remember { mutableStateOf<Paquete?>(null) }

    Column(modifier = Modifier.fillMaxSize().background(SurfaceLight)) {
        AmazonTopBar(title = "Admisión", letter = "A")
        AmazonStepBar(activeStep = 0)
        
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(12.dp)
        ) {
            AmazonSectionTitle("Datos del paquete")
            AmazonTextField(
                label = "Remitente",
                value = remitente,
                onValueChange = { remitente = it },
                placeholder = "Nombre del remitente"
            )
            Spacer(modifier = Modifier.height(8.dp))
            AmazonTextField(
                label = "Destinatario",
                value = destinatario,
                onValueChange = { destinatario = it },
                placeholder = "Nombre del destinatario"
            )
            Spacer(modifier = Modifier.height(8.dp))
            AmazonTextField(
                label = "Dirección origen",
                value = direccionOrigen,
                onValueChange = { direccionOrigen = it },
                placeholder = "Calle, ciudad..."
            )
            
            AmazonDivider()
            
            AmazonSectionTitle("Ubicación de recolección")
            AmazonCoordinateCard(lat = lat?.value, lng = lng?.value)
            Spacer(modifier = Modifier.height(10.dp))
            
            AmazonMapButton(onClick = { 
                val route = if (direccionOrigen.isNotBlank()) "map?address=$direccionOrigen" else "map"
                navController.navigate(route) 
            })
            
            Spacer(modifier = Modifier.height(10.dp))
            
            val tempId = "AMZ-${System.currentTimeMillis()}"
            AmazonIdChip(id = tempId)
            
            Spacer(modifier = Modifier.height(16.dp))
            
            val isReady = remitente.isNotBlank() && destinatario.isNotBlank() && direccionOrigen.isNotBlank() && lat?.value != null
            
            AmazonPrimaryButton(
                onClick = {
                    paqueteCreated = Paquete.createNew(remitente, destinatario, direccionOrigen, lat!!.value!!, lng!!.value!!)
                    showSummary = true
                },
                text = "Registrar admisión",
                enabled = isReady
            )
        }
        
        if (showSummary && paqueteCreated != null) {
            AlertDialog(
                onDismissRequest = { showSummary = false },
                confirmButton = {
                    Button(onClick = {
                        try {
                            val paqueteJson = Json.encodeToString(paqueteCreated!!)
                            val intent = Intent("com.equipoamazon.intent.action.ADMISION_TO_DISTRIBUCION").apply {
                                addCategory(Intent.CATEGORY_DEFAULT)
                                putExtra("paquete_json", paqueteJson)
                            }
                            if (intent.resolveActivity(context.packageManager) != null) {
                                context.startActivity(intent)
                            } else {
                                Toast.makeText(context, "La App de Distribución no está instalada", Toast.LENGTH_LONG).show()
                            }
                        } catch (e: Exception) {
                            Toast.makeText(context, "Error al enviar: ${e.message}", Toast.LENGTH_SHORT).show()
                        }
                    }) {
                        Text("Enviar a Distribución")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showSummary = false }) {
                        Text("Cerrar")
                    }
                },
                title = { Text("Resumen de Admisión") },
                text = {
                    Column {
                        Text("ID: ${paqueteCreated!!.idPaquete}")
                        Text("Remitente: ${paqueteCreated!!.remitente}")
                        Text("Destinatario: ${paqueteCreated!!.destinatario}")
                        Text("Origen: ${paqueteCreated!!.direccionOrigen}")
                        Text("Coordenadas: ${paqueteCreated!!.latOrigen}, ${paqueteCreated!!.lngOrigen}")
                        Text("Fecha: ${paqueteCreated!!.fechaAdmision}")
                        Text("Estado: ${paqueteCreated!!.estado}")
                    }
                }
            )
        }
    }
}
