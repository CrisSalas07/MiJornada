# Pruebas manuales — MiJornada

Ejecute los casos **en orden** sobre un inicio limpio de la app: cada fila parte del estado que dejó la anterior.
Los textos exactos vienen de `app/src/main/res/values/strings.xml`.

**Dispositivo / emulador utilizado:** <!-- AUTOR: p. ej. "Emulador Pixel 4a, API 37". -->

| # | Entrada / Acción | Esperado | Observado | Evidencia |
|---|---|---|---|---|
| T1 | **Nombre vacío.** Nombre: *(vacío)*. Minutos: `30`. Toque *Registrar actividad*. | Error en el campo nombre: "Escriba el nombre de la actividad." Cursor en el campo nombre. La lista sigue en "Aún no hay actividades registradas." | | |
| T2 | **Minutos vacíos.** Nombre: `Cálculo`. Minutos: *(vacío)*. Toque *Registrar actividad*. | Error en el campo minutos: "Escriba los minutos como un número entero." Cursor en minutos. Lista sin cambios (vacía). | | |
| T3 | **Cero minutos.** Nombre: `Cálculo`. Minutos: `0`. Toque *Registrar actividad*. | Error en minutos: "Los minutos deben estar entre 1 y 480." Lista sin cambios (vacía). | | |
| T4 | **Sobre el máximo.** Nombre: `Cálculo`. Minutos: `481`. Toque *Registrar actividad*. | Error en minutos: "Los minutos deben estar entre 1 y 480." Lista sin cambios (vacía). | | |
| T5 | **Límite inferior (1).** Nombre: `Lectura`. Minutos: `1`. Toque *Registrar actividad*. | Aceptada. Lista: "1. Lectura — 1 min". Resumen: "Resumen: 1 actividad, 1 min en total". Ambos campos limpios, cursor en nombre. | | |
| T6 | **Límite superior (480).** Nombre: `Proyecto`. Minutos: `480`. Toque *Registrar actividad*. | Aceptada. La lista agrega "2. Proyecto — 480 min". Resumen: "Resumen: 2 actividades, 481 min en total". | | |
| T7 | **Eliminar último.** Toque *Eliminar último* una vez. | "2. Proyecto — 480 min" desaparece. Lista: "1. Lectura — 1 min". Resumen: "Resumen: 1 actividad, 1 min en total". | | |
| T8 | **Eliminar con lista vacía.** Toque *Eliminar último* una vez (la lista queda vacía) y luego tóquelo otra vez. | Primer toque: lista "Aún no hay actividades registradas.", resumen "Resumen: 0 actividades, 0 min en total". Segundo toque: Toast "No hay actividades para eliminar."; nada más cambia. | | |
| T9 | **Tres actividades válidas.** Registre `Cálculo`/`30`, luego `Programación`/`45`, luego `Inglés`/`25`. | Lista: "1. Cálculo — 30 min", "2. Programación — 45 min", "3. Inglés — 25 min". Resumen: "Resumen: 3 actividades, 100 min en total" (30 + 45 + 25 = 100). | | |
| T10 | **Rotación con registros.** Con las tres actividades de T9, rote a horizontal y luego de vuelta a vertical. | Tras cada rotación, los mismos tres elementos en el mismo orden y "Resumen: 3 actividades, 100 min en total". | | |
