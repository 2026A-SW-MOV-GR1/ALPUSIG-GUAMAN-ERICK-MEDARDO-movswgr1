# Fase A: Análisis de Producto y UX - Discord

## 1. Reserva de App

**Aplicación seleccionada:** Discord  
**Versión analizada:** Discord Desktop/Mobile (2025-2026)  
**Justificación:** Discord es una plataforma de comunicación compleja con múltiples listas anidadas, micro-interacciones y un sistema de navegación jerárquico ideal para demostrar la replicación de UI con KMP/Compose Multiplatform.

---

## 2. Definición del Mercado Objetivo

### Demografía Primaria
- **Edad:** 13-35 años (núcleo), con expansión a 35-55 años
- **Nivel socioeconómico:** Clase media a media-alta; usuarios con acceso a internet de banda ancha
- **Intereses principales:**
  - Gaming (origen histórico, aún 40-50% del uso)
  - Educación y estudio colaborativo (universidades, bootcamps)
  - Comunidades de arte y diseño (ilustradores, diseñadores, streamers)
  - Crypto y blockchain (comunidades de proyectos)
  - Trabajo remoto (equipos distribuidos, startups)
  - Hobbies y foros temáticos (anime, libros, tecnología)

### Evolución del Mercado
Discord **nació en 2015** como plataforma para gamers buscando alternativa a Skype. Desde entonces ha diversificado:
- **2015-2018:** Dominó el mercado gamer; compitió con TeamSpeak y Curse Voice
- **2020-2021:** Pivote hacia educación (pandemia COVID-19 aceleró adopción académica)
- **2022-2024:** Consolidación en trabajo remoto, comunidades creativas y descentralizadas

### Comportamiento del Usuario
- **Sesiones típicas:** 2-8 horas/día (gamers), 30min-2 horas/día (educación/trabajo)
- **Dispositivos:** Desktop (principal), Mobile (acceso secundario/notificaciones)
- **Expectativas:** Latencia baja (<100ms), audio de calidad, interfaz predecible

---

## 3. Psicología del Color - Paleta de Discord

### Justificación Estratégica de la Paleta

#### **Color Primario: Blurple (#5865F2)**
- **Tonalidad:** Azul púrpura, saturado
- **Psicología:** 
  - Confianza y estabilidad (azul)
  - Creatividad e imaginación (púrpura)
  - Comunidad y pertenencia
- **Uso en Discord:**
  - Botones principales (crear servidor, enviar mensaje)
  - Links activos
  - Estados activos (servidor seleccionado)
  - Badges de verificación
- **Efecto psicológico:** Comunica "es seguro conectarse, es un espacio para crear"

#### **Fondos Oscuros (Escala de Grises)**
- **Rail de servidores:** `#1E1F22` (más oscuro)
- **Panel de canales:** `#2B2D31` (medio)
- **Panel de chat:** `#313338` (medio-claro)
- **Elementos elevados:** `#383A40` (más claro)
- **Texto primario:** `#F2F3F5` (casi blanco)
- **Texto secundario:** `#B5BAC1` (gris claro)

**Psicología:**
- **Contraste jerárquico:** Los paneles más oscuros se perciben como "navegación", los claros como "contenido"
- **Fatiga ocular reducida:** Temas oscuros son más cómodos para sesiones largas (12+ horas/día en gaming)
- **Ahorro de batería:** En dispositivos OLED (móviles modernos), fondos oscuros = < consumo de energía
- **Percepción de profundidad:** Gradual de oscuro a claro crea ilusión 3D, mejora UX

#### **Estados de Usuario (Indicadores)**
- **En línea (#23A55A - Verde):** Energía, disponibilidad activa
- **Ausente (#F0B232 - Amarillo):** Advertencia, no disponible inmediatamente
- **No Molestar (#F23F42 - Rojo):** Urgencia, no interrumpir
- **Desconectado (#80848E - Gris):** Inactividad, neutral

**Consistencia:** Estos colores de estado son estándares en la industria (Slack, Teams, etc.), facilitando reconocimiento inmediato.

### Jerarquía Visual Completa

```
Nivel 1 - Acción:    Blurple (#5865F2) → Botones principales, CTAs
Nivel 2 - Estructura: Gradiente oscuro (#1E1F22 → #313338) → Paneles, navegación
Nivel 3 - Contenido:  Texto primario (#F2F3F5) sobre fondos → Mensajes
Nivel 4 - Secundaria: Texto muted (#B5BAC1) → Timestamps, hints
Nivel 5 - Feedback:   Estados (#23A55A, #F0B232, #F23F42, #80848E) → Presencia
```

---

## 4. Auditoría de Componentes - Listas a Replicar

### Lista 1: Rail de Servidores (LazyRow/LazyColumn)
**Ubicación:** Barra izquierda, vertical narrow  
**Contenido:** Íconos circulares (~48dp) con avatares de servidores  
**Interacción:**
- Click: Selecciona servidor, navega a su lista de canales
- Hover: Transición de círculo → rounded square (radio 16dp)
- Indicador: Barra vertical verde/blurple a la izquierda del activo
- Animación: Micro-transición de 200ms en forma

**Desafío de rendering:** 30-50 servidores; necesita LazyRow eficiente + recomposición mínima

### Lista 2: Canales Agrupados (LazyColumn + Headers Colapsables)
**Ubicación:** Panel izquierdo después del rail  
**Contenido:**
- Headers de categoría (ej: "CANALES DE VOZ", "CANALES DE TEXTO")
- Items de canal con ícono (# para texto, speaker para voz, candado para privado)
- Unread badges (rojo #F23F42) cuando hay mensajes nuevos
- Contador de notificaciones en números

**Estructura de datos:**
```kotlin
data class Categoria(
  id: String,
  nombre: String,
  isExpanded: Boolean,
  canales: List<Canal>
)

data class Canal(
  id: String,
  nombre: String,
  tipo: TipoCanal, // TEXTO, VOZ, FORUM
  esPrivado: Boolean,
  tieneNotificaciones: Boolean,
  conteoNotificaciones: Int
)
```

**Desafío:** Hasta 100+ canales; colapsables; estado persistente; LazyColumn anidado

### Lista 3: Mensajes de Chat (LazyColumn Invertida + Agrupación)
**Ubicación:** Panel central principal  
**Contenido:**
- Mensajes agrupados por autor (solo muestra avatar/nombre en el primero)
- Timestamps (relative time, ej "2 days ago")
- Avatares circulares (~32dp) con indicador de estado
- Reacciones emoji, respuestas, menciones
- Agrupación: Mensajes del mismo autor con < 5 min diferencia = grupo único

**Estructura de datos:**
```kotlin
data class Mensaje(
  id: String,
  autorId: String,
  autorNombre: String,
  avatarUrl: String,
  contenido: String,
  timestamp: LocalDateTime,
  reacciones: List<Reaccion>,
  esRespuesta: Boolean,
  mensajeReferenciado: Mensaje?
)

data class Grupo de Mensajes(
  autorId: String,
  autorNombre: String,
  avatarUrl: String,
  estado: EstadoUsuario,
  mensajes: List<Mensaje>
)
```

**Desafío:** 500-1000+ mensajes en servidores activos; scroll invertido; agrupación dinámica; LazyColumn con key={mensaje.id}

### Lista 4: Miembros Conectados (LazyColumn con Headers de Estado)
**Ubicación:** Panel derecho (opcional en versión mobile, visible en desktop)  
**Contenido:**
- Headers: "En línea (23)", "Ausente (5)", "No Molestar (3)", "Desconectado (12)"
- Items: Avatar + nombre + estado + juego/actividad
- Hover: Opciones (mensaje directo, perfil, invitar)
- Colores dinámicos según estado

**Estructura de datos:**
```kotlin
data class Miembro(
  id: String,
  nombre: String,
  avatarUrl: String,
  estado: EstadoUsuario, // ONLINE, IDLE, DND, OFFLINE
  actividad: String?, // "Playing Valorant", "Streaming", null
  rolColor: Color?
)

enum class EstadoUsuario {
  ONLINE("#23A55A"), IDLE("#F0B232"), DND("#F23F42"), OFFLINE("#80848E")
}
```

**Desafío:** Dinámico, se actualiza en tiempo real; agrupación por estado; hasta 50-100 miembros

---

## 5. Tipografía

### Decisión: Roboto en lugar de "gg sans"
Discord usa su propia tipografía "gg sans" (antes "Whitney") de licencia propietaria. **No podemos replicarla literalmente en un proyecto open-source.**

**Alternativa elegida: Roboto**
- **Motivo:** Métricas similares, excelente legibilidad, open-source (Apache 2.0), amplio soporte en KMP/Compose
- **Weights utilizados:**
  - Regular (400): Texto de mensaje, timestamps
  - Medium (500): Nombres de canal, timestamps en encabezados
  - Bold (700): Nombres de usuarios, títulos de categoría

### Escala Tipográfica
```
Títulos de categoría:  14sp, Bold, uppercase, tracking +0.5
Nombres de usuario:    14sp, Medium, #F2F3F5
Contenido de mensaje:  14sp, Regular, #F2F3F5
Timestamps:            12sp, Regular, #B5BAC1
Hint/Placeholder:      14sp, Regular, #B5BAC1, italic
Badges (notificaciones):  10sp, Bold, #FFFFFF
```

---

## 6. Conclusiones del Análisis

### Síntesis de Hallazgos
1. **Público:** Altamente diverso (gamers, estudiantes, profesionales). La paleta oscura es estratégica para sesiones largas.
2. **Colores:** Blurple + escala gris comunica profesionalismo + comunidad. Indicadores de estado = estándar industria.
3. **Componentes:** 4 listas complejas con desafíos de performance (scroll fluido, keys estables, agrupación dinámica).
4. **Tipografía:** Roboto es un substitute viable, manteniendo métricas Discord.

### Implicaciones para la Implementación en KMP
- **Performance critical:** `LazyColumn`/`LazyRow` con keys obligatorias
- **Fidelidad visual:** Exacta replicación de espaciado (padding 8dp, gaps 4dp) y radios de borde (8dp, 16dp)
- **Animaciones:** Transiciones de 200-300ms, curves Material estándar
- **State management:** Cambios dinámicos de estado de usuario, categorías expandidas, etc.
