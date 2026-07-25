# Plan de Corrección Definitivo - OSM 403 (Escenario Multi-Factor)

Si el error 403 persiste, significa que la identificación de la app sigue siendo rechazada o que hay un problema con el proveedor de mapas específico (Mapnik). Aplicaremos una estrategia de "fuerza bruta" técnica para cumplir con todas las políticas de OSM.

## Investigacion de Escenarios

1.  **Identificación Inconsistente**: El User-Agent puede estar siendo sobrescrito durante la carga de preferencias.
2.  **Falta de Referer**: Muchos servidores ahora exigen la cabecera `Referer`.
3.  **Bloqueo de Proveedor**: Mapnik (el servidor por defecto) es el más estricto. Probaremos un proveedor alternativo.
4.  **Caché de Bloqueo**: OSMDroid puede estar intentando re-descargar tiles bloqueados que tiene en su base de datos interna.

## Cambios Propuestos

### 1. Re-Estructuración de Configuración (androidApp)

#### [MODIFY] [GestionPaqueteriaApp.kt](file:///D:/Gestion_Paquetes_Ex1-Pr1/androidApp/src/main/kotlin/com/example/gestionpaqueteria/GestionPaqueteriaApp.kt)
- Eliminaremos el uso de `load()` de preferencias para evitar que valores antiguos interfieran.
- Configuraremos todo manualmente de forma estática.
- Añadiremos la cabecera `Referer` de forma global.
- Usaremos un User-Agent que imite a un navegador estándar (como prueba definitiva de identificación).

### 2. Cambio de Proveedor de Mapas (shared)

#### [MODIFY] [OsmMapView.kt](file:///D:/Gestion_Paquetes_Ex1-Pr1/shared/src/androidMain/kotlin/com/example/gestionpaqueteria/map/OsmMapView.kt)
- Cambiaremos temporalmente el `TileSource` a uno menos restrictivo (como `WIKIMEDIA` o `OpenTopo`) para verificar si es un bloqueo específico de los servidores de Mapnik.
- Forzaremos la configuración de la instancia justo antes de crear el `MapView`.

### 3. Limpieza de Directorios

#### [MODIFY] [GestionPaqueteriaApp.kt](file:///D:/Gestion_Paquetes_Ex1-Pr1/androidApp/src/main/kotlin/com/example/gestionpaqueteria/GestionPaqueteriaApp.kt)
- Añadiremos lógica para **borrar físicamente** el directorio de caché de tiles en cada inicio durante esta fase de prueba, para asegurar que no se carguen imágenes de "Error 403" guardadas.

---

## Verificación

1. **Compilar y Ejecutar**.
2. **Observar Logs**: He añadido logs específicos para ver la URL exacta que falla.
