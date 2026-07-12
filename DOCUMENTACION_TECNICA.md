# Documentación Técnica: Contador Persistente en KMP

## 1. Solución Implementada

### Arquitectura de Persistencia
Se implementó una solución multiplataforma en KMP que maneja la persistencia del estado mediante:

1. **ViewModel Multiplataforma** (`CounterViewModel.kt`)
   - Extiende `androidx.lifecycle.ViewModel`
   - Mantiene el estado del contador en `mutableStateOf<Int>`
   - El ViewModel es automáticamente preservado por Android durante Configuration Changes

2. **UI con Compose** (`CounterScreen.kt`)
   - Utiliza `@Composable` con `viewModel()` que obtiene la instancia del ViewModel
   - La recomposición ocurre cuando el estado cambia
   - El ViewModel es inyectado automáticamente por Compose

3. **Lifecycle Observer** (`LifecycleObserver.kt`)
   - Intercepta todos los eventos del ciclo de vida
   - Proporciona logging detallado de cada fase

### ¿Por Qué No Se Pierde el Dato?

**En Android tradicional:**
- `onSaveInstanceState` requiere serializar datos manualmente
- `onRestoreInstanceState` requiere desserializar datos manualmente
- Es propenso a errores si no se implementa correctamente

**En KMP con ViewModel + Compose:**
- El `ViewModel` no se destruye durante rotation (Configuration Change)
- Android mantiene la instancia del ViewModel en memoria
- Solo se destruye cuando la Activity es verdaderamente destruida (usuario cierra app)
- El estado en `mutableStateOf` persiste automáticamente
- Compose automáticamente recompone la UI cuando el estado cambia

## 2. Mapeo de Eventos del Ciclo de Vida

### Primer Lanzamiento de la App
```
[LIFECYCLE] onCreate - Activity created
[LIFECYCLE] onStart - Activity is starting
[LIFECYCLE] onResume - Activity became visible and interactive
```

### Rotación de Pantalla (Configuration Change)
```
[LIFECYCLE] onPause - Activity is about to lose focus
[LIFECYCLE] onStop - Activity is no longer visible
[LIFECYCLE] onSaveInstanceState - Saving state before destruction
[LIFECYCLE] onDestroy - Activity is being destroyed (rotation or exit)
                       ⚠️ IMPORTANTE: ViewModel se mantiene en memoria
[LIFECYCLE] onCreate - Activity created (nueva instancia)
[LIFECYCLE] onStart - Activity is starting
[LIFECYCLE] onRestore - Restoring state after recreation
[LIFECYCLE] onResume - Activity became visible and interactive
```

**Nota Crítica:** Durante la rotación:
- El `ViewModel` permanece en la misma instancia en memoria
- La `Activity` es completamente destruida y recreada
- El `count` en el ViewModel NO se pierde porque el ViewModel no se destruye
- Compose automáticamente recompone la UI con el valor guardado

### Multitarea (Home → App)
```
[LIFECYCLE] onPause - Activity is about to lose focus
[LIFECYCLE] onStop - Activity is no longer visible
... (usuario hace otra cosa)
[LIFECYCLE] onRestart - Activity returning from background
[LIFECYCLE] onStart - Activity is starting
[LIFECYCLE] onResume - Activity became visible and interactive
```

### Cierre de la App
```
[LIFECYCLE] onPause
[LIFECYCLE] onStop
[LIFECYCLE] onDestroy
                    ⚠️ Aquí SÍ se destruye el ViewModel
```

## 3. Secuencia Detallada: Rotación de Pantalla

### ANTES (Sin implementación correcta)
```
Pantalla Vertical: count = 10
    ↓ [Usuario gira dispositivo]
    ↓ Android destruye Activity
    ↓ count se pierde porque no fue serializado
    ↓ Pantalla Horizontal: count = 0 ❌ PÉRDIDA DE DATOS
```

### AHORA (Con ViewModel)
```
Pantalla Vertical: count = 10 (almacenado en ViewModel)
    ↓ [Usuario gira dispositivo]
    ↓ onDestroy de Activity (pero ViewModel persiste)
    ↓ Activity es recreada
    ↓ Se obtiene la MISMA instancia del ViewModel
    ↓ Pantalla Horizontal: count = 10 ✅ DATOS PERSISTIDOS
```

## 4. Explicación Técnica: ¿Por Qué Funciona en KMP?

### Ventajas del Enfoque ViewModel + Compose

1. **Separación de Responsabilidades**
   - ViewModel maneja la lógica y estado
   - Activity maneja eventos del ciclo de vida
   - UI Compose maneja la presentación

2. **Retención Automática**
   - Android Framework automáticamente retiene el ViewModel
   - No requiere serialización manual con `Bundle`
   - Es más seguro y menos propenso a errores

3. **Composability**
   - `rememberSaveable` puede usarse para persistencia entre recomposiciones
   - `viewModel()` obtiene la instancia del ViewModel correctamente
   - Compose maneja la inyección de dependencias automáticamente

4. **Multiplataforma**
   - El patrón ViewModel funciona en KMP
   - En iOS también se pueden usar equivalentes (SwiftUI StateManagement)
   - La lógica de negocio está en `commonMain`

## 5. Logs Esperados en Consola

Ejecutar la app:
```
[LIFECYCLE] [1720419600000] onCreate - Activity created
[LIFECYCLE] [1720419600050] onStart - Activity is starting
[LIFECYCLE] [1720419600100] onResume - Activity became visible and interactive
```

Presionar el botón "Incrementar" 5 veces:
```
[LIFECYCLE] [1720419601000] COUNT_INCREMENTED: 1
[LIFECYCLE] [1720419601500] COUNT_INCREMENTED: 2
[LIFECYCLE] [1720419602000] COUNT_INCREMENTED: 3
[LIFECYCLE] [1720419602500] COUNT_INCREMENTED: 4
[LIFECYCLE] [1720419603000] COUNT_INCREMENTED: 5
```

Rotar el dispositivo (llegar a count=5):
```
[LIFECYCLE] [1720419604000] onPause - Activity is about to lose focus
[LIFECYCLE] [1720419604100] onStop - Activity is no longer visible
[LIFECYCLE] [1720419604150] onSaveInstanceState - Saving state before destruction
[LIFECYCLE] [1720419604200] onDestroy - Activity is being destroyed (rotation or exit)
[LIFECYCLE] [1720419604300] onCreate (MainActivity.onCreate called)
[LIFECYCLE] [1720419604350] onCreate - savedInstanceState restored (rotation detected)
[LIFECYCLE] [1720419604400] onStart - Activity is starting
[LIFECYCLE] [1720419604450] onRestore - Restoring state after recreation
[LIFECYCLE] [1720419604500] onResume - Activity became visible and interactive
      ⬆️ En este punto, el contador sigue siendo 5 ✅
```

## 6. Archivo de Configuración AndroidManifest.xml

Para que la rotación funcione correctamente, el manifest debe permitir rotaciones:
```xml
<activity
    android:name=".MainActivity"
    android:exported="true"
    android:configChanges="orientation|screenSize"
    />
```

Sin esta configuración, Android destruye completamente la actividad en lugar de solo recrearla.

## 7. Validación de la Solución

✅ **Requisito 1: Contador funciona**
- Botón "+" incrementa el valor
- Botón "-" decrementa el valor (mínimo 0)
- El valor se visualiza en pantalla

✅ **Requisito 2: Persistencia en rotación**
- El ViewModel se retiene automáticamente
- El estado de `count` no se pierde
- La UI se recompone con el valor correcto

✅ **Requisito 3: Logs de ciclo de vida**
- Todos los eventos (onCreate, onStart, onResume, onPause, onStop, onDestroy, onRestart) son loguados
- Los timestamps permiten ver el orden exacto de eventos
- Se diferencia claramente entre rotación y multitarea

## 8. Conclusión

La solución implementada en KMP utilizando ViewModel + Compose Multiplatform proporciona:
- **Persistencia automática** sin necesidad de serialización manual
- **Separación clara** de responsabilidades
- **Debugging facilitado** con logging completo de ciclo de vida
- **Arquitectura escalable** para aplicaciones más complejas
- **Compatibilidad multiplataforma** con el mismo patrón
