# Setup: Proyecto Kotlin Multiplatform — Persistencia Dual

## Paso 1 — Crear el proyecto en Android Studio

1. `File → New → New Project`
2. Seleccionar plantilla **"Kotlin Multiplatform App"** (sección Mobile)
3. Configurar:
   - **Name:** `DualPersistenceApp`
   - **Package:** `com.tuusuario.dualpersistence`
   - **Minimum SDK:** API 26 (Android 8.0)
4. **Finish**

> Si no aparece la plantilla: `Settings → Plugins → buscar "Kotlin Multiplatform" → Install → Restart`.

---

## Paso 2 — Estructura esperada del proyecto

```
DualPersistenceApp/
├── composeApp/
│   └── src/
│       ├── androidMain/
│       └── commonMain/
├── shared/
│   └── src/
│       ├── commonMain/
│       ├── androidMain/
│       └── commonTest/
└── build.gradle.kts
```

Todo el código de modelos, repositorios y ViewModels va en `shared/src/commonMain`.

---

## Paso 3 — Plugins en `build.gradle.kts` raíz

```kotlin
plugins {
    alias(libs.plugins.kotlinMultiplatform).apply(false)
    alias(libs.plugins.androidApplication).apply(false)
    kotlin("plugin.serialization") version "2.0.0" apply false
    id("app.cash.sqldelight") version "2.0.2" apply false
}
```

---

## Paso 4 — Dependencias en `shared/build.gradle.kts`

```kotlin
plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization")
    id("app.cash.sqldelight")
}

kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.8.1")
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.7.1")
                implementation("io.github.xxfast:kstore:0.8.0")
                implementation("io.github.xxfast:kstore-file:0.8.0")
            }
        }
        val androidMain by getting {
            dependencies {
                implementation("app.cash.sqldelight:android-driver:2.0.2")
                implementation("app.cash.sqldelight:coroutines-extensions:2.0.2")
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.8.1")
            }
        }
    }
}
```

---

## Paso 5 — Configurar SQLDelight en `shared/build.gradle.kts`

Agregar al final del archivo:

```kotlin
sqldelight {
    databases {
        create("AppDatabase") {
            packageName.set("com.tuusuario.dualpersistence.db")
        }
    }
}
```

---

## Paso 6 — Crear el schema SQL

Crear el archivo en:

```
shared/src/commonMain/sqldelight/com/tuusuario/dualpersistence/db/Item.sq
```

Contenido:

```sql
CREATE TABLE Item (
    id TEXT NOT NULL PRIMARY KEY,
    title TEXT NOT NULL,
    description TEXT NOT NULL,
    createdAt INTEGER NOT NULL
);

getAllItems:
SELECT * FROM Item;

insertItem:
INSERT OR REPLACE INTO Item(id, title, description, createdAt)
VALUES (?, ?, ?, ?);

updateItem:
UPDATE Item SET title = ?, description = ? WHERE id = ?;

deleteItem:
DELETE FROM Item WHERE id = ?;
```

---

## Paso 7 — Sincronizar y generar código

1. Clic en **"Sync Now"** en Android Studio
2. Ejecutar en terminal:

```bash
./gradlew generateSqlDelightInterface
```

3. Verificar que no haya errores en el panel de Gradle antes de continuar.

---

## Paso 8 — Prompt inicial para Copilot

Con el proyecto limpio y sincronizado, usar este prompt:

> "Basándote en el archivo `actividad.md`, implementa la arquitectura completa en este orden:
> 1. Modelo `Item` con `@Serializable`
> 2. Interfaz `ItemRepository` con `Flow`
> 3. `SQLiteItemRepository` usando SQLDelight
> 4. `NoSQLItemRepository` usando KStore
> 5. `StorageToggleManager` con `MutableStateFlow<StorageMode>`
> 6. `DualRepositoryProvider` con `flatMapLatest`
> 7. `ItemViewModel`
> 8. UI con Switch en AppBar e indicador de origen activo"
