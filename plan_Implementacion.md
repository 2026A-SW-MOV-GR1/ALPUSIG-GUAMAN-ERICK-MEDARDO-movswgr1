# Prompt Maestro: Reto de Ciclo de Vida y Persistencia en KMP

## 1. Rol y Contexto
Actúa como un desarrollador Senior experto en Kotlin Multiplatform (KMP), Compose Multiplatform y arquitectura de Android. 
Actualmente participo en un taller técnico denominado *"La batalla del estado: Ciclo de vida y persistencia en ecosistemas móviles"*. Mi asignación específica es resolver este desafío utilizando **Kotlin Multiplatform (KMP)**.

## 2. El Desafío Técnico Base
El sistema operativo Android destruye y recrea la "Actividad" al girar la pantalla (Configuration Change), lo que causa pérdida de datos si no están correctamente gestionados.
Debo implementar una aplicación de Contador con las siguientes características mínimas en KMP:
* Un botón que sume +1 a una variable `count`.
* Un texto que muestre ese valor en pantalla.

## 3. El Objetivo Principal (Persistencia)
El requerimiento crítico es que si el contador llega a un número (por ejemplo, 10) y se gira el dispositivo, **el valor debe mantenerse intacto**.
* En Android Nativo, esto se hace con `onSaveInstanceState` y `onRestoreInstanceState`.
* **Tu tarea:** Debes proporcionarme la solución técnica equivalente y óptima para KMP (por ejemplo, utilizando `rememberSaveable`, la gestión de estado de Compose Multiplatform, o un `ViewModel` multiplataforma si es necesario).

## 4. Pruebas y Logs de Ciclo de Vida (El Experimento)
Una vez implementado el contador persistente, necesito que el código incluya la impresión en consola (Logs) para analizar los siguientes escenarios:
* **Rotación:** Girar el celular y confirmar que el estado se mantiene.
* **Multitarea:** Salir al "Home" y volver a la app.

Necesito que me indiques cómo suscribirse y registrar en KMP (o en la capa específica de Android dentro del proyecto KMP) los equivalentes a los siguientes eventos nativos:
* `onCreate`
* `onStart`
* `onResume`
* `onPause`
* `onStop`
* `onDestroy`
* `onRestart`

## 5. Entregables Esperados
Por favor, genera tu respuesta estructurada de la siguiente manera:
1. **Código Solución KMP:** El código fuente necesario (UI en Compose Multiplatform y lógica de estado) para que el contador funcione y sobreviva a la rotación de la pantalla.
2. **Mapeo de Logs:** Una explicación clara de cómo se interceptan los eventos de ciclo de vida en este entorno y la secuencia de logs que se imprimirán en consola durante una rotación (detallando específicamente en qué momento se dispara el equivalente a `onDestroy`).
3. **Explicación Técnica:** Una breve defensa de las funciones, *hooks* o enfoques utilizados para evitar la pérdida de información en KMP. Esta explicación debe ser clara y estar lista para ser expuesta en la demostración final del taller.