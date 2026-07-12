# Discord Clone - Kotlin Multiplatform (KMP)

Este proyecto es una réplica de alta fidelidad de la interfaz de **Discord Mobile**, desarrollada como parte del taller "Native UI Re-Engineering & UX Analysis". El objetivo principal es demostrar el uso de **Kotlin Multiplatform (KMP)** y **Compose Multiplatform** para crear interfaces nativas complejas, fluidas y dinámicas.

## 🚀 Tecnologías Utilizadas

- **Kotlin Multiplatform (KMP)**: Compartición de lógica de negocio y modelos entre plataformas.
- **Compose Multiplatform**: Framework declarativo para la construcción de la UI nativa (renderizado mediante Skia).
- **Material 3**: Base de componentes de diseño.
- **Kotlinx Serialization**: Para la gestión de modelos de datos.

## 🛠️ Características Implementadas

### 1. Experiencia de Usuario (UX) y Navegación
- **Splash Screen**: Pantalla de inicio con logo oficial y animación de carga fluida.
- **Navegación Mobile-First**: Arquitectura de panel único que alterna entre la lista de canales y el chat, optimizando el espacio en dispositivos móviles.
- **Overlay de Miembros**: Panel lateral deslizable (slide-in) para ver los integrantes del servidor, replicando el comportamiento nativo de Discord.

### 2. Fidelidad Visual (UI)
- **Barra de Servidores Vertical**: Lista vertical con indicadores de selección animados y transiciones de forma (círculo a cuadrado redondeado).
- **Estructura de Canales Compleja**: Organización por categorías colapsables (`INICIO`, `ESCRIBAMOS`, `VOICE-CHATS`).
- **Diferenciación de Escenas por Canal**: 
  - **Chat de Texto**: Layout estándar con mensajes agrupados.
  - **Canal de Anuncios**: Modo de solo lectura con avisos visuales.
  - **Galería de Imágenes**: Grid de 2 columnas para canales visuales.
  - **Canal de Voz**: Escena dedicada con estado de conexión simulado y botón de unión.

### 3. Funcionalidad Dinámica
- **Chat Funcional**: Envío de mensajes en tiempo real (estado en memoria) con scroll automático al final.
- **Selector de Emojis**: Panel interactivo para insertar emojis en el chat.
- **Creación de Servidores**: Flujo completo mediante diálogo para añadir nuevos servidores con nombre e icono personalizados.
- **Ajustes de Perfil**: Pantalla dedicada para editar el nombre de usuario, mensaje de estado y estado de presencia (Online, Idle, DND, Offline).

## 🏗️ Arquitectura del Proyecto

El código sigue una estructura de **Clean Architecture** dentro del módulo `shared`:

- **`domain/models`**: Definición de las entidades de negocio (`Servidor`, `Canal`, `Mensaje`, `Miembro`).
- **`data`**: Proveedor de datos mock (`MockDataProvider`) y gestión del estado inicial.
- **`presentation/screens`**: Implementación de las pantallas principales y la lógica de navegación.
- **`presentation/components`**: Componentes de UI reutilizables y altamente personalizables.
- **`presentation/theme`**: Definición de la paleta de colores oficial de Discord, tipografía y formas.

## 📱 Cómo Ejecutar

### Android
Para compilar y ejecutar la aplicación en un emulador o dispositivo físico:
```bash
./gradlew :androidApp:assembleDebug
```

### Pruebas
Para ejecutar las pruebas unitarias en el host de Android:
```bash
./gradlew :shared:testAndroidHostTest
```

---
*Este proyecto es estrictamente educativo y no utiliza WebViews; todo el contenido es renderizado de forma nativa.*
