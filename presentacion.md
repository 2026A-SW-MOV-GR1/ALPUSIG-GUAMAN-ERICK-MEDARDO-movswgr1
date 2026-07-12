# Guía de Presentación: App Ciclo de Vida

Esta guía detalla los puntos clave para explicar el funcionamiento y propósito de la aplicación "Ciclo de Vida".

---

## 1. Introducción y Objetivo
*   **Nombre del Proyecto:** Ciclo de Vida.
*   **Objetivo Principal:** Visualizar y entender el comportamiento de los componentes de Android (Activity y ViewModel) durante las diferentes etapas de su ciclo de vida y ante cambios de configuración (como la rotación de pantalla).
*   **Tecnologías:** Kotlin Multiplatform (KMP), Jetpack Compose y Android Jetpack Lifecycle.

## 2. Arquitectura de la Aplicación
*   **MainActivity:** Es el punto de entrada. Maneja la UI de Compose y gestiona el registro del `LifecycleObserver`.
*   **AppLifecycleObserver:** Clase dedicada a monitorear los eventos de la Activity (`onCreate`, `onStart`, etc.) de forma desacoplada usando la interfaz `DefaultLifecycleObserver`.
*   **CounterViewModel:** Almacena el estado del contador. Es crucial porque sobrevive a la destrucción de la Activity durante una rotación.
*   **UI (Compose):** Interfaz reactiva que se actualiza automáticamente cuando el estado en el ViewModel cambia.

## 3. Puntos Clave para Explicar (Demostración)

### A. El Ciclo de Vida en el Log
*   Muestra cómo al abrir, pausar o cerrar la app, se disparan mensajes en el **Logcat** con la etiqueta `LIFECYCLE_EVENTS`.
*   Explica que el `AppLifecycleObserver` nos permite reaccionar a estos eventos sin saturar de código la `MainActivity`.

### B. Rotación de Pantalla y Persistencia
*   **El Problema:** Normalmente, al rotar la pantalla, la Activity se destruye y se vuelve a crear, lo que causaría la pérdida de datos (como el valor del contador).
*   **La Solución:** Muestra que al rotar el dispositivo, el contador **no vuelve a cero**. Esto ocurre porque el `ViewModel` permanece en memoria mientras la Activity se recrea.
*   Muestra los logs de `onSaveInstanceState` y cómo el ViewModel no llama a `onCleared` hasta que la app se cierra definitivamente.

### C. Recomposición en Compose
*   Menciona que cada vez que el contador cambia, la función `App()` se "recompone". Hay un log específico (`App Recomposed`) que demuestra este comportamiento reactivo.

## 4. Conclusión
*   Este proyecto demuestra la importancia de separar la lógica de negocio y el estado (en el ViewModel) de la lógica de la UI y el sistema (en la Activity).
*   Facilita la creación de aplicaciones robustas que no pierden información y que responden correctamente a las interrupciones del sistema.

---

### Tips para la Presentación:
1.  **Abre el Logcat** antes de empezar y filtra por "LIFECYCLE".
2.  **Interactúa con el contador**, luego rota la pantalla y destaca que el número se mantiene.
3.  **Presiona el botón de "Recientes"** (cuadrado) y vuelve a entrar para mostrar `onPause` y `onResume`.
