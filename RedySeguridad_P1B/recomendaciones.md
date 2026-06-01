# Recomendaciones de Desarrollo
**Proyecto: Red y Seguridad — Kotlin Multiplatform**

---

## Fase 1: Estructura del Proyecto KMP

Antes de escribir lógica, configurar el proyecto con la siguiente estructura de sourceSets en `build.gradle.kts`:

```
:app (Android)
  └── src/
      ├── commonMain/kotlin/     ← ViewModels, interfaces, modelos
      ├── androidMain/kotlin/    ← implementaciones nativas
      └── androidUnitTest/
```

Dependencias clave a declarar desde el inicio:

```kotlin
// commonMain
implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core")
implementation("io.ktor:ktor-client-core")
implementation("io.ktor:ktor-client-content-negotiation")
implementation("io.ktor:ktor-serialization-kotlinx-json")

// androidMain
implementation("io.ktor:ktor-client-android")
implementation("androidx.security:security-crypto")        // EncryptedSharedPreferences
implementation("androidx.datastore:datastore-preferences") // DataStore
```

---

## Fase 2: Modelos de Datos (commonMain)

Data classes compartidas entre capas, definidas en `commonMain`:

```kotlin
// Módulo 1 — REST
@Serializable
data class Post(
    val id: Int,
    val userId: Int,
    val title: String,
    val body: String
)

// Módulo 3 — Storage
enum class StorageType {
    SHARED_PREFERENCES,
    DATA_STORE,
    ENCRYPTED_SHARED_PREFERENCES
}

data class SecretEntry(
    val key: String,
    val value: String,
    val storage: StorageType
)
```

---

## Fase 3: Estados de UI (UiState)

Cada pantalla tiene un `sealed class` que modela todos sus posibles estados. Este diseño es lo que sustenta el criterio de **Gestión de Estado (20%)** de la rúbrica.

```kotlin
// PostUiState — Módulo 1
sealed class PostUiState {
    object Idle : PostUiState()
    object Loading : PostUiState()         // inputs deshabilitados
    data class Success(val post: Post) : PostUiState()
    object UpdateSuccess : PostUiState()   // respuesta 200 OK capturada
    data class Error(val message: String) : PostUiState()
}

// SecretUiState — Módulo 3
sealed class SecretUiState {
    object Idle : SecretUiState()
    object Saving : SecretUiState()
    object Saved : SecretUiState()
    data class Retrieved(val value: String) : SecretUiState()
    object NotFound : SecretUiState()      // mensaje genérico, sin revelar existencia
    data class Error(val message: String) : SecretUiState()
}
```

---

## Fase 4: Interfaces Repository (expect/actual)

En `commonMain` se definen los contratos. En `androidMain` se proveen las implementaciones nativas.

```kotlin
// commonMain — contratos
interface PostRepository {
    suspend fun getPost(id: Int): Result<Post>
    suspend fun updatePost(id: Int, post: Post): Result<Unit>
}

interface SecretRepository {
    suspend fun saveSecret(entry: SecretEntry): Result<Unit>
    suspend fun getSecret(key: String, storage: StorageType): Result<String?>
}
```

El uso de `Result<T>` de Kotlin estándar permite manejar éxito y error sin lanzar excepciones en los ViewModels.

---

## Fase 5: ViewModels (commonMain)

Los ViewModels viven en `commonMain` usando `ViewModel` de `lifecycle-viewmodel` (soporte KMP desde la versión 2.8+). La regla de la rúbrica es que los widgets se deshabiliten cuando `uiState.value is PostUiState.Loading` — esa lógica vive aquí, no en la UI.

```kotlin
class PostViewModel(private val repo: PostRepository) : ViewModel() {
    var postId by mutableStateOf("")
    var editTitle by mutableStateOf("")
    var editBody by mutableStateOf("")
    val uiState = MutableStateFlow<PostUiState>(PostUiState.Idle)

    fun fetchPost() { /* lanza coroutine, cambia a Loading, llama repo.getPost() */ }
    fun updatePost() { /* lanza PUT, espera 200 OK, cambia a UpdateSuccess */ }
}

class SecretViewModel(private val repo: SecretRepository) : ViewModel() {
    var key by mutableStateOf("")
    var value by mutableStateOf("")
    var selectedStorage by mutableStateOf(StorageType.SHARED_PREFERENCES)
    val uiState = MutableStateFlow<SecretUiState>(SecretUiState.Idle)

    fun saveSecret() { /* llama repo.saveSecret() */ }
    fun retrieveSecret() { /* llama repo.getSecret(), emite Retrieved o NotFound */ }
}
```

---

## Fase 6: Wireframes de las Pantallas

**Pantalla POST — Módulo 1:**

```
┌─────────────────────────────┐
│  Post ID:  [   42   ] [GET] │  ← input numérico + botón (deshabilitado en loading)
├─────────────────────────────┤
│  Title: [_______________]   │  ← editable, vacío hasta recibir respuesta
│  Body:  [_______________]   │
│         [_______________]   │
│                    [PUT]    │  ← activo solo cuando hay un post cargado
├─────────────────────────────┤
│  Estado: ✓ Actualizado (200)│  ← banner de confirmación / error
└─────────────────────────────┘
```

**Pantalla Secretos — Módulo 3:**

```
┌─────────────────────────────┐
│  Clave:  [_____________]    │
│  Valor:  [_____________]    │
│  Mecanismo: ○ SP  ○ DS  ○ ESP│  ← RadioGroup / SegmentedButton
│                  [GUARDAR]  │
├─────────────────────────────┤
│  Clave:  [_____________]    │
│  Mecanismo: ○ SP  ○ DS  ○ ESP│
│                 [RECUPERAR] │
├─────────────────────────────┤
│  Resultado: [valor_aquí]    │  ← o mensaje genérico si no existe
└─────────────────────────────┘
```

---

## Fase 7: Orden de Implementación

Seguir este orden reduce el riesgo de bloqueos:

1. Configurar Gradle y dependencias
2. Definir modelos y enums en `commonMain`
3. Definir sealed classes de UiState en `commonMain`
4. Definir interfaces Repository en `commonMain`
5. Implementar `PostRepositoryImpl` con Ktor en `androidMain`
6. Implementar `SecretRepositoryImpl` con los tres mecanismos en `androidMain`
7. Escribir los ViewModels en `commonMain`
8. Construir las pantallas con Jetpack Compose en `androidMain`
9. Conectar navegación entre pantallas

---

## Fase 8: Tabla de Correspondencia Técnica (Sustentación — 20%)

Material de apoyo para defender el proyecto. La rúbrica pide la explicación técnica de la correspondencia entre el framework móvil y las capacidades nativas de Android:

| Concepto KMP / Kotlin | Capacidad Android subyacente |
|---|---|
| `Ktor HttpClient(Android)` | `HttpURLConnection` / OkHttp engine |
| `suspend fun` en Coroutines | Thread pools del sistema operativo |
| `StateFlow` en ViewModel | `LiveData` + patrón Observer |
| `EncryptedSharedPreferences` | Android Keystore System (respaldado por hardware) |
| `DataStore` + `Flow` | I/O asíncrono de archivos con esquema Preferences |
| `expect/actual` | Compilación condicional por plataforma en Gradle |
