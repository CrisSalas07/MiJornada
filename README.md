# MiJornada — Mi jornada de estudio (versión personal)

Aplicación Android nativa para la tarea de IF0009 en la UCR Sede del Sur. El usuario registra las actividades de
estudio del día (nombre y minutos), las ve como una lista numerada con un resumen actualizado, puede eliminar la
última y conserva la lista cuando la pantalla rota.

**Autor:** Cristopher <!-- AUTOR: agregue sus apellidos y carné. -->
**Curso:** IF0009 — Universidad de Costa Rica, Sede del Sur

**Video:** <!-- AUTOR: pegue aquí el enlace del video. -->

---

## Requisitos y versiones

| Elemento | Versión |
|---|---|
| Lenguaje / UI | Java + vistas XML |
| Android Gradle Plugin (AGP) | 9.3.3 |
| Gradle (wrapper) | 9.5.0 |
| JDK del daemon de Gradle (toolchain) | 25 |
| Compatibilidad Java source/target | 11 |
| `compileSdk` | 37 |
| `targetSdk` | 37 |
| `minSdk` | 24 (Android 7.0) |
| JUnit | 4.13.2 |
| Android Studio | <!-- AUTOR: Help > About, p. ej. "Android Studio X 2026.x.x". --> |

No se agregaron dependencias además de las de la plantilla *Empty Views Activity*.

## Cómo ejecutar

**Android Studio**
1. *File > Open* y seleccione la carpeta del proyecto.
2. Espere a que termine la sincronización de Gradle.
3. Inicie un emulador (o conecte un teléfono) con Android 7.0 (API 24) o superior y presione *Run 'app'*.

**Línea de comandos (PowerShell, desde la carpeta del proyecto)**
```powershell
.\gradlew.bat assembleDebug   # compila el APK de depuración
.\gradlew.bat test            # ejecuta las pruebas unitarias JUnit
.\gradlew.bat installDebug    # instala en el emulador en ejecución / dispositivo conectado
```

El reporte de las pruebas unitarias se genera en `app\build\reports\tests\testDebugUnitTest\index.html`.

## Estructura de archivos

```
app/src/main/
├── AndroidManifest.xml                 Declara MainActivity como launcher (sin configChanges)
├── java/cr/ac/ucr/mijornada/
│   ├── MainActivity.java               Solo UI: lee el formulario, llama al modelo, dibuja, guarda/restaura el estado
│   └── model/                          Java puro, sin imports de Android
│       ├── ActividadEstudio.java       Actividad inmutable: nombre + minutos
│       ├── StudyLog.java               El ArrayList de actividades; cantidad y total se calculan a partir de él
│       └── ActivityValidator.java      Reglas de validación y los límites de 1–480 minutos
└── res/
    ├── layout/activity_main.xml        ScrollView > LinearLayout con el formulario, botones, resumen y lista
    ├── values/strings.xml              Todos los textos visibles (en español), incluidos strings con formato y plurales
    └── values/dimens.xml               Espaciado (dp), tamaños de texto (sp) y el área táctil de 48dp
app/src/test/java/cr/ac/ucr/mijornada/model/
├── ActivityValidatorTest.java          Nombre vacío / solo espacios, minutos vacíos y no numéricos, 0, 481, 1, 480
└── StudyLogTest.java                   Tres inserciones, removeLast, removeLast en lista vacía, vista de solo lectura
TESTING.md                              Tabla de pruebas manuales
package.ps1                             Genera el ZIP de entrega
```

## Arquitectura

La aplicación separa **las reglas del problema** de **la pantalla de Android**:

```
activity_main.xml  ──inflado por──▶  MainActivity  ──consulta──▶  ActivityValidator
   (vistas + ids)                   (eventos, render)  ──actualiza──▶  StudyLog ──contiene──▶ ActividadEstudio
```

1. **XML ↔ Activity.** `setContentView(R.layout.activity_main)` crea las vistas descritas en el layout, y
   `findViewById(R.id.…)` le da a `MainActivity` una referencia a cada una. Todos los textos vienen de `strings.xml`.
2. **Eventos.** Los dos botones reciben listeners de clic (`setOnClickListener`). Android llama a
   `registerActivity()` o `deleteLastActivity()` cuando el usuario los toca.
3. **Modelo.** `MainActivity` nunca decide si la entrada es válida: pasa el texto crudo a
   `ActivityValidator.validate(...)`, que devuelve `OK`, `EMPTY_NAME`, `INVALID_MINUTES` u `OUT_OF_RANGE`.
   Solo con `OK` se agrega una nueva `ActividadEstudio` al `StudyLog`; cada error retorna antes de tocar la
   colección. Los errores se muestran con `EditText.setError(...)` y el cursor pasa al campo incorrecto.
4. **Dibujado.** Un único método `render()` reconstruye la lista numerada y el resumen recorriendo la colección
   cada vez que cambia, de modo que la pantalla nunca se desfasa de los datos.
5. **Pruebas.** Como el modelo no tiene imports de Android, sus reglas se ejecutan como pruebas JUnit normales en la
   computadora, sin emulador.

Edge-to-edge está habilitado como en la guía del curso. El listener de window insets agrega padding al `ScrollView`
para las barras del sistema **y el teclado** (`systemBars() | ime()`), así el formulario puede desplazarse por encima
del teclado en pantallas pequeñas.

## Qué se conserva y qué no

| Situación | Actividades registradas | Texto que se está escribiendo |
|---|---|---|
| Rotación de pantalla (u otro cambio de configuración) | **Se conservan** — se guardan en `onSaveInstanceState` como dos listas paralelas (nombres y minutos) y se reconstruyen en `onCreate` | Se conserva — Android guarda el estado de las vistas que tienen id |
| La app pasa a segundo plano y luego el sistema la cierra para liberar memoria | **Se conservan** — Android devuelve el mismo `Bundle` guardado cuando el usuario regresa | Se conserva |
| El usuario cierra la app (la desliza fuera de *Recientes*) o el dispositivo se reinicia | **Se pierden** — el Bundle es solo para estado temporal de la UI; nada se escribe en almacenamiento | Se pierde |

La rotación recrea la Activity a propósito: el Manifest no tiene `android:configChanges`, así que el código de
guardar/restaurar es lo que conserva los datos.

## Dificultades y soluciones

<!-- AUTOR: describa las dificultades que encontró y cómo las resolvió. -->

## Reflexión

<!-- AUTOR: reflexión personal, 300–500 palabras, escrita por usted. -->

## Fuentes

- Android Developers — Ciclo de vida de la Activity y guardado del estado de la UI:
  <https://developer.android.com/guide/components/activities/activity-lifecycle>,
  <https://developer.android.com/topic/libraries/architecture/saving-states>
- Android Developers — Mostrar contenido edge-to-edge y window insets:
  <https://developer.android.com/develop/ui/views/layout/edge-to-edge>
- Android Developers — Recursos de strings, formato y plurales:
  <https://developer.android.com/guide/topics/resources/string-resource>
- Android Developers — Hacer apps más accesibles (etiquetas, áreas táctiles, live regions):
  <https://developer.android.com/guide/topics/ui/accessibility/apps>
- Android Developers — Crear pruebas unitarias locales: <https://developer.android.com/training/testing/local-tests>
- JUnit 4: <https://junit.org/junit4/>
<!-- AUTOR: agregue la guía del curso y cualquier otro material que haya usado. -->

## Uso de IA

<!-- AUTOR: declaración de uso de IA, escrita por usted. -->
