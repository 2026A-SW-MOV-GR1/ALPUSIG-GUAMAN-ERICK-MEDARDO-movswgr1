# Guía de Conexión para App 2 (Distribución)

Este documento contiene toda la información técnica que la **App de Distribución** necesita para recibir correctamente los datos enviados por la **App de Admisión**.

---

## 1. Configuración del Manifest (AndroidManifest.xml)

La App 2 debe declarar que es capaz de "escuchar" el Intent enviado por la App 1. Para ello, añade el siguiente `intent-filter` a la Activity que recibirá los datos:

```xml
<activity 
    android:name=".TuActivityReceptora" 
    android:exported="true">
    <intent-filter>
        <!-- Acción exacta acordada -->
        <action android:name="com.equipoamazon.intent.action.ADMISION_TO_DISTRIBUCION" />
        <category android:name="android.intent.category.DEFAULT" />
    </intent-filter>
</activity>
```

---

## 2. Contrato del Intent

*   **Acción**: `com.equipoamazon.intent.action.ADMISION_TO_DISTRIBUCION`
*   **Key del Extra**: `paquete_json`
*   **Tipo**: `String` (JSON serializado)

---

## 3. Modelo de Datos (Kotlin)

Para que la deserialización funcione sin errores, se debe usar la siguiente estructura de datos (usando la librería `kotlinx.serialization`):

```kotlin
import kotlinx.serialization.Serializable

@Serializable
data class Paquete(
    val idPaquete: String,
    val remitente: String,
    val destinatario: String,
    val direccionOrigen: String,
    val latOrigen: Double,
    val lngOrigen: Double,
    val fechaAdmision: String,
    val estado: String
)
```

---

## 4. Código para Recibir y Leer los Datos

Dentro del `onCreate` (o `onNewIntent`) de la Activity receptora en la App 2:

```kotlin
import kotlinx.serialization.json.Json

// ...

val paqueteJson = intent.getStringExtra("paquete_json")

if (paqueteJson != null) {
    try {
        // Deserializar el JSON al objeto Paquete
        val paquete = Json.decodeFromString<Paquete>(paqueteJson)
        
        // Ejemplo de uso:
        println("Recibido paquete ID: ${paquete.idPaquete}")
        println("Destinatario: ${paquete.destinatario}")
        println("Ubicación Origen: ${paquete.latOrigen}, ${paquete.lngOrigen}")
        
    } catch (e: Exception) {
        // Manejar error de formato si es necesario
    }
} else {
    // El intent no contenía datos del paquete
}
```

---

## 5. Puntos Clave para la Integración

1.  **ID de Paquete**: El `idPaquete` (ej: `AMZ-1721130600000`) es generado por la App 1 y **no debe ser modificado**. Es el identificador único para todo el proceso logístico.
2.  **Estado Inicial**: La App 1 siempre envía el estado como `"ADMITIDO"`. La App 2 debería cambiarlo a `"DISTRIBUCION"` o `"EN_CAMINO"` al procesarlo.
3.  **Coordenadas**: `latOrigen` y `lngOrigen` son tipo `Double`. Son las coordenadas fijadas manualmente en el mapa por el operador de admisión.
4.  **Desacoplamiento**: El Intent es **implícito**. Esto significa que la App 1 no conoce el nombre del paquete (package name) de la App 2, solo busca a alguien que responda a la acción de "ADMISION_TO_DISTRIBUCION".
