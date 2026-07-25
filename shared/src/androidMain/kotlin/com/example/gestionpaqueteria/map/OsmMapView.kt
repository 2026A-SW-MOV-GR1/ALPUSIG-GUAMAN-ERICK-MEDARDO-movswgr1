package com.example.gestionpaqueteria.map

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import org.osmdroid.events.MapEventsReceiver
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.MapEventsOverlay
import org.osmdroid.views.overlay.Marker

@Composable
fun OsmMapView(
    modifier: Modifier = Modifier,
    initialPoint: GeoPoint? = null,
    onLocationSelected: (GeoPoint) -> Unit
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    
    // Crear el MapView y configurarlo inicialmente
    val mapView = remember {
        MapView(context).apply {
            // Probamos con MAPNIK pero con las nuevas cabeceras globales configuradas en la App
            setTileSource(TileSourceFactory.MAPNIK)
            setMultiTouchControls(true)
            setTilesScaledToDpi(true)
            setUseDataConnection(true)
            isClickable = true
            isFocusable = true
            
            controller.setZoom(14.0)
            val startPoint = initialPoint ?: GeoPoint(-0.1807, -78.4678)
            controller.setCenter(startPoint)
        }
    }

    // Manejo del ciclo de vida
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_RESUME -> mapView.onResume()
                Lifecycle.Event.ON_PAUSE -> mapView.onPause()
                else -> {}
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
            mapView.onDetach()
        }
    }

    AndroidView(
        modifier = modifier,
        factory = {
            mapView.apply {
                val marker = Marker(this)
                marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
                
                try {
                    val markerIcon = context.getDrawable(org.osmdroid.library.R.drawable.marker_default)
                    if (markerIcon != null) {
                        marker.icon = markerIcon
                    }
                } catch (e: Exception) {
                    // Fallback
                }
                
                if (initialPoint != null) {
                    marker.position = initialPoint
                    overlays.add(marker)
                }

                val mapEventsReceiver = object : MapEventsReceiver {
                    override fun singleTapConfirmedHelper(p: GeoPoint): Boolean {
                        marker.position = p
                        if (!overlays.contains(marker)) {
                            overlays.add(marker)
                        }
                        invalidate()
                        onLocationSelected(p)
                        return true
                    }

                    override fun longPressHelper(p: GeoPoint): Boolean = false
                }
                
                overlays.add(MapEventsOverlay(mapEventsReceiver))
            }
        },
        update = { view ->
            initialPoint?.let {
                view.controller.animateTo(it)
                view.controller.setZoom(16.0)
            }
        }
    )
}
