# RedySeguridad P1B

Proyecto Kotlin Multiplatform con enfoque Android. Incluye pantallas para consumo de una API de ejemplo y almacenamiento seguro/local de datos.

## Funcionalidades

- Pantalla POST: consulta GET y actualizacion PUT contra JSONPlaceholder.
- Pantalla Secretos: guarda y recupera valores en SharedPreferences, DataStore y EncryptedSharedPreferences.

## Estructura del proyecto

- `androidApp/`: aplicacion Android (UI, manifest, recursos).
- `shared/`: modulo compartido.
  - `shared/src/commonMain/kotlin`: codigo comun para todas las plataformas.
  - Otros folders (por plataforma) contienen implementaciones especificas cuando aplica.

## Requisitos

- Android Studio (o IntelliJ con plugin Kotlin Multiplatform).
- JDK 17.
- Android SDK configurado (ver `local.properties`).

## Ejecucion

Usa las configuraciones de ejecucion del IDE o compila el APK con Gradle:

```bash
./gradlew :androidApp:assembleDebug
```

## Pruebas

```bash
./gradlew :shared:testAndroidHostTest
./gradlew :shared:commonTest
```

## Notas

- JSONPlaceholder es una API de prueba; las operaciones de actualizacion simulan persistencia.
