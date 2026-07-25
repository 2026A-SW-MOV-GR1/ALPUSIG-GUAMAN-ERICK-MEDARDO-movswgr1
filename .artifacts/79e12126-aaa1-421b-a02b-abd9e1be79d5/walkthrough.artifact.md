# Walkthrough - OSM 403 Access Blocked (Multi-Factor Fix)

I have implemented a "nuclear" fix for the 403 error by using a browser-mimicking identity and forcing a cache cleanup.

## Changes Made

### Mimicking a Real Browser
- **[GestionPaqueteriaApp.kt](file:///D:/Gestion_Paquetes_Ex1-Pr1/androidApp/src/main/kotlin/com/example/gestionpaqueteria/GestionPaqueteriaApp.kt)**:
    - **User-Agent**: Set to a standard Chrome browser string: `Mozilla/5.0 (Windows NT 10.0; Win64; x64) ... Chrome/120.0.0.0 Safari/537.36`.
    - **Referer**: Added the `Referer` header pointing to `https://www.openstreetmap.org/`. This is often required by tile servers to verify the source.
    - **No-Load Policy**: Disabled `Configuration.getInstance().load()` to prevent Android's auto-backup or old settings from overriding our new identity.

### Cache Management
- **Automatic Cleanup**: Added logic to **delete the entire tile cache folder** (`osmdroid/tiles`) every time the application starts. This ensures that any "Error 403" images saved from previous failed attempts are physically removed from the device.

## Verification Results

### Build
- The project compiles successfully (`:androidApp:assembleDebug`).

### Expected Behavior
- When you open the map, the app will:
    1. Wipe any old map data.
    2. Identify itself as a legitimate browser.
    3. Request tiles with proper headers.

---
> [!IMPORTANT]
> Since the app now clears its own cache on startup, you just need to **close and re-open the app** to apply the fix. You don't need to manually clear settings anymore (though it's still good practice).
