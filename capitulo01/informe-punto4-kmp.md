# Informe Punto 4: KMP / Compose Multiplatform

Este documento recopila el proceso, decisiones, errores y resultados para cumplir el punto 4 del taller: gestion de recursos compartidos vs nativos usando Kotlin Multiplatform (KMP) y Compose Multiplatform.

## Objetivo del punto 4
**Requerimiento:** Usar el sistema de recursos de Compose Multiplatform (recursos comunes) y demostrar como los calificadores del set comun o del modulo Android resuelven la interfaz. Se valora el uso de `expect/actual` si se consulta `R.string` de Android.

## Estado final (cumplimiento)
- **KMP real con modulo shared**: UI Compose en `shared` y `:app` como host.
- **Recursos comunes CMP**: `shared/src/commonMain/composeResources/values/strings.xml`.
- **Recursos Android con calificadores**: `shared/src/androidMain/res/values*` para idioma y orientacion.
- **expect/actual**: `AppResources` consulta `R.string`/`R.color` en Android y usa recursos CMP para previsualizacion.
- **Demostracion de resolucion**:
  - En runtime Android se aplican `values`, `values-en`, `values-land`, `values-en-land`.
  - En Preview (inspection) se ve el recurso comun CMP, demostrando la capa compartida.

## Estructura del proyecto (relevante al punto 4)
- `shared/src/commonMain/kotlin/...`:
  - `AppScreen` (UI Compose compartida).
  - `AppResources` con `expect` para strings/colores.
- `shared/src/androidMain/kotlin/...`:
  - `AppResources.android.kt` con `actual` que lee `R.*` de Android.
- `shared/src/commonMain/composeResources/values/strings.xml`:
  - Recursos comunes CMP.
- `shared/src/androidMain/res/values*`:
  - Recursos Android con calificadores.

## Pasos realizados (resumen cronologico)
1. **Crear modulo `shared` KMP** y mover la UI Compose a `commonMain` (`AppScreen`).
2. **Host Android** (`:app`) solo llama a `AppScreen()` desde `MainActivity`.
3. **Implementar `expect/actual`**:
   - `expect` en `commonMain` para texto y colores.
   - `actual` en `androidMain` usando `stringResource`/`colorResource` con `R.*`.
4. **Agregar recursos Android con calificadores** en `shared/src/androidMain/res/values*`:
   - `values`, `values-en`, `values-land`, `values-en-land`.
5. **Habilitar recursos Compose Multiplatform**:
   - Plugin `org.jetbrains.compose`.
   - `compose.components.resources` en `commonMain`.
   - Recursos comunes en `shared/src/commonMain/composeResources/values/strings.xml`.
6. **Mantener colores en Android** (ver consideraciones):
   - Los colores se definen en `values*` Android, ya que CMP no admite `<color>` en XML.

## Errores encontrados y soluciones
1. **AGP 9 + KMP incompatibilidad**
   - Error: `com.android.library` no compatible con `kotlin-multiplatform`.
   - Solucion temporal: usar propiedades de compatibilidad en `gradle.properties`:
     - `android.builtInKotlin=false`
     - `android.newDsl=false`
2. **App no abre (ANR o crash)**
   - Causa: `MainActivity` no se encontraba (Kotlin no compilaba en `:app`).
   - Solucion: aplicar plugin `org.jetbrains.kotlin.android` en `:app`.
3. **Inconsistent JVM target**
   - Error: Java 11 vs Kotlin 21.
   - Solucion: `kotlinOptions { jvmTarget = "11" }`.
4. **Compose resources y Android SourceSet**
   - Error: `You cannot add Provider instances to the Android SourceSet API`.
   - Solucion: `android.sourceset.disallowProvider=false` en `gradle.properties`.
5. **CMP no acepta `<color>` en XML**
   - Error: `Unknown resource type: 'color'`.
   - Solucion: dejar colores en Android (`R.color`) y solo strings en CMP.
6. **Package de recursos CMP**
   - Error: `Unresolved reference Res`.
   - Solucion: usar el paquete generado correcto:
     - `moviles2026aswgr1.shared.generated.resources`.

## Consideraciones clave (para la presentacion)
- **CMP + Android no se excluyen**: los recursos comunes sirven como base y Android puede sobrescribir con calificadores.
- **Demostracion de resolucion**:
  - En Android se usan los calificadores (`values-en`, `values-land`) y el sistema elige el mas especifico.
  - En CMP (Preview) se toma el recurso comun (`composeResources`).
- **expect/actual justificado**:
  - Permite usar `R.string` y `R.color` en Android, cumpliendo el requerimiento del enunciado.
  - La capa comun mantiene la UI y la API de recursos unificada.
- **Limitacion actual de CMP**:
  - Los colores no se declaran como `<color>` en `composeResources/values`.
  - Por eso se mantienen en Android; esto no viola el requisito porque el texto si se define en CMP.
- **Demostracion en runtime (minimo cambio)**:
  - `AppScreen` acepta `useCommonResources: Boolean = false`.
  - Si se pasa `true`, la app usa el string comun CMP en runtime.
  - Si se deja en `false`, usa `R.string` con calificadores Android.

## Evidencias (archivos clave)
- `shared/src/commonMain/composeResources/values/strings.xml` (recurso comun CMP).
- `shared/src/commonMain/kotlin/.../AppResources.kt` (expect y acceso a CMP).
- `shared/src/androidMain/kotlin/.../AppResources.android.kt` (actual con `R.*`).
- `shared/src/androidMain/res/values*` (calificadores Android).
- `app/src/main/java/.../MainActivity.kt` (host Android).

## Respuesta directa al punto 4
- **Se usa el sistema de recursos CMP**: si, en `composeResources` para strings.
- **Se demuestra interaccion entre recursos comunes y Android**: si, Android usa `R.*` con calificadores y CMP se usa como base/preview.
- **Se usa expect/actual**: si, en `AppResources` para `R.string`/`R.color`.

## Recomendaciones para la presentacion
- Mostrar comparacion en tiempo real:
  - Cambiar idioma y orientacion para ver `values*` Android.
  - Abrir Preview para ver el string comun CMP.
- Explicar el flujo de resolucion:
  1) UI en `commonMain`.
  2) `expect` de recursos.
  3) `actual` Android usa `R.*` (calificadores).
  4) CMP como capa comun (string base).
- Para mostrar interaccion en runtime, ejecutar temporalmente:
  - `AppScreen(useCommonResources = true)`
  - Luego volver a `false` para mostrar Android `values*`.
