# Fase C: Crítica y Propuesta de Mejora - Discord UX

## 1. Análisis Crítico de Discord

### Falla de UX Identificada: Falta de Indicador Visual Persistente de "Canales No Leídos"

#### Descripción del Problema

En Discord, cuando hay mensajes nuevos sin leer en un canal, el indicador de notificación (badge de número) aparece en la lista de canales. **Sin embargo, este badge desaparece después de un tiempo o en ciertos estados de navegación**, generando confusión:

1. **Usuario navega al servidor A** → Ve que el canal #general tiene 5 mensajes sin leer (badge mostrando "5")
2. **Usuario navega al servidor B** → Cuando vuelve al servidor A, **el badge puede no ser visible si no scrollea** porque está contraído en la categoría
3. **Usuario tiene 10+ canales sin leer** → No hay forma de ver un resumen global de qué canales necesitan atención

#### Impacto en UX
- **Pérdida de contexto:** El usuario no sabe qué canales tienen contenido pendiente
- **Ansiedad de FOMO:** Especialmente en comunidades grandes (30+ canales), perder mensajes importantes es crítico
- **Complejidad de navegación:** En servidores con muchas categorías colapsadas, los badges "ocultos" generan frustración

#### Ejemplos de Casos de Uso Afectados
- Gaming: Perder invitaciones de equipo (mensajes urgentes)
- Educación: No notar que un profesor ha compartido material importante
- Trabajo remoto: Perder anuncios o actualizaciones de proyecto

---

## 2. Propuesta de Mejora: Sistema de Indicadores Persistentes + Badge Global

### Solución Propuesta

Implementar un **sistema multinivel de indicadores de canales no leídos**:

1. **Dot rojo persistente** en canales con mensajes sin leer (incluso colapsados)
2. **Contador global** en el header del servidor mostrando total de notificaciones
3. **"Unread Separator"** en el chat: línea roja que separa mensajes leídos de nuevos
4. **Animación de entrada suave** para nuevos mensajes (pulse de 200ms)

### Sustento Técnico

#### Por qué esta mejora es adecuada:

1. **Principio de Visibilidad del Estado del Sistema** (Nielsen Heuristics)
   - Usuario siempre sabe si hay contenido pendiente
   - Cumple con el "O" de CRUD: Observability

2. **Reducción de Carga Cognitiva**
   - No requiere acción adicional del usuario (no es un toggle)
   - La información está "ahí" sin distraer

3. **Consistencia con Estándares de Industria**
   - Slack: Muestra "*" en canales no leídos y cuenta global
   - Teams: Muestra "●" (dot) en chats con mensajes nuevos
   - Telegram: Muestra números en chats + contador en app

4. **Performance en KMP/Compose**
   - Fácil de implementar con recomposición selectiva (el dot es un `Surface` pequeño, < 1KB)
   - `key` estable por canal = eficiencia garantizada
   - Sin impacto en scroll (LazyColumn + memoization)

---

## 3. Implementación de la Mejora

### Cambios en Modelos de Datos

Se añade un campo `tieneNoLeidos` al `Canal`:

```kotlin
@Serializable
data class Canal(
    val id: String,
    val nombre: String,
    val tipo: TipoCanal = TipoCanal.TEXTO,
    val esPrivado: Boolean = false,
    val tieneNotificaciones: Boolean = false,
    val conteoNotificaciones: Int = 0,
    val categoriaPadreId: String? = null,
    val tieneNoLeidos: Boolean = false,  // ← NUEVO
    val tiempoUltimoMensaje: Long = 0    // ← NUEVO (para unread separator)
)
```

### Cambios en Componentes UI

#### 1. CanalItem mejorado (con dot rojo de no leídos)

```kotlin
@Composable
fun CanalItem(
    nombre: String,
    tipo: Char,
    isPrivado: Boolean,
    tieneNotificaciones: Boolean,
    conteoNotificaciones: Int,
    tieneNoLeidos: Boolean,  // ← NUEVO
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .height(32.dp)
            .clickable(onClick = onClick),
        color = Color.Transparent
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            // Dot de no leídos (solo si hay)
            if (tieneNoLeidos) {
                Surface(
                    modifier = Modifier.size(6.dp),
                    shape = CircleShape,
                    color = DiscordColors.Error
                ) {}
            } else {
                Spacer(modifier = Modifier.width(6.dp))
            }
            
            Text(text = tipo.toString(), fontSize = 14.sp, color = DiscordColors.TextSecondary)
            
            Text(
                text = nombre,
                fontSize = 14.sp,
                color = DiscordColors.TextPrimary,
                modifier = Modifier.weight(1f),
                fontWeight = if (tieneNoLeidos) FontWeight.SemiBold else FontWeight.Normal
            )
            
            if (tieneNotificaciones) {
                NotificationBadge(cantidad = conteoNotificaciones)
            }
        }
    }
}
```

#### 2. Contador Global en Header del Servidor

En `ChannelListScreen`, se añade un badge global:

```kotlin
// En el header del servidor
Row(
    modifier = Modifier
        .fillMaxWidth()
        .padding(12.dp),
    horizontalArrangement = Arrangement.SpaceBetween,
    verticalAlignment = Alignment.CenterVertically
) {
    Text(
        text = "Gaming Legends",
        fontSize = 16.sp,
        color = DiscordColors.TextPrimary
    )
    
    val countNoLeidos = categorias
        .flatMap { it.canales }
        .count { it.tieneNoLeidos }
    
    if (countNoLeidos > 0) {
        Surface(
            modifier = Modifier.size(20.dp),
            shape = CircleShape,
            color = DiscordColors.Error
        ) {
            Box(contentAlignment = Alignment.Center) {
                Text(
                    text = if (countNoLeidos > 9) "9+" else countNoLeidos.toString(),
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }
    }
}
```

#### 3. Unread Separator en ChatScreen

```kotlin
// Entre grupos de mensajes
if (grupo.tiempoUltimoMensaje > ultimoTiempoLeido) {
    Divider(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        color = DiscordColors.Error,
        thickness = 2.dp
    )
    Text(
        text = "mensajes nuevos",
        fontSize = 11.sp,
        color = DiscordColors.Error,
        modifier = Modifier
            .align(Alignment.CenterHorizontally)
            .padding(vertical = 8.dp)
    )
}
```

### Cambios en MockDataProvider

```kotlin
fun getMockCategorias(): List<Categoria> = listOf(
    Categoria(
        id = "cat2",
        nombre = "CANALES DE TEXTO",
        isExpanded = true,
        canales = listOf(
            Canal("ch4", "general", TipoCanal.TEXTO, tieneNoLeidos = false),
            Canal("ch6", "memes", TipoCanal.TEXTO, tieneNotificaciones = true, conteoNotificaciones = 15, tieneNoLeidos = true),  // ← CON DOT
            Canal("ch7", "ayuda", TipoCanal.TEXTO, tieneNoLeidos = true)
        )
    ),
    // ...
)
```

---

## 4. Beneficios de la Mejora

| Aspecto | Impacto |
|---|---|
| **Visibilidad** | ✅ Usuario siempre ve qué canales tienen contenido pendiente |
| **Claridad** | ✅ Dot rojo + contador global eliminan ambigüedad |
| **Performance** | ✅ Recomposición mínima (dot es Surface de 6dp) |
| **Accesibilidad** | ✅ Color rojo (#F23F42) tiene buen contraste en fondo oscuro (#2B2D31) |
| **Compatibilidad** | ✅ No requiere cambios arquitectónicos, es aditivo |

---

## 5. Conclusión

Discord es una app excelente, pero este indicador de no leídos persistente resolvería un pain point real para usuarios de comunidades grandes y servidores con muchos canales. La implementación en Compose Multiplatform es trivial (~50 líneas), y el impacto en UX es significativo.

La mejora sigue principios de Nielsen (Visibilidad) + estándares de industria (Slack, Teams), garantizando que sea intuitiva e inmediata para los usuarios.
