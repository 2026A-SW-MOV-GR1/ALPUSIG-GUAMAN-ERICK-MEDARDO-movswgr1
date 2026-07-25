# Plan de Restauración — Rediseño Amazon Logistics

Se ha detectado que los archivos del sistema de diseño y componentes personalizados fueron eliminados o revertidos. Este plan restaurará la interfaz de **Amazon Logistics** (App 1: Admisión) basada en las guías anteriores.

## Propósito
Restaurar la coherencia visual del sistema Amazon (colores oscuros, acento naranja, tipografía densa) en todas las pantallas.

## Pasos de Implementación

### 1. Recreación del Sistema de Diseño (Theme)
Se volverán a crear los archivos base en la carpeta `ui/theme/`:
- `[NEW]` `Color.kt`: Paleta oficial (`AmazonDark`, `AmazonOrange`, `SurfaceLight`, etc.).
- `[NEW]` `Type.kt`: Tipografía operativa (8sp a 13sp).
- `[NEW]` `Theme.kt`: Composable `AmazonTheme` para aplicar el esquema de colores.

### 2. Recreación de Componentes Amazon
Se volverá a crear `ui/components/AmazonComponents.kt` con:
- `AmazonTopBar` (con ícono 'A').
- `AmazonStepBar` (barra de progreso de 3 pasos).
- `AmazonTextField` y `AmazonField`.
- `AmazonCoordinateCard` (borde naranja).
- `AmazonPrimaryButton` y `AmazonMapButton` (con iconos dibujados en Canvas).
- `AmazonMapPanel` (panel inferior del mapa).

### 3. Actualización de Pantallas
- `[MODIFY]` `MainActivity.kt`: Envolver con `AmazonTheme`.
- `[MODIFY]` `FormScreen.kt`: Aplicar los nuevos componentes y estructura.
- `[MODIFY]` `MapScreen.kt`: Aplicar TopBar, StepBar y el nuevo panel inferior.

## Verificación
- Se verificará la compilación exitosa.
- Se confirmará visualmente que el diseño coincide con el prototipo de Amazon (fondo oscuro en TopBar, botones naranjas, tipografía pequeña).

---
> [!IMPORTANT]
> Se asegurará que los nombres de los colores y estilos coincidan exactamente para evitar errores de referencia "Unresolved reference".
