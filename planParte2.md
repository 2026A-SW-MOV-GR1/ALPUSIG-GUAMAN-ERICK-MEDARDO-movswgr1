# Prompt Agente — Parte 2: Envío de Intent hacia App 2 (Distribución)
> Prerequisito: `planParte1.md` completamente funcional.
> Esta parte no modifica el mapa ni el formulario; solo agrega el mecanismo
> de salida del flujo logístico hacia la siguiente app.

---

## Contexto de negocio

Una vez que el operador confirmó el registro del paquete en la App Admisión,
el sistema debe "pasarlo" automáticamente a la App de Distribución
(de Christian Aragon). Eso se hace lanzando un Intent de Android con todos
los datos del paquete encapsulados. La App 2 los recibirá y continuará el
flujo logístico sin pedirle nada extra al usuario.

---

## Lo que debe existir al terminar

### 1. Formato del dato que viaja en el Intent

El objeto `Paquete` (definido en `planParte1.md`, `commonMain`) se serializa
completo a JSON usando `kotlinx.serialization` y viaja como un único
`String` extra en el Intent.

- **Key del extra:** `"paquete_json"`
- **Valor:** el JSON del objeto `Paquete` con todos sus campos completos.

Ejemplo del JSON que debe generarse (los valores son ilustrativos):
```
{
  "idPaquete": "AMZ-1721130600000",
  "remitente": "Amazon FC Quito Norte",
  "destinatario": "María Pérez",
  "direccionOrigen": "Av. Amazonas y Naciones Unidas, Quito",
  "latOrigen": -0.180653,
  "lngOrigen": -78.467834,
  "fechaAdmision": "2026-07-16T09:30:00",
  "estado": "ADMITIDO"
}
```

### 2. Tipo de Intent: implícito con action personalizada

Usar un Intent **implícito**, no explícito. Esto desacopla la App 1 del
`applicationId` específico de la App 2.

- **Action del Intent:**
  `"com.equipoamazon.intent.action.ADMISION_TO_DISTRIBUCION"`
- **Category:** `Intent.CATEGORY_DEFAULT`

La App 2 (Christian) deberá declarar un `intent-filter` en su
`AndroidManifest.xml` escuchando esa misma action. Eso es responsabilidad
de él, no de esta app.

### 3. Declaración en `AndroidManifest.xml` de la App Admisión

Para que Android 11+ (API 30+) permita que esta app detecte si la App 2
está instalada, agregar un bloque `<queries>` en el manifest:

- Declarar dentro de `<queries>` un `<intent>` con la action
  `"com.equipoamazon.intent.action.ADMISION_TO_DISTRIBUCION"`.

Sin esto, `resolveActivity()` puede devolver `null` aunque la App 2
esté correctamente instalada (restricción de visibilidad de paquetes
de Android 11).

### 4. Función de envío (en `androidApp`, no en `commonMain`)

Crear una función en la capa Android (no en `commonMain`, porque usa
`Context` e `Intent` de Android) que:

1. Tome el objeto `Paquete` ya construido.
2. Lo serialice a JSON.
3. Construya el Intent con la action y el extra `"paquete_json"`.
4. Verifique con `resolveActivity()` que existe al menos una app capaz de
   resolver ese Intent.
5. Si existe: llame a `startActivity(intent)`.
6. Si no existe: muestre un mensaje claro al usuario indicando que la
   App de Distribución no está instalada, **sin lanzar excepción ni crashear**.

### 5. Dónde se invoca

En la pantalla de confirmación (el último paso del flujo del
`planParte1.md`), el botón "Enviar a Distribución" debe llamar a esta
función con el objeto `Paquete` ya construido y validado.

El botón solo debe estar habilitado cuando:
- El objeto `Paquete` esté completamente construido (todos los campos
  llenos y coordenadas confirmadas).
- No antes.

### 6. Cómo probar esta parte sin que exista la App 2

No depender de que Christian tenga su app lista. Dos opciones válidas
para probar de forma independiente:

**Opción A — ADB desde terminal:**
Simular la recepción del Intent con el comando `adb shell am start`
pasando la action y el extra `paquete_json` con un JSON de prueba.
Esto confirma que el Intent está bien formado incluso sin la App 2.

**Opción B — App receptora mínima:**
Crear un proyecto Android separado y temporal (no KMP, puede ser un
proyecto vacío básico) con una sola Activity que declare el
`intent-filter` de la action acordada y muestre en pantalla el valor
del extra `"paquete_json"` recibido. Usar solo para validar y descartar.

---

## Contrato de datos para el equipo

Compartir con Christian (App 2) y Dorian (App 3) lo siguiente antes
de que ellos programen sus receptores:

| Campo | Tipo | Descripción |
|---|---|---|
| `idPaquete` | String | ID único generado en App 1 — no modificar en apps siguientes |
| `remitente` | String | Origen del paquete |
| `destinatario` | String | Cliente final |
| `direccionOrigen` | String | Referencia textual del punto de recogida |
| `latOrigen` | Double | Coordenada fijada en el mapa de App 1 |
| `lngOrigen` | Double | Coordenada fijada en el mapa de App 1 |
| `fechaAdmision` | String (ISO-8601) | Timestamp de admisión |
| `estado` | String | `"ADMITIDO"` al salir de App 1; cada app siguiente lo actualiza |

La key del extra es siempre `"paquete_json"` y el valor es el JSON
completo serializado. Christian debe leer ese extra en su `Intent`
recibido y deserializarlo con su propia copia del modelo (o la misma
si comparten un módulo).

---

## Restricciones y aclaraciones

- No usar `startActivity` sin verificar `resolveActivity` antes.
- No hardcodear el `applicationId` de la App 2 en ningún lugar;
  usar únicamente la action del Intent para mantener el desacoplamiento.
- No agregar campos nuevos al `Paquete` sin comunicarlo al equipo;
  el contrato de datos es grupal.
- Esta parte no requiere cambios en el mapa ni en el formulario,
  solo se añade la función de envío y se conecta al botón existente.
