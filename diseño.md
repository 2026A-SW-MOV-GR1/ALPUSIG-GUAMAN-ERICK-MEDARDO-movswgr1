# Guía de Diseño — Ecosistema Amazon KMP
> Documento compartido para los 3 integrantes del equipo.
> Cada app es independiente pero debe verse como parte de un mismo sistema.
> Aplicar estas especificaciones tal cual para lograr coherencia visual entre
> App 1 (Admisión), App 2 (Distribución) y App 3 (Última Milla).

---

## 1. Identidad visual del sistema

El diseño está inspirado en la identidad de Amazon Logistics: oscuro,
profesional y con el naranja como único acento de color. No es una app
de consumidor final — es una herramienta operativa, por lo que la UI
debe sentirse funcional y densa, no decorativa.

---

## 2. Paleta de colores

Estos son los únicos colores a usar en toda la app. No agregar colores
adicionales sin consenso del equipo.

| Nombre        | Hex       | Uso principal |
|---|---|---|
| Amazon Dark   | `#131921` | TopBar, botones primarios, fondo de status bar |
| Amazon Navy   | `#1a1a2e` | Status bar, step bar, acentos oscuros |
| Amazon Orange | `#FF9900` | Acento principal: botones CTA, marcadores, íconos activos, bordes destacados |
| Surface Light | `#f4f4f4` | Fondo del body/contenido de pantallas |
| Surface White | `#ffffff` | Cards, campos de formulario, panel inferior del mapa |
| Border Light  | `#dddddd` | Bordes de campos y separadores |
| Text Primary  | `#1a1a2e` | Texto principal en campos y valores |
| Text Secondary| `#888888` | Labels de campos, subtítulos, coordenadas secundarias |
| Text Muted    | `#bbbbbb` | Placeholders |
| Success Green | `#4CAF50` | Paso completado en la barra de progreso, chip de ID activo |
| Cancel Gray   | `#f4f4f4` | Botón cancelar (fondo), con texto `#666666` |

---

## 3. Tipografía

Usar la fuente del sistema Android (Roboto por defecto en Compose).
No importar fuentes externas.

| Elemento | Tamaño | Peso | Color |
|---|---|---|---|
| Título TopBar | 13sp | Medium (500) | `#FF9900` |
| Subtítulo TopBar | 9sp | Normal (400) | `#aaaaaa` |
| Section title | 9sp | Medium (500) | `#555555` + uppercase + letter-spacing 0.5 |
| Label de campo | 8sp | Normal (400) | `#888888` |
| Valor de campo | 11sp | Medium (500) | `#1a1a2e` |
| Placeholder | 11sp | Normal (400) | `#bbbbbb` |
| Coordenadas | 9sp | Medium (500) | `#1a1a2e` |
| Subtext coordenadas | 7.5sp | Normal (400) | `#888888` |
| Botón primario | 11sp | Medium (500) | `#ffffff` |
| Botón cancelar | 10sp | Normal (400) | `#666666` |
| Step labels | 7.5sp | Normal (400) | `#aaaaaa` (inactivo) / `#FF9900` (activo) |
| Chip de ID | 8sp | Normal (400) | `#666666` |

---

## 4. Componentes comunes (replicar en las 3 apps)

### 4.1 Status Bar
- Fondo: `#1a1a2e`
- Texto de hora e íconos: blanco `#ffffff`
- Altura: estándar del sistema (no personalizar)

### 4.2 TopBar
- Fondo: `#131921`
- Padding horizontal: 16dp, vertical: 12dp
- Ícono de la app a la izquierda: cuadrado 22×22dp con `border-radius` 4dp
  y fondo `#FF9900`. Cada app pone su letra inicial en blanco dentro
  (A = Admisión, D = Distribución, U = Última Milla).
- Título a la derecha del ícono: color `#FF9900`, 13sp, weight 500.
  Cada app pone el nombre de su módulo.
- Subtítulo debajo del título: `"Sistema Logístico Amazon"`, color `#aaaaaa`, 9sp.

### 4.3 Barra de progreso de pasos
- Fondo: `#1a1a2e`
- Tres segmentos horizontales con gap de 4dp entre ellos, padding 8dp lateral.
- Cada segmento: altura 3dp, border-radius 2dp.
- Estado completado: `#4CAF50`
- Estado activo (paso actual): `#FF9900`
- Estado pendiente: `#333333`
- Debajo de los segmentos, tres labels de texto (7.5sp) alineados a cada
  segmento: el label del paso activo en `#FF9900`, los demás en `#aaaaaa`.
- Los 3 pasos son los mismos para las 3 apps (representan el flujo global):
  `"Admisión"` / `"Distribución"` / `"Última Milla"`.
  Cada app resalta el paso que le corresponde como activo y marca como
  completados los anteriores.

### 4.4 Campos de formulario
- Fondo: `#ffffff`
- Border: `0.5dp` solid `#dddddd`
- Border-radius: `8dp`
- Padding interno: `8dp` vertical, `10dp` horizontal
- Separación entre campos: `8dp`
- Estructura interna: label arriba (8sp, `#888888`), valor abajo (11sp, `#1a1a2e`).
- No usar TextField de Material con underline; usar un contenedor con bordes
  redondeados completos (outlined style).

### 4.5 Card de coordenadas
- Fondo: `#ffffff`
- Border: `0.5dp` solid `#FF9900` (el naranja distingue esta card del resto)
- Border-radius: `8dp`
- Padding: `8dp` vertical, `10dp` horizontal
- Contenido: punto naranja (8dp de diámetro, `#FF9900`) a la izquierda +
  texto de coordenadas a la derecha.
- Primera línea: lat, lng en formato 6 decimales, 9sp, `#1a1a2e`, weight 500.
- Segunda línea: descripción corta del estado del marcador, 7.5sp, `#888888`.

### 4.6 Botón de mapa (abrir mapa)
- Fondo: `#131921`
- Border-radius: `8dp`
- Padding: `9dp` vertical
- Ícono de pin a la izquierda (color `#FF9900`)
- Texto: describe la acción según el módulo (cada app adapta el texto),
  color `#FF9900`, 10sp, weight 500.
- Ancho: ocupar todo el ancho disponible.

### 4.7 Chip de ID de paquete
- Fondo: `#f0f0f0`
- Border-radius: `20dp` (pill)
- Padding: `4dp` vertical, `10dp` horizontal
- Punto verde (`#4CAF50`, 5dp diámetro) a la izquierda.
- Texto: ID del paquete (formato `AMZ-{timestamp}`), 8sp, `#666666`.
- Alineación: a la izquierda, no centrado.

### 4.8 Botón primario (acción principal)
- Fondo: `#FF9900`
- Border-radius: `10dp`
- Padding: `11dp` vertical
- Ícono a la izquierda (blanco, 13sp).
- Texto: acción principal del módulo, 11sp, `#ffffff`, weight 500.
- Ancho: todo el ancho disponible.
- Solo habilitado cuando todos los campos requeridos estén completos.
- Cuando está deshabilitado: opacidad 40%, no cambiar colores.

### 4.9 Botones del mapa (confirmar / cancelar)
- Contenedor: fondo blanco `#ffffff`, padding `8dp`, border-top `0.5dp` `#eeeeee`.
- Botón cancelar: fondo `#f4f4f4`, border-radius `8dp`, padding `8dp`,
  texto `"Cancelar"` 10sp `#666666`. Ocupa 1 fracción del ancho.
- Botón confirmar: fondo `#FF9900`, border-radius `8dp`, padding `8dp`,
  texto de confirmación 10sp `#ffffff` weight 500. Ocupa 2 fracciones del ancho.
- Los dos botones en una fila con gap `6dp`.

### 4.10 Separadores de sección
- Línea de `0.5dp` de alto, color `#dddddd`, margin vertical `10dp`.
- Después de cada separador, un `section-title` (ver tipografía).

---

## 5. Pantalla del mapa (común a las 3 apps)

Cada app tiene su propia lógica de mapa, pero la estructura visual de
la pantalla debe ser la misma:

- TopBar igual al del resto de la app (mismo componente).
- Barra de progreso igual (mismo componente).
- El `MapView` ocupa todo el espacio disponible entre la TopBar y el
  panel inferior. No agregar padding alrededor del mapa.
- Panel inferior (fuera del mapa, fondo blanco):
  - Fila de coordenadas: punto naranja + texto de lat/lng actuales.
  - Fila de botones: Cancelar + Confirmar (ver 4.9).
  - Padding total del panel: `8dp` vertical, `12dp` horizontal.
- El marcador sobre el mapa debe ser visible y con color `#FF9900` o
  un pin personalizado que use ese color. No usar el marcador azul
  por defecto de OSMDroid sin personalizar.

---

## 6. Espaciado general

| Elemento | Valor |
|---|---|
| Padding lateral del body | `12dp` |
| Padding vertical del body | `12dp` |
| Gap entre campos | `8dp` |
| Gap entre secciones | `10dp` (separador) |
| Gap entre botones en fila | `6dp` |
| Padding interno de cards | `8dp` vertical, `10dp` horizontal |

---

## 7. Lo que cada app adapta (diferencias permitidas)

La siguiente tabla resume qué puede variar entre apps sin romper la
coherencia visual del sistema:

| Elemento | App 1 Admisión | App 2 Distribución | App 3 Última Milla |
|---|---|---|---|
| Ícono en TopBar | Letra **A** | Letra **D** | Letra **U** |
| Título TopBar | `"Admisión"` | `"Distribución"` | `"Última Milla"` |
| Paso activo en barra | Paso 1 | Paso 2 | Paso 3 |
| Pasos completados | Ninguno | Paso 1 | Pasos 1 y 2 |
| Campos del formulario | Remitente, Destinatario, Dirección Origen | Los que correspondan a su módulo | Los que correspondan a su módulo |
| Texto del botón de mapa | `"Cambiar ubicación en mapa"` | Según su lógica | Según su lógica |
| Texto del botón primario | `"Registrar admisión"` | Según su acción | Según su acción |
| Lógica del mapa | Marcador manual | Múltiples marcadores de hubs | GPS + ruta al destino |

---

## 8. Lo que NO debe variar entre apps

- Paleta de colores (ningún color fuera de la sección 2).
- Estructura de TopBar, barra de progreso y panel inferior del mapa.
- Tipografía y tamaños de texto.
- Border-radius de cada componente.
- Fondo de pantalla (`#f4f4f4` en body, `#ffffff` en cards).
- El chip de ID del paquete siempre visible con el mismo formato `AMZ-*`.
- El botón primario siempre en `#FF9900` con texto blanco.

---

## 9. Checklist antes de entregar

- [ ] TopBar muestra la letra del módulo en naranja sobre fondo oscuro.
- [ ] Barra de progreso refleja correctamente el paso activo y los completados.
- [ ] Todos los campos usan el estilo outlined con border `#dddddd`.
- [ ] La card de coordenadas tiene borde naranja `#FF9900`.
- [ ] El botón primario está deshabilitado si faltan datos.
- [ ] El mapa ocupa todo el espacio disponible sin padding lateral.
- [ ] El marcador en el mapa usa color naranja o pin personalizado.
- [ ] El panel inferior del mapa muestra coordenadas + botones en la misma
      estructura (cancelar gris / confirmar naranja).
- [ ] El chip de ID del paquete es visible en la pantalla de confirmación.
- [ ] No hay colores fuera de la paleta definida en la sección 2.
