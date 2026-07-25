# Guía de Conexión Técnica para App 2 (Distribución)

Este documento contiene las especificaciones finales para asegurar la integración perfecta entre la **App de Admisión** (App 1) y la **App de Distribución** (App 2).

---

## 1. Dependencia Requerida (build.gradle)
La App 2 **debe** incluir la librería de serialización en su archivo `build.gradle` (módulo app) para poder procesar el JSON recibido:

```kotlin
dependencies {
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.3")
}

plugins {
    id("org.jetbrains.kotlin.plugin.serialization") version "1.9.0" // O la versión de tu Kotlin
}
```

---

## 2. Configuración del Manifest (AndroidManifest.xml)
Asegúrate de que la Activity que recibirá los datos tenga exactamente este bloque. **Importante**: `android:exported="true"` es obligatorio en Android 12+.

```xml
<activity 
    android:name=".TuActivityReceptora" 
    android:exported="true">
    <intent-filter>
        <action android:name="com.equipoamazon.intent.action.ADMISION_TO_DISTRIBUCION" />
        <category android:name="android.intent.category.DEFAULT" />
    </intent-filter>
</activity>
```

---

## 3. Contrato de Datos (Intent)
*   **Action**: `com.equipoamazon.intent.action.ADMISION_TO_DISTRIBUCION`
*   **Extra Key**: `paquete_json`
*   **Formato**: String JSON.

### Modelo de Datos (Paquete.kt)
Copia esta clase exactamente igual para que la deserialización sea automática:

```kotlin
import kotlinx.serialization.Serializable

@Serializable
data class Paquete(
    val idPaquete: String,      // Generado en App 1: "AMZ-{timestamp}"
    val remitente: String,
    val destinatario: String,
    val direccionOrigen: String,
    val latOrigen: Double,      // Coordenada manual fijada en mapa
    val lngOrigen: Double,      // Coordenada manual fijada en mapa
    val fechaAdmision: String,  // Formato ISO-8601
    val estado: String          // Valor inicial: "ADMITIDO"
)
```

---

## 4. Código de Recepción (Activity)

```kotlin
import kotlinx.serialization.json.Json

// ... dentro de onCreate ...
val paqueteJson = intent.getStringExtra("paquete_json")

if (paqueteJson != null) {
    try {
        val paquete = Json.decodeFromString<Paquete>(paqueteJson)
        // Lógica: Mostrar en UI, guardar en BD, etc.
        Toast.makeText(this, "Paquete ${paquete.idPaquete} recibido", Toast.LENGTH_SHORT).show()
    } catch (e: Exception) {
        Log.e("INTEGRACION", "Error al procesar JSON: ${e.message}")
    }
}
```

---

## 5. Puntos Críticos de Integración (¡IMPORTANTE!)

1.  **Inmutabilidad del ID**: El campo `idPaquete` es el ancla de todo el flujo. **No lo alteres ni lo regeneres** en la App 2.
2.  **Formato de Fecha**: La fecha viene en formato ISO-8601 (ej: `2024-07-23T20:30:00Z`). Si necesitas mostrarla diferente, usa un formateador, pero mantén el String original si vas a reenviar el objeto a una App 3.
3.  **Precisión de Coordenadas**: Las coordenadas son `Double`. Asegúrate de no redondearlas al procesarlas para no perder precisión en el punto de recogida.
4.  **Actualización de Estado**: Al recibir el paquete, se recomienda que la App 2 actualice internamente el campo `estado` a algo como `"DISTRIBUCION"` o `"TRANSITO"`.

---

## 6. ¿Cómo probar sin la App 1? (Prueba vía ADB)
Christian puede probar su app incluso si tú no le has enviado el APK, usando este comando en la terminal (reemplazando los datos por unos de prueba):

```bash
adb shell am start \
  -a com.equipoamazon.intent.action.ADMISION_TO_DISTRIBUCION \
  -c android.intent.category.DEFAULT \
  --es paquete_json '{"idPaquete":"AMZ-123","remitente":"Amazon","destinatario":"Christian","direccionOrigen":"Quito","latOrigen":-0.18,"lngOrigen":-78.46,"fechaAdmision":"2024-07-23T20:00:00Z","estado":"ADMITIDO"}'
```
Si este comando abre su app y muestra los datos, la integración está garantizada.


