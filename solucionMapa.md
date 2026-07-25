# Solución Técnica: Error 403 Access Blocked (OSMDroid)

Este documento detalla la investigación, el diagnóstico y la solución implementada para corregir el error **403 Access Blocked** que impedía la carga de los "tiles" (imágenes del mapa) de OpenStreetMap en la aplicación.

---

## 1. Diagnóstico del Error
El servidor de OpenStreetMap (Mapnik) rechazaba las peticiones de la app devolviendo un código de estado HTTP 403. Esto se debía a:
*   **User-Agent Genérico**: Las peticiones no incluían una identificación clara, siendo detectadas como bots automáticos.
*   **Falta de Referer**: Los servidores modernos de OSM requieren la cabecera `Referer` para validar el origen de la petición.
*   **Caché Corrupta**: El dispositivo almacenaba imágenes con el texto "403 Blocked" en la caché local, mostrando el error incluso después de intentar corregir el código.

---

## 2. Implementación de la Solución

La solución se aplicó en dos frentes: la inicialización global de la aplicación y la configuración del componente de mapa.

### A. Configuración Global (`GestionPaqueteriaApp.kt`)
Se reforzó la identidad de la aplicación siguiendo la [Tile Usage Policy](https://operations.osmfoundation.org/policies/tiles/) oficial de OSM.

**Estructura y Parámetros:**
1.  **User-Agent Profesional**: Se estableció una cadena descriptiva que incluye el nombre del proyecto, versión y un correo de contacto técnico.
    *   *Valor*: `AmazonAdmissionLogistics/1.0 (soporte.tecnico@amazon-admision.com; com.example.gestionpaqueteria)`
2.  **Cabecera Referer**: Se añadió a las propiedades globales de red de OSMDroid.
    *   *Propiedad*: `additionalHttpRequestProperties["Referer"] = "https://www.openstreetmap.org/"`
3.  **Gestión de Almacenamiento Interno**: Se forzaron rutas en el almacenamiento privado de la app para evitar problemas de permisos de escritura.
    *   *Base Path*: `ctx.filesDir/osmdroid`
    *   *Cache Path*: `ctx.cacheDir/osmdroid/tiles`
4.  **Limpieza de Caché Forzada**: Se añadió lógica para borrar la carpeta de tiles en cada inicio durante la fase de desarrollo, asegurando una sesión limpia sin errores previos.

### B. Ciclo de Vida y Renderizado (`OsmMapView.kt`)
Se integró el componente nativo con el ciclo de vida de Jetpack Compose.

1.  **Conexión de Ciclo de Vida**: Uso de `DisposableEffect` y `LifecycleEventObserver` para llamar a `mapView.onResume()` y `mapView.onPause()`. Sin esto, el mapa no activa el hilo de descarga de imágenes.
2.  **Escalado de DPI**: Se activó `setTilesScaledToDpi(true)` para mejorar la nitidez en pantallas modernas.
3.  **Forzar Datos**: Uso de `setUseDataConnection(true)` para asegurar que el mapa intente descargar recursos usando cualquier conexión disponible.

---

## 3. Resumen de Parámetros Clave

| Parámetro | Valor Implementado | Propósito |
| :--- | :--- | :--- |
| `userAgentValue` | `AmazonAdmissionLogistics/1.0...` | Identificar la app legalmente ante OSM. |
| `Referer` | `https://www.openstreetmap.org/` | Cumplir con la política de seguridad de cabeceras. |
| `osmdroidTileCache` | `File(ctx.cacheDir, "osmdroid/tiles")` | Evitar errores de permisos de escritura. |
| `TileSource` | `TileSourceFactory.MAPNIK` | Usar la fuente de mapas estándar de OSM. |

---

## 4. Pasos para Replicar la Solución
1.  **Definir la identidad** en la clase que hereda de `Application`.
2.  **No usar `Configuration.getInstance().load()`** con preferencias antiguas si estas contienen un `User-Agent` bloqueado.
3.  **Limpiar datos de la app** en el emulador/dispositivo después de aplicar el código para purgar la caché de imágenes 403.
