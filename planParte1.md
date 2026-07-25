# Prompt Agente — Parte 1: App Admisión de Paquetes Amazon
> Proyecto: Kotlin Multiplatform (KMP) + Compose Multiplatform, target Android.
> Rol en el flujo: Eslabón 1 de 3. Esta app genera y envía los datos; no recibe nada de nadie.
> Mapa: OSMDroid (OpenStreetMap) — sin API Key, sin credenciales externas.

---

## Contexto de negocio

Eres el operador de admisión de un centro de distribución de Amazon.
Tu app registra la entrada de un paquete al sistema logístico: captura los
datos del envío y fija en el mapa el punto físico exacto donde fue recogido
o recibido. Con eso el paquete queda "admitido" y listo para ser procesado
por la siguiente app del flujo (Distribución).

---

## Lo que debe existir al terminar

### 1. Estructura del proyecto

- Proyecto KMP con dos módulos: `shared` y `androidApp`.
- `shared/commonMain` contiene únicamente el modelo de datos y utilidades
  de validación/formato (sin dependencias de Android).
- `shared/androidMain` contiene el Composable del mapa (OSMDroid vive aquí,
  es una librería nativa Android).
- `androidApp` contiene la Activity principal, las pantallas de UI y la
  lógica de navegación.

### 2. Modelo de datos (`commonMain`)

Un único `data class` llamado `Paquete`, serializable con
`kotlinx.serialization`, con los siguientes campos:

- `idPaquete: String` — identificador único del envío (generar automáticamente
  con formato `AMZ-{timestamp}` o UUID al momento de crear el paquete).
- `remitente: String` — quién despacha (ej. nombre del vendedor o fulfillment
  center de Amazon).
- `destinatario: String` — nombre del cliente final que recibirá el paquete.
- `direccionOrigen: String` — descripción textual legible del punto de
  recolección (referencia, no coordenada).
- `latOrigen: Double` — latitud fijada manualmente en el mapa.
- `lngOrigen: Double` — longitud fijada manualmente en el mapa.
- `fechaAdmision: String` — fecha y hora de registro en formato ISO-8601,
  generada automáticamente al confirmar.
- `estado: String` — valor fijo `"ADMITIDO"` al salir de esta app.

Este modelo es el **contrato de datos** que viajará al Intent hacia la App 2.
No agregar campos extra sin coordinar con el equipo.

### 3. Dependencias necesarias

En `androidApp/build.gradle.kts` (o donde corresponda en el proyecto KMP):

- `org.osmdroid:osmdroid-android:6.1.18`
- `org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.3`
- Plugin de serialización: `kotlin("plugin.serialization")` en el módulo
  `shared`.

### 4. Permisos en `AndroidManifest.xml`

- `INTERNET` — obligatorio para que OSMDroid descargue los tiles del mapa.
- `ACCESS_NETWORK_STATE` — para verificar conectividad antes de cargar tiles.
- `ACCESS_FINE_LOCATION` — declararlo aunque no se use GPS activamente; da
  opción a centrar el mapa en la ubicación real del operador si se desea.

### 5. Inicialización de OSMDroid

Antes de renderizar cualquier mapa, OSMDroid necesita ser inicializado con
el contexto de la aplicación y un `userAgentValue` (puede ser el
`packageName`). Hacerlo en la `Application` class o al inicio de
`MainActivity`, no dentro del Composable.

### 6. Pantallas de la app (navegación entre 2 pantallas)

**Pantalla A — Formulario de Admisión**

- Campos de texto para: remitente, destinatario, dirección de origen
  (referencial, no coordenadas).
- Un área que muestre las coordenadas seleccionadas (lat/lng), inicialmente
  vacía o con texto "Sin ubicación fijada".
- Botón "Fijar en mapa" → navega a Pantalla B.
- Botón "Registrar Admisión" → solo habilitado cuando todos los campos
  están llenos Y hay coordenadas confirmadas desde el mapa. Al presionar,
  construye el objeto `Paquete` completo y navega a una pantalla de
  confirmación (o muestra un diálogo/card de resumen) y habilita el botón
  de envío del Intent (ver `planParte2.md`).

**Pantalla B — Mapa OSMDroid**

- Renderizar un `MapView` de OSMDroid dentro de un `AndroidView` Composable
  (así se integra una View nativa de Android dentro de Compose).
- Centro inicial del mapa: coordenadas de Quito, Ecuador
  (lat: -0.1807, lng: -78.4678), zoom nivel 14.
- Comportamiento de tap: al tocar cualquier punto del mapa, colocar (o
  mover si ya existe uno) un `Marker` en esa posición. Usar
  `MapEventsOverlay` con el callback `singleTapConfirmedHelper` para
  capturar el tap.
- Mostrar debajo del mapa (fuera del `MapView`) las coordenadas actuales
  del marcador en tiempo real, con 6 decimales de precisión.
- El marcador debe tener un ícono personalizado (puede ser el pin
  predeterminado de OSMDroid o un drawable propio del proyecto; no dejar
  el ícono vacío/null porque lanza excepción en OSMDroid).
- Botón "Confirmar ubicación" → regresa a Pantalla A pasando las coordenadas
  seleccionadas. Solo habilitado si hay un marcador colocado.
- Botón "Cancelar" → regresa sin cambios.

### 7. Comportamiento esperado completo (flujo de usuario)

1. El operador abre la app y ve el formulario vacío.
2. Llena los campos de texto.
3. Toca "Fijar en mapa" → se abre el mapa centrado en Quito.
4. Toca el punto exacto de recolección en el mapa → aparece el marcador y
   se muestran las coordenadas.
5. Si se equivoca, toca otro punto → el marcador se mueve al nuevo punto.
6. Toca "Confirmar ubicación" → regresa al formulario con las coordenadas
   ya visibles.
7. Toca "Registrar Admisión" → se construye el objeto `Paquete`, aparece
   un resumen (card o diálogo) con todos los datos del paquete incluyendo
   las coordenadas, y se habilita el botón "Enviar a Distribución".

---

## Restricciones y aclaraciones

- No implementar GPS/geolocalización automática en esta app. El marcador
  es 100% manual (el operador decide dónde colocarlo).
- No usar Google Maps SDK ni Mapbox; únicamente OSMDroid.
- No requerir API Key de ningún tipo.
- La lógica de envío del Intent al presionar "Enviar a Distribución" está
  definida en `planParte2.md` y debe implementarse después de tener esta
  parte funcional.
- El proyecto debe compilar y correr en un emulador Android estándar de
  Android Studio (OSMDroid no requiere Google Play Services).
