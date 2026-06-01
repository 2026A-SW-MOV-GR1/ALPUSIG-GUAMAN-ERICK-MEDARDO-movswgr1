# Examen Práctico: Persistencia Dual en Móviles
**FIS — Programación de Aplicaciones Móviles | Escuela Politécnica Nacional**
**Framework: Kotlin Multiplatform (Android Target) | Semestre 2026**

---

## Contexto

Transforma tu CRUD básico en un sistema adaptable capaz de cambiar su motor de datos sin alterar la interfaz de usuario. La aplicación debe soportar dos modos de almacenamiento local que pueden conmutarse en tiempo de ejecución.

---

## 1. Mecanismo de Conmutación Dual

La aplicación debe implementar dos posiciones de almacenamiento que funcionen de forma independiente:

### Posición A — Relacional (SQL)
- Usar **SQLDelight (Android Target)** como motor principal.
- Las operaciones CRUD se ejecutan sobre tablas con esquema fijo: llaves, tipos y columnas definidos.
- Los datos guardados en este modo **solo existen en el almacén SQL**; no se sincronizan al almacén NoSQL.

### Posición B — No Relacional (NoSQL)
- Usar **KStore** o **Realm Local** como motor alternativo.
- La persistencia opera sobre colecciones o documentos JSON sin esquema estricto.
- Los datos guardados en este modo **solo existen en el almacén NoSQL**; no se sincronizan al almacén SQL.

> Ambos motores deben estar activos y operativos al mismo tiempo. Cambiar de modo **no borra ni migra** los datos del otro motor.

---

## 2. Requisitos de Interfaz de Usuario

### 2.1 Control Switch en AppBar
- Añadir un `Switch` o `ToggleButton` en la barra superior (`TopAppBar`) de la pantalla CRUD.
- El switch debe ser interactivo y visible en todo momento mientras se usa la pantalla.

### 2.2 Reactividad Instantánea
- Al alternar el switch, la lista de datos debe actualizarse **al instante**, sin reiniciar la app ni navegar a otra pantalla.
- No se permite hacer `finish()` + relaunch, ni ninguna forma de reinicio de Activity/Fragment para lograr la actualización.

### 2.3 Indicador de Origen Activo
- Implementar una etiqueta visual clara en la UI (chip de color, badge o texto explicativo) que indique en todo momento si la lista está leyendo desde **SQLite** o desde **NoSQL**.
- El indicador debe cambiar junto con el switch, de forma reactiva.

---

## 3. Principios de Ingeniería Exigidos

### 3.1 Patrón Repositorio
- Las vistas (Composables o Activities) **no deben llamar directamente** al motor SQL ni al NoSQL.
- Implementar una **interfaz común** (`ItemRepository` o equivalente) que abstraiga las operaciones CRUD.
- Ambas implementaciones (`SQLiteItemRepository` y `NoSQLItemRepository`) deben cumplir esa misma interfaz.
- El ViewModel solo interactúa con la interfaz, nunca con las implementaciones concretas directamente.

### 3.2 Logs Estructurados
- Cada operación de escritura en base de datos (insert, update, delete) debe imprimir una traza en la consola con su tipo:
  - `[DEBUG]` para operaciones de lectura o estado interno.
  - `[INFO]` para escrituras exitosas y cambios de motor.
  - `[ERROR]` para fallos en operaciones de persistencia.
- Cada cambio de motor (SQL → NoSQL o viceversa) debe también emitir un log `[INFO]` indicando el nuevo modo activo.

### 3.3 Pruebas Unitarias
- Escribir **al menos dos pruebas locales** (unit tests en `commonTest` o `androidTest`) que validen:
  1. La escritura correcta en una de las capas de repositorio (SQL o NoSQL).
  2. Que al cambiar el motor, la fuente de datos cambia efectivamente (un registro guardado en SQL no aparece al consultar en modo NoSQL).
- Las pruebas deben correr y pasar sin necesidad de un dispositivo físico o emulador (preferible usar fakes/mocks para los repositorios).

---

## 4. Tecnologías Sugeridas para Kotlin Multiplatform

| Capa | Mapeo Relacional (SQL) | Mapeo No Relacional (NoSQL) | Estrategia de Estado |
|---|---|---|---|
| **Kotlin Multiplatform** | SQLDelight (Android Target) | KStore / Realm Local | StateFlow / Compose Runtime |

---

## 5. Entregables del Examen

### 5.1 Código Fuente Limpio
- Subir el proyecto a un **repositorio personal de Git** (GitHub, GitLab, etc.).
- El código debe estar **estructurado en capas lógicas**:
  - `model/` — entidades de dominio.
  - `repository/` — interfaz + implementaciones SQL y NoSQL.
  - `viewmodel/` — lógica de presentación y manejo del switch.
  - `ui/` — Composables o vistas, sin lógica de acceso a datos.
- No se acepta lógica de base de datos dentro de Composables, Activities o Fragments.

### 5.2 Pruebas Ejecutables
- Las pruebas unitarias deben ejecutarse en tiempo real desde el IDE o con `./gradlew test`.
- Deben **pasar exitosamente** al momento de la entrega.
- Incluir al menos los dos casos descritos en la sección 3.3.

### 5.3 Demostración de Estado Independiente
- El proyecto debe poder demostrar de forma interactiva que:
  - Un registro guardado en modo SQL **no aparece** al cambiar a modo NoSQL.
  - Un registro guardado en modo NoSQL **no aparece** al cambiar a modo SQL.
- Esta demostración se realiza en vivo durante la sustentación.

---

## 6. Rúbrica de Calificación

| Criterio | Peso | Descripción |
|---|---|---|
| **Conmutación Correcta** | 40% | Guardado e impresión de datos independientes entre SQL y NoSQL con actualización reactiva en UI. |
| **Abstracción y Arquitectura** | 20% | Uso correcto del Patrón Repositorio y logs estructurados de auditoría. |
| **Suite de Pruebas Unitarias** | 20% | Pruebas de integración locales corriendo y validadas con éxito. |
| **Sustentación Oral** | 20% | Defensa metodológica del diseño y las decisiones del código. |

**Peso total del examen: 40% de la nota final de la materia.**

---

## 7. Sustentación del Examen

Durante la sustentación debes demostrar dominio sobre:

- El **ciclo de vida del dispositivo móvil** en relación con la persistencia (qué pasa con los datos al cerrar la app, al rotar la pantalla, etc.).
- El **manejo de hilos de persistencia** (por qué las operaciones de base de datos no deben correr en el hilo principal, cómo se maneja con coroutines en KMP).
- La **consistencia en la reactividad de la UI** (cómo el `StateFlow` o `SharedFlow` garantiza que la lista siempre refleja el estado real del motor activo).
- Las **decisiones de diseño** tomadas: por qué se eligió esa estructura de capas, ese motor NoSQL, ese mecanismo de conmutación.

---

*Materia: Aplicaciones Móviles | Semestre 2026 | FIS — EPN*
