## Plan: Migración a KMP Compose Android

Definir una estructura KMP real con módulo compartido y target Android único, moviendo la UI a commonMain y resolviendo textos con expect/actual sobre R.string. Esto mantiene internacionalización y orientación con recursos Android existentes, y deja la Activity como anfitriona Android.

### Steps 4
1. Actualizar [settings.gradle.kts](D:\Proyectos-git\ALPUSIG-GUAMAN-ERICK-MEDARDO-movswgr1\capitulo01\settings.gradle.kts) para incluir el módulo `:shared`.
2. Crear módulo `shared` KMP con Compose y target Android en su build.gradle.kts.
3. Mover `PantallaConfiguración` a commonMain y definir `expect`/`actual` para `AppStrings.saludo` y `stringResource`.
4. Ajustar [MainActivity.kt](D:\Proyectos-git\ALPUSIG-GUAMAN-ERICK-MEDARDO-movswgr1\capitulo01\app\src\main\java\com\example\moviles2026aswgr1\MainActivity.kt) para usar la UI shared y conservar recursos en res/values*.

### Further Considerations
1. ¿`stringResource` expect/actual o un helper `getString(id)`? A: Composable expect/actual; B: helper con Context.
2. ¿Mantener `configChanges` en Manifest o permitir recreación? A: mantener; B: eliminar para recomposición estándar.

Resumen: Plan entregado para migrar a KMP/Compose Multiplatform con Android único, expect/actual para R.string e i18n/orientación.

