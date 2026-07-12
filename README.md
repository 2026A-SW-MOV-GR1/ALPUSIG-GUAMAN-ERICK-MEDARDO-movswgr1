# Proyecto: Ciclo de Vida (Contador Persistente)

Este es un proyecto educativo desarrollado con **Kotlin Multiplatform (KMP)** y **Jetpack Compose**. Su objetivo principal es demostrar y visualizar los eventos del ciclo de vida de Android y la persistencia de datos mediante el uso de `ViewModel`.

## 🚀 Propósito del Proyecto
La aplicación muestra un contador simple que permite incrementar y decrementar un valor. Lo relevante ocurre "detrás de escena":
- **Monitoreo de Ciclo de Vida:** Utiliza un `LifecycleObserver` para registrar en el Logcat cada etapa de la Activity (`onCreate`, `onStart`, `onResume`, etc.).
- **Persistencia ante Rotación:** Gracias al uso de `CounterViewModel`, el estado del contador se mantiene intacto incluso cuando el dispositivo se rota y la Activity se recrea.
- **Arquitectura Reactiva:** La UI se actualiza automáticamente ante cambios de estado en el ViewModel.

## 🏗️ Estructura del Proyecto
- **[`shared/commonMain`](./shared/src/commonMain/kotlin):** Contiene la lógica central.
  - `App.kt`: Interfaz de usuario construida con Compose Multiplatform.
  - `CounterViewModel.kt`: Gestiona el estado del contador y sobrevive a cambios de configuración.
- **[`androidApp`](./androidApp/src/main/kotlin/com/example/ciclo_de_vida):** Contiene la implementación específica para Android.
  - `MainActivity.kt`: Punto de entrada que gestiona el ciclo de vida y registra el observador.
  - `AppLifecycleObserver.kt`: Clase que escucha y loguea los eventos de la aplicación.

## 📱 Cómo Probar la App
1. **Ejecutar la App:** Usa `./gradlew :androidApp:assembleDebug` o el botón "Run" en Android Studio.
2. **Observar Logs:** Abre el **Logcat** y filtra por la etiqueta `LIFECYCLE_EVENTS` para ver cómo reacciona la app a tus acciones.
3. **Probar Persistencia:** Incrementa el contador y **rota la pantalla**. Verás que el número no se pierde, demostrando la eficacia del ViewModel.

## 🛠️ Tecnologías Utilizadas
- **Kotlin Multiplatform**
- **Jetpack Compose** (UI Declarativa)
- **Android Jetpack Lifecycle** (`ViewModel`, `LifecycleObserver`)
- **Material Design 3**

---

*Desarrollado como parte del Taller 07 sobre el Ciclo de Vida en Android.*
