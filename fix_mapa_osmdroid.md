# Fix — Mapa OSMDroid sin tiles (cuadrícula gris en blanco)

## Diagnóstico

La pantalla del mapa abre correctamente pero muestra una cuadrícula gris
vacía. Esto significa que el `MapView` está inicializado y renderizado, pero
OSMDroid **no puede descargar ni mostrar los tiles** del mapa. No es un
error de código de UI; es un problema de configuración o conectividad.

---

## Causas posibles (verificar en orden)

### Causa 1 — `Configuration.getInstance()` no se ejecutó antes del MapView

OSMDroid requiere que su configuración global se inicialice **antes** de que
cualquier `MapView` sea creado. Si esto se hace dentro del Composable o
después de que la pantalla ya renderizó, el mapa aparece vacío.

**Dónde debe estar la inicialización:**
- En una clase `Application` personalizada, en su método `onCreate()`.
- O en `MainActivity.onCreate()`, antes de llamar a `setContent { }`.
- **Nunca** dentro del bloque `AndroidView { }` ni en un `LaunchedEffect`.

**Qué debe incluir la inicialización:**
- Cargar la configuración con el contexto de la aplicación y las
  `SharedPreferences` del paquete.
- Asignar el `userAgentValue` con el `packageName` de la app.
  Este campo no puede quedar vacío ni con el valor por defecto
  `"osmdroid"` genérico — algunos servidores de tiles rechazan
  peticiones con ese user agent.

Si la app no tiene una clase `Application` personalizada declarada en el
`AndroidManifest.xml`, crearla y declararla es el primer paso.

---

### Causa 2 — El emulador no tiene acceso a internet

OSMDroid descarga los tiles en tiempo real desde los servidores de
OpenStreetMap. Si el emulador no tiene conectividad, el mapa queda gris.

**Cómo verificar en Android Studio:**
- Abrir el emulador → los tres puntos (Extended controls) →
  sección "Cellular" → asegurarse de que el estado sea "Home" y
  la velocidad no sea "No network".
- Alternativamente, abrir el navegador dentro del emulador y probar
  cargar cualquier página web. Si no carga, el problema es de red.

**Fix si el emulador no tiene internet:**
- En Android Studio: `Settings > Tools > Emulator` → verificar que no
  haya proxy configurado que bloquee el tráfico.
- Reiniciar el emulador desde `Device Manager` con "Cold Boot".
- Si el problema persiste, usar un dispositivo físico con datos móviles
  o WiFi para la prueba.

---

### Causa 3 — Permiso `INTERNET` no declarado o no encontrado en el módulo correcto

En proyectos KMP, el `AndroidManifest.xml` puede estar en
`androidApp/src/main/` o en otro lugar dependiendo de cómo se generó el
proyecto. Verificar que el permiso esté en el manifest correcto —
el que realmente empaqueta el APK.

**Permisos que deben estar presentes:**
- `android.permission.INTERNET`
- `android.permission.ACCESS_NETWORK_STATE`

Sin `INTERNET`, OSMDroid no puede hacer ninguna petición HTTP para
descargar tiles, y el mapa queda gris sin lanzar excepción visible.

---

### Causa 4 — El `MapView` no tiene `setTileSource` configurado

Por defecto OSMDroid debería usar `TileSourceFactory.MAPNIK` (el tile
de OpenStreetMap), pero en algunas versiones o configuraciones el tile
source queda en `null`. Dentro del bloque donde se configura el `MapView`
(en el `AndroidView` factory), asegurarse de que se establezca
explícitamente el tile source a `TileSourceFactory.MAPNIK` antes de
cualquier otra configuración del mapa.

---

### Causa 5 — El `MapView` no tiene `setMultiTouchControls(true)` y `isClickable = true`

Si el `MapView` no recibe estas propiedades, puede renderizar parcialmente
o no reaccionar al ciclo de vida correctamente. Asegurarse de que en la
configuración del `MapView` dentro del `AndroidView`:
- `setMultiTouchControls(true)` esté activo.
- `isClickable = true` y `isFocusable = true` estén asignados.

---

### Causa 6 — El ciclo de vida del `MapView` no está conectado

OSMDroid necesita que el `MapView` reciba los callbacks de ciclo de vida:
`onResume()` y `onPause()`. Si no se llaman, el mapa puede aparecer
vacío o congelado.

En Compose, esto se maneja con `DisposableEffect` escuchando el
`LocalLifecycleOwner`. El efecto debe:
- Llamar `mapView.onResume()` cuando el ciclo de vida entre en `ON_RESUME`.
- Llamar `mapView.onPause()` cuando entre en `ON_PAUSE`.
- Llamar `mapView.onDetach()` en el bloque `onDispose` del
  `DisposableEffect`.

Si el `MapView` se crea en el `AndroidView` pero no hay ningún
`DisposableEffect` conectando el ciclo de vida, los tiles nunca se
solicitan correctamente.

---

## Lista de verificación (aplicar en orden)

- [ ] Existe una clase `Application` personalizada con la inicialización
      de `Configuration.getInstance()` en `onCreate()`.
- [ ] Esa clase está declarada en `AndroidManifest.xml` con el atributo
      `android:name`.
- [ ] El `userAgentValue` está asignado con el `packageName` real de la app
      (no el string `"osmdroid"` por defecto).
- [ ] Los permisos `INTERNET` y `ACCESS_NETWORK_STATE` están en el
      `AndroidManifest.xml` del módulo que genera el APK.
- [ ] El emulador tiene acceso a internet (verificar con el navegador).
- [ ] El `MapView` tiene `setTileSource(TileSourceFactory.MAPNIK)` explícito.
- [ ] El `MapView` tiene `setMultiTouchControls(true)`.
- [ ] Hay un `DisposableEffect` en el Composable que llama `onResume()`,
      `onPause()` y `onDetach()` en los momentos correctos del ciclo de vida.

---

## Resultado esperado tras el fix

Al abrir la pantalla del mapa, en 1-3 segundos (dependiendo de la
velocidad del emulador) deben aparecer los tiles de OpenStreetMap
mostrando el mapa de Quito centrado en lat: -0.1807, lng: -78.4678
con zoom nivel 14. Las calles, edificios y nombres de lugares deben
ser visibles, exactamente como se vería OpenStreetMap en un navegador.
