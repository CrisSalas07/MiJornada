# Manual tests — MiJornada

Run the cases **in order** on a fresh launch of the app: each row starts from the state the previous row left.
Exact texts come from `app/src/main/res/values/strings.xml`.

**Device / emulator used:** <!-- AUTHOR: e.g. "Pixel 4a emulator, API 37". -->

| # | Input / Action | Expected | Observed | Evidence |
|---|---|---|---|---|
| T1 | **Empty name.** Nombre: *(empty)*. Minutos: `30`. Tap *Registrar actividad*. | Error on the name field: "Escriba el nombre de la actividad." Cursor in the name field. List still "Aún no hay actividades registradas." | | |
| T2 | **Empty minutes.** Nombre: `Cálculo`. Minutos: *(empty)*. Tap *Registrar actividad*. | Error on the minutes field: "Escriba los minutos como un número entero." Cursor in minutes. List unchanged (empty). | | |
| T3 | **Zero minutes.** Nombre: `Cálculo`. Minutos: `0`. Tap *Registrar actividad*. | Error on minutes: "Los minutos deben estar entre 1 y 480." List unchanged (empty). | | |
| T4 | **Above maximum.** Nombre: `Cálculo`. Minutos: `481`. Tap *Registrar actividad*. | Error on minutes: "Los minutos deben estar entre 1 y 480." List unchanged (empty). | | |
| T5 | **Lower limit (1).** Nombre: `Lectura`. Minutos: `1`. Tap *Registrar actividad*. | Accepted. List: "1. Lectura — 1 min". Summary: "Resumen: 1 actividad, 1 min en total". Both fields cleared, cursor in name. | | |
| T6 | **Upper limit (480).** Nombre: `Proyecto`. Minutos: `480`. Tap *Registrar actividad*. | Accepted. List adds "2. Proyecto — 480 min". Summary: "Resumen: 2 actividades, 481 min en total". | | |
| T7 | **Delete last.** Tap *Eliminar último* once. | "2. Proyecto — 480 min" disappears. List: "1. Lectura — 1 min". Summary: "Resumen: 1 actividad, 1 min en total". | | |
| T8 | **Delete on empty list.** Tap *Eliminar último* once (list becomes empty), then tap it again. | First tap: list "Aún no hay actividades registradas.", summary "Resumen: 0 actividades, 0 min en total". Second tap: Toast "No hay actividades para eliminar."; nothing else changes. | | |
| T9 | **Three valid activities.** Register `Cálculo`/`30`, then `Programación`/`45`, then `Inglés`/`25`. | List: "1. Cálculo — 30 min", "2. Programación — 45 min", "3. Inglés — 25 min". Summary: "Resumen: 3 actividades, 100 min en total" (30 + 45 + 25 = 100). | | |
| T10 | **Rotation with records.** With the three activities of T9, rotate to landscape, then back to portrait. | After each rotation the same three items, in the same order, and "Resumen: 3 actividades, 100 min en total". | | |
