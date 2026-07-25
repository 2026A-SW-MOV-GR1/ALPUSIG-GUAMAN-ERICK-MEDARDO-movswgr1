package com.example.gestionpaqueteria.ui

import android.location.Geocoder
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.gestionpaqueteria.map.OsmMapView
import com.example.gestionpaqueteria.ui.components.AmazonMapPanel
import com.example.gestionpaqueteria.ui.components.AmazonStepBar
import com.example.gestionpaqueteria.ui.components.AmazonTopBar
import org.osmdroid.util.GeoPoint
import java.util.*

@Composable
fun MapScreen(navController: NavController, initialAddress: String? = null) {
    val context = LocalContext.current
    var selectedPoint by remember { mutableStateOf<GeoPoint?>(null) }
    var initialPoint by remember { mutableStateOf<GeoPoint?>(null) }
    
    LaunchedEffect(initialAddress) {
        if (!initialAddress.isNullOrBlank()) {
            try {
                val geocoder = Geocoder(context, Locale.getDefault())
                val addresses = geocoder.getFromLocationName("$initialAddress, Ecuador", 1)
                if (!addresses.isNullOrEmpty()) {
                    val location = addresses[0]
                    val point = GeoPoint(location.latitude, location.longitude)
                    initialPoint = point
                    selectedPoint = point
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
    
    Column(modifier = Modifier.fillMaxSize()) {
        AmazonTopBar(
            title = "Fijar ubicación",
            subtitle = "Toca el mapa para colocar el marcador"
        )
        AmazonStepBar(activeStep = 0)
        
        Box(modifier = Modifier.weight(1f)) {
            OsmMapView(
                modifier = Modifier.fillMaxSize(),
                initialPoint = initialPoint,
                onLocationSelected = { selectedPoint = it }
            )
        }
        
        AmazonMapPanel(
            lat = selectedPoint?.latitude,
            lng = selectedPoint?.longitude,
            onConfirm = {
                navController.previousBackStackEntry?.savedStateHandle?.set("lat", selectedPoint?.latitude)
                navController.previousBackStackEntry?.savedStateHandle?.set("lng", selectedPoint?.longitude)
                navController.popBackStack()
            },
            onCancel = { navController.popBackStack() }
        )
    }
}
