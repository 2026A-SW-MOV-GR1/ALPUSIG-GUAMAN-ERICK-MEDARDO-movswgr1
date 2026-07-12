# Instrucciones de Prueba: Contador Persistente en KMP

## Requisitos
- Android Studio con SDK 34+
- Emulador o dispositivo Android

## Cómo Ejecutar

### 1. Compilar y Ejecutar
```bash
cd d:\Taller07_ciclo_vida
./gradlew build
./gradlew installDebug
# O abrir en Android Studio y presionar Run
```

### 2. Ver los Logs en la Consola

Abre **Logcat** en Android Studio:
- Menú: View → Tool Windows → Logcat
- Filtra por "LIFECYCLE" para ver solo nuestros logs

## Escenarios de Prueba

### Escenario 1: Inicio de la Aplicación
**Pasos:**
1. Inicia la app desde Android Studio
2. Observa los logs en Logcat

**Logs Esperados:**
```
[LIFECYCLE] onCreate - Activity created
[LIFECYCLE] onStart - Activity is starting
[LIFECYCLE] onResume - Activity became visible and interactive
```

**Resultado:** ✅ La app aparece con contador en 0

---

### Escenario 2: Incrementar el Contador
**Pasos:**
1. Presiona el botón "➕ Incrementar" varias veces (por ejemplo, 10 veces)
2. Observa el valor en pantalla
3. Verifica los logs

**Logs Esperados:**
```
[LIFECYCLE] COUNT_INCREMENTED: 1
[LIFECYCLE] COUNT_INCREMENTED: 2
...
[LIFECYCLE] COUNT_INCREMENTED: 10
[LIFECYCLE] App Recomposed - Count: 10
```

**Resultado:** ✅ El contador aumenta y los logs muestran cada incremento

---

### Escenario 3: Rotación de Pantalla (Prueba CRÍTICA)
**Pasos:**
1. Ingresa el contador a un número específico (ej: 15)
2. Gira el dispositivo/emulador (Ctrl+F12 en emulador, o físicamente en dispositivo)
3. Observa si el contador mantiene el valor de 15

**Logs Esperados:**
```
[LIFECYCLE] onPause - Activity is about to lose focus
[LIFECYCLE] onStop - Activity is no longer visible
[LIFECYCLE] onSaveInstanceState - Saving state before destruction
[LIFECYCLE] onDestroy - Activity is being destroyed (rotation or exit)
[LIFECYCLE] onCreate (MainActivity.onCreate called)
[LIFECYCLE] onCreate - savedInstanceState restored (rotation detected)
[LIFECYCLE] onStart - Activity is starting
[LIFECYCLE] onRestore - Restoring state after recreation
[LIFECYCLE] onResume - Activity became visible and interactive
```

**Resultado:** ✅ El contador sigue siendo 15 después de la rotación (PERSISTENCIA CONFIRMADA)

---

### Escenario 4: Multitarea (Home → App)
**Pasos:**
1. Ten el contador con un valor específico (ej: 7)
2. Presiona el botón HOME para enviar la app al background
3. Presiona el ícono de la app para traerla de vuelta
4. Verifica que el contador sigue siendo 7

**Logs Esperados:**
```
[LIFECYCLE] onPause - Activity is about to lose focus
[LIFECYCLE] onStop - Activity is no longer visible
... (usuario hace otra cosa)
[LIFECYCLE] onRestart - Activity returning from background
[LIFECYCLE] onStart - Activity is starting
[LIFECYCLE] onResume - Activity became visible and interactive
```

**Resultado:** ✅ El contador mantiene el valor (7)

---

### Escenario 5: Cerrar la App
**Pasos:**
1. Presiona el botón BACK o "Exit" para cerrar completamente
2. Vuelve a abrir la app
3. Verifica que el contador vuelve a 0

**Logs Esperados:**
```
[LIFECYCLE] onPause
[LIFECYCLE] onStop
[LIFECYCLE] onDestroy
... (app cerrada)
[LIFECYCLE] onCreate - Activity created
[LIFECYCLE] onCreate - First time launch (no savedInstanceState)
[LIFECYCLE] onStart - Activity is starting
[LIFECYCLE] onResume - Activity became visible and interactive
```

**Resultado:** ✅ El contador se reinicia a 0 (comportamiento esperado)

---

## Diferencia Clave: Rotación vs Cierre

| Evento | Rotación | Cierre Total |
|--------|----------|--------------|
| onDestroy | Sí, Activity se destruye | Sí, Activity se destruye |
| ViewModel | Se retiene en memoria | Se destruye |
| Contador | Se mantiene (persistido) | Se reinicia a 0 |
| savedInstanceState | Se usa para UI | Se usa para Bundle |

---

## Mapeo de Eventos del Ciclo de Vida

```
┌─────────────────────────────────────────────────┐
│          PRIMER INICIO (COLD START)             │
└─────────────────────────────────────────────────┘
                    ↓
              onCreate()
                    ↓
              onStart()
                    ↓
             onResume() ← APP VISIBLE Y ACTIVA
                    ↓
         (usuario interactúa)
                    ↓
              [ROTACIÓN]
                    ↓
              onPause()
              onStop()
         onSaveInstanceState()
              onDestroy() ← Activity se destruye
            [ViewModel persiste]
              onCreate() ← NEW Activity
         onRestoreInstanceState()
              onStart()
             onResume() ← APP VISIBLE CON DATOS GUARDADOS
                    ↓
         (usuario interactúa)
                    ↓
              onPause()
              onStop()
              [HOME BUTTON]
            (app en background)
                    ↓
              onRestart() ← Activity retorna del background
              onStart()
             onResume()
                    ↓
              [BACK BUTTON]
              onPause()
              onStop()
              onDestroy() ← Activity se destruye
             ViewModel onCleared()
```

---

## Archivos Clave

- **CounterViewModel.kt**: Lógica del contador y logging
- **App.kt**: UI Compose con botones e incremento
- **MainActivity.kt**: Ciclo de vida de Android con eventos loguados
- **LifecycleObserver.kt**: Observer para interceptar eventos

---

## Conclusión de Pruebas

✅ **Todos los requisitos del plan_Implementacion.md completados:**
1. ✅ Contador que suma +1
2. ✅ Texto que muestra el valor
3. ✅ **Persistencia en rotación** (sin onSaveInstanceState manual)
4. ✅ Logs de ciclo de vida completo
5. ✅ Diferenciación de eventos (onCreate, onStart, onResume, onPause, onStop, onDestroy, onRestart)
6. ✅ Solución multiplataforma en KMP
