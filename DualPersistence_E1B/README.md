# DualPersistence_E1B — Persistencia Dual KMP (Android)

Proyecto Kotlin Multiplatform (Android target) que implementa persistencia dual conmutables en tiempo real: SQLDelight (SQLite) y KStore (NoSQL). La UI reacciona al cambio de modo sin reiniciar la app.

## Funcionalidad clave

- Conmutación en tiempo real entre modo SQL y NoSQL desde la UI.
- Datos independientes: lo guardado en SQL no aparece en NoSQL y viceversa.
- Patrón repositorio con una interfaz común.
- Logs estructurados para lecturas, escrituras y cambios de motor.
- Pruebas unitarias locales en `shared/src/commonTest`.

## Arquitectura

- `model/` — entidades de dominio (`Item`, `StorageMode`).
- `repository/` — `ItemRepository` + implementaciones `SQLiteItemRepository` y `NoSqlItemRepository`.
- `repository/StorageToggleManager` — estado de conmutación con `StateFlow`.
- `repository/DualRepositoryProvider` — selecciona el repositorio activo con `flatMapLatest`.
- `ui/` + `viewmodel/` — ViewModel y pantalla Compose con switch + indicador de modo.

## Estructura de carpetas

- `androidApp/` — app Android (Compose).
- `shared/` — lógica común KMP.
  - `shared/src/commonMain/kotlin` — modelos, repositorios, ViewModel, UI.
  - `shared/src/commonMain/sqldelight` — esquema SQLDelight.
  - `shared/src/androidMain/kotlin` — integraciones Android (drivers, paths, etc.).

## Esquema SQLDelight

Archivo: `shared/src/commonMain/sqldelight/com/example/dualpersistence_e1b/db/Item.sq`

Incluye:
- `CREATE TABLE Item ...`
- `getAllItems`, `insertItem`, `updateItem`, `deleteItem`

## Conmutación de almacenamiento

- Modo activo controlado por `StorageToggleManager`.
- La lista se actualiza de forma reactiva al cambiar el switch.
- Indicador visual muestra si la fuente actual es **SQLite** o **NoSQL**.

## Logs

Las operaciones de persistencia emiten logs con etiquetas:
- `[DEBUG]` lecturas / estado interno.
- `[INFO]` escrituras exitosas y cambios de motor.
- `[ERROR]` fallos de persistencia.

## Requisitos

- Android Studio (con soporte KMP).
- JDK 11.
- Gradle Wrapper incluido.

## Ejecutar

```powershell
./gradlew :androidApp:assembleDebug
```

También puedes ejecutar desde Android Studio usando las run configurations.

## Pruebas

```powershell
./gradlew :shared:test
./gradlew :shared:testAndroidHostTest
```

## Notas

- No hay sincronización entre SQL y NoSQL: los datos permanecen en su motor.
- La configuración de versiones vive en `gradle/libs.versions.toml`.

---

Más info: https://www.jetbrains.com/help/kotlin-multiplatform-dev/get-started.html
