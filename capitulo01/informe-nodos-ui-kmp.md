# Informe: Nodos nativos en jerarquia de vistas (KMP/Compose)

## Alcance
Este informe cubre el caso **Kotlin Multiplatform + Compose (Nativo Moderno)** del taller.

## Conteo de nodos nativos (Android View Hierarchy)
> Nota: no se puede inferir el numero exacto de nodos nativos sin inspeccionar el arbol de vistas en ejecucion.
> Para la diapositiva, usa el conteo obtenido con Layout Inspector (pasos abajo) y completa los campos.

- **Pantalla de listado (Read)**
  - Nodos nativos (View tree): **[COMPLETAR]**
- **Pantalla de formulario (Create/Update)**
  - Nodos nativos (View tree): **[COMPLETAR]**
- **Dialogo de eliminacion (Delete)**
  - Nodos nativos (View tree): **[COMPLETAR]**

### Como medir (Layout Inspector)
1. Ejecuta la app en dispositivo/emulador.
2. Abre **Android Studio > App Inspection > Layout Inspector**.
3. Selecciona el proceso `com.example.moviles2026aswgr1`.
4. Cambia a cada pantalla (Listado, Formulario, Dialogo).
5. En el panel de jerarquia, cuenta los nodos **View** (no los composables). El root suele ser `AndroidComposeView`.
6. Anota el total y colocalo arriba.

## Interpretacion
- **Compose** dibuja la UI sobre un lienzo; la jerarquia nativa suele ser muy pequeña
  (pocas Views, tipicamente `AndroidComposeView` y contenedores del Window).
- Por eso el numero de nodos nativos **no** crece con los componentes Compose.

## Verificacion de cumplimiento del taller (KMP/Compose)
**Requerimientos solicitados**
- Lista: `LazyColumn`.
- Inputs: `OutlinedTextField`.
- Dialogo: `AlertDialog`.
- Feedback: `Toast.makeText`.
- Interaccion: click item -> editar, FAB -> crear, boton rojo -> confirmar + toast.
- Sin librerias UI externas.

**Estado en el proyecto**
- ✅ Lista: `LazyColumn` implementado en `AppScreen.kt`.
- ✅ Formulario: `OutlinedTextField` + `Switch` en `AppScreen.kt`.
- ✅ Dialogo: `AlertDialog` en `AppScreen.kt`.
- ✅ Toast: `rememberToast()` con `Toast.makeText` en `PlatformFeedback.android.kt`.
- ✅ Navegacion: List ↔ Form por estado en `AppScreen.kt`.
- ✅ Datos hardcoded: 3 items.

## Archivos clave
- `shared/src/commonMain/kotlin/com/example/moviles2026aswgr1/shared/AppScreen.kt`
- `shared/src/commonMain/kotlin/com/example/moviles2026aswgr1/shared/PlatformFeedback.kt`
- `shared/src/androidMain/kotlin/com/example/moviles2026aswgr1/shared/PlatformFeedback.android.kt`

## Texto corto para diapositiva
"En Compose, la UI se renderiza en un lienzo. La jerarquia nativa se mantiene minima (pocas Views),
por eso el conteo de nodos nativos no crece con los componentes Compose. La validacion se realiza
con Layout Inspector en cada pantalla (Listado, Formulario, Dialogo)."

