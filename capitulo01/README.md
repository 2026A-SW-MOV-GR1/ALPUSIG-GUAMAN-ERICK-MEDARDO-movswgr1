# KMP Compose Android (solo Android)

Este proyecto migra la UI a un modulo KMP `shared` usando Compose Multiplatform, con recursos Android y `expect/actual` para leer `R.string` y colores por idioma y orientacion.

## Estructura
- `shared`: modulo KMP con UI Compose y acceso a recursos via `expect/actual`.
- `app`: host Android que solo llama a `AppScreen()`.

## Ejecutar (Android)
```powershell
.\gradlew.bat :app:assembleDebug
```

## Recursos KMP vs Android
- Recursos comunes CMP: `shared/src/commonMain/composeResources/values`.
- Recursos Android con calificadores: `shared/src/androidMain/res/values*`.
- En Android se usa `R.*` via `expect/actual`; en Preview se usan recursos comunes para evidenciar la capa compartida.

## Notas
- Los textos y colores estan en `shared/src/androidMain/res/values*`.
- `AppResources` usa `expect/actual` para leer `R.string` y `R.color`.
