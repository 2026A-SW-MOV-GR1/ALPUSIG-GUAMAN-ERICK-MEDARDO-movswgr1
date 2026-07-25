package com.example.gestionpaqueteria

import android.app.Application
import org.osmdroid.config.Configuration
import java.io.File

class GestionPaqueteriaApp : Application() {
    override fun onCreate() {
        super.onCreate()
        
        val ctx = applicationContext
        val instance = Configuration.getInstance()

        // 1. Identidad de "Navegador Real" para saltar bloqueos de bots
        // Usamos un UA de Chrome estándar y un Referer
        val browserUserAgent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36"
        instance.userAgentValue = browserUserAgent
        
        // Algunos servidores requieren el referer para validar la petición
        instance.additionalHttpRequestProperties["Referer"] = "https://www.openstreetmap.org/"

        // 2. Configurar rutas de almacenamiento interno
        val osmBase = File(ctx.filesDir, "osmdroid")
        if (!osmBase.exists()) osmBase.mkdirs()
        instance.osmdroidBasePath = osmBase
        
        val osmCache = File(ctx.cacheDir, "osmdroid/tiles")
        
        // 3. LIMPIEZA FORZADA: Borrar caché vieja de errores 403 en cada inicio de prueba
        if (osmCache.exists()) {
            osmCache.deleteRecursively()
        }
        osmCache.mkdirs()
        instance.osmdroidTileCache = osmCache

        // 4. Configuración técnica adicional
        instance.isDebugMode = true
        instance.isDebugMapTileDownloader = true
        
        // NO llamamos a instance.load(ctx, prefs) para evitar que se pisen los valores 
        // con configuraciones antiguas del sistema.
    }
}
