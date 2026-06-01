# Proyecto: Red y Seguridad
**FIS - Programación de Aplicaciones Móviles | Escuela Politécnica Nacional**

---

## Objetivo del Proyecto

Los sistemas modernos operan conectados. En este proyecto grupal, expandirán la frontera de su software conectando la interfaz a servidores externos (REST) y asegurando llaves privadas en los compartimentos nativos del sistema operativo Android.

---

## Módulo 1: Conectividad REST

### Interacción Externa de Datos

Diseñarán una pantalla independiente en su aplicación para interactuar de forma reactiva con el Fake API de pruebas de **JSONPlaceholder**.

- **Peticiones Asíncronas:** Implementación de consultas directas usando HTTP Client nativo o librerías de red del framework móvil.
- **Manejo de Estados:** Deshabilitar campos de texto y botones mientras la petición esté en tránsito (loading states) para optimizar la UX.

### Flujo de Consulta y Actualización

**Consulta (GET)**

El usuario consulta posts del servidor por identificador único.

- **Entrada Numérica:** Input de texto que restringe el ID solicitado.
- **Disparador GET:** Envío de petición a `/posts/{id}` para pintar el contenido editable.

**Actualización (PUT)**

Modificación local y validación de respuesta de red simulada.

- **Disparador PUT:** Envío del JSON modificado de vuelta al recurso `/posts/{id}`.
- **Confirmación:** Capturar código `200 OK` para actualizar el estado visual de la pantalla.

---

## Módulo 3: Almacenamiento Seguro

El almacenamiento local no encriptado expone datos confidenciales ante la extracción física o vulnerabilidades de red. Implementarán almacenamiento seguro bajo el principio de conocimiento previo de llave.

### Mecanismos de Persistencia a Evaluar

| Mecanismo | Nivel de Encriptación | Propósito académico / Caso de uso | Estructura de acceso |
|---|---|---|---|
| SharedPreferences | Ninguna (Texto Plano) | Preferencias sencillas y estados de UI rápidos en memoria | Síncrona / XML directo |
| DataStore (Preferences) | Ninguna (Texto Plano) | Migración moderna que evita bloqueos del hilo principal de UI | Reactiva / Kotlin Flow / Streams |
| EncryptedSharedPreferences | AES-256 SIV & AES-128 GCM | Fichas confidenciales de identidad, Tokens JWT y credenciales | Cifrado automático sobre disco |

### Pantalla de Gestión de Secretos

Para garantizar el aprendizaje del almacenamiento interno de Android, la UI funcionará de forma transaccional directa sin listar claves:

- **Acción Guardar:** Se ingresa una `Llave`, un `Valor` y se elige el compartimento nativo mediante un selector gráfico para persistir el dato.
- **Acción Recuperar:** El usuario ingresa la `Llave` y el selector donde asume que está el secreto. Si existe, lo revela; de lo contrario, notifica su inexistencia de forma genérica.

---

## Rúbrica de Evaluación

**Peso total del proyecto: 60% — Red & Persistencia Segura**

| Criterio | Peso | Descripción |
|---|---|---|
| Módulo 1 (REST API) | 30% | Petición GET/PUT con JSONPlaceholder controlando los estados de carga en los widgets |
| Módulo 3 (Seguridad) | 30% | Integración correcta de SharedPreferences, DataStore y EncryptedSharedPreferences |
| Gestión de Estado | 20% | Reactividad de datos instantánea sin caída de procesos |
| Sustentación de la Solución | 20% | Explicación técnica de la correspondencia entre el framework móvil y las capacidades de Android |
