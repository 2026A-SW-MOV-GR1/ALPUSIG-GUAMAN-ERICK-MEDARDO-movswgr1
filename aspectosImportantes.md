# Contrato de Integración — App Admisión a App Distribución

Este documento detalla los aspectos técnicos necesarios para recibir los datos del paquete enviados desde la **App de Admisión** (Eslabón 1) hacia la **App de Distribución** (Eslabón 2 - Christian Aragon).

---

## 1. Configuración del Receptor (App Distribución)

Para recibir el paquete, la App 2 debe declarar un `intent-filter` en su `AndroidManifest.xml` dentro de la Activity encargada de procesar la entrada:

```xml
<activity android:name=".TuActivityReceptora" android:exported="true">
    <intent-filter>
        <action android:name="com.equipoamazon.intent.action.ADMISION_TO_DISTRIBUCION" />
        <category android:name="android.intent.category.DEFAULT" />
    </intent-filter>
</activity>
```

---

## 2. Contrato de Datos (Intent)

La información viaja encapsulada en un Intent implícito con las siguientes especificaciones:

*   **Action**: `com.equipoamazon.intent.action.ADMISION_TO_DISTRIBUCION`
*   **Extra Key**: `paquete_json`
*   **Formato del Valor**: String (JSON serializado con `kotlinx.serialization`).

### Estructura del JSON (`Paquete`)
```json
{
  "idPaquete": "AMZ-1721130600000",
  "remitente": "Nombre del Vendedor",
  "destinatario": "Nombre del Cliente",
  "direccionOrigen": "Referencia textual del punto de recogida",
  "latOrigen": -0.180653,
  "lngOrigen": -78.467834,
  "fechaAdmision": "2026-07-16T09:30:00Z",
  "estado": "ADMITIDO"
}
```

---

## 3. Modelo de Datos Sugerido (Kotlin)

Se recomienda usar el mismo modelo para evitar errores de deserialización:

```kotlin
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

## 4. Ejemplo de Recepción en la App 2

```kotlin
// Dentro de la Activity receptora
val jsonExtra = intent.getStringExtra("paquete_json")
if (jsonExtra != null) {
    val paquete = Json.decodeFromString<Paquete>(jsonExtra)
    // Ya puedes usar el objeto paquete.idPaquete, paquete.latOrigen, etc.
}
```

---

## 5. Notas Importantes para el Compañero
*   **ID Único**: El `idPaquete` generado por la App 1 es inmutable. Debe mantenerse en todo el flujo logístico.
*   **Estado**: La App 1 siempre envía el estado como `"ADMITIDO"`. La App 2 es responsable de actualizarlo a `"EN_CAMINO"` o similar si es necesario.
*   **Coordenadas**: `latOrigen` y `lngOrigen` representan el punto físico exacto donde el operador de admisión recibió el paquete (fijado manualmente en el mapa).
*   **Visibilidad**: La App 1 ya tiene configurado el bloque `<queries>` para detectar a la App 2, por lo que la comunicación debería ser fluida siempre que la App 2 declare el `intent-filter` correctamente.
