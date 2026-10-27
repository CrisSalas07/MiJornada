# CLAUDE.md — MiJornada (IF0009, Android native, Java + XML)

## Context
University assignment "Mi jornada de estudio — versión personal" (UCR Sede del Sur, IF0009).
Native Android app in **Java + XML Views** (NOT Kotlin, NOT Compose). Package: `cr.ac.ucr.mijornada`.
Developer OS: Windows. Use `gradlew.bat`, PowerShell syntax.
The author must explain every change in a video, so code must stay simple, readable and teachable.

## Hard rules
- Java + XML Views only. Never add Kotlin source files, Compose, ViewModel, Room, RecyclerView or new dependencies.
- Do not change existing Gradle dependency versions, compile/target SDK, or the Manifest launcher activity.
  Never add `android:configChanges` to the Manifest (rotation must recreate the Activity).
- Work **one phase at a time**. At the end of each phase: build, run tests, commit, print a short summary
  (what changed, why, how to verify manually) and **STOP**. Do not start the next phase until told.
- Small, focused changes. Do not rewrite files that the phase does not require.
- **Division of work:** the author personally handles the video, the reflection (300–500 words), the explanations
  of the code, and the AI-usage disclosure. Do not write any of these; leave clearly marked placeholders
  (`<!-- AUTHOR: ... -->`). Everything else is your responsibility; do it to the highest quality.
- **Escalate, don't decide:** if anything blocks you or is ambiguous (build/sync errors, unexpected template or
  versions, a requirement that conflicts with this file, a missing tool, a test you cannot make pass), STOP and
  report: what happened, the exact error, and 1–3 options with your recommendation. Wait for the author's answer.
  Never work around a problem silently, never change dependencies/SDK/architecture on your own, never skip or
  weaken a test to make it pass.

## Language conventions
- **All documentation in English**: Javadoc, code comments, README.md, TESTING.md, commit messages.
- **User-facing UI text in Spanish**, always in `res/values/strings.xml` (no hard-coded strings in XML or Java).
- Names required by the assignment stay as given: class `ActividadEstudio` with `nombre` and `minutos`.
  All other identifiers in English.

## Architecture (decided — do not change)
Separate problem rules (pure Java, testable without Android) from Android-specific code:

| File | Responsibility | Depends on Android? |
|---|---|---|
| `model/ActividadEstudio.java` | Immutable model: `nombre`, `minutos`, constructor, getters. | No |
| `model/StudyLog.java` | Owns `ArrayList<ActividadEstudio>`: `add`, `removeLast` (returns boolean), `getActivities` (unmodifiable view), `getCount`, `getTotalMinutes`. Count/total always computed from the collection. | No |
| `model/ActivityValidator.java` | `validate(String name, String minutesText)` → `ValidationResult` enum: `OK`, `EMPTY_NAME`, `INVALID_MINUTES`, `OUT_OF_RANGE`. Constants `MIN_MINUTES = 1`, `MAX_MINUTES = 480`. Trims input. | No |
| `MainActivity.java` | UI only: read inputs, call validator, update `StudyLog`, render list + summary, save/restore Bundle. | Yes |

- Rendering: a single `private void render()` method rebuilds the numbered list and summary by iterating the collection.
- State: in `onSaveInstanceState` save names as `ArrayList<String>` and minutes as `ArrayList<Integer>` (same order);
  restore by rebuilding `ActividadEstudio` objects in order. Bundle keys as `private static final String` constants.
- Error messages: `EditText.setError(getString(...))` + `requestFocus()`. Empty-list delete: Toast with a string resource.
- An invalid input must never modify the collection.

## Quality checklist (every phase)
- `gradlew.bat assembleDebug` succeeds.
- `gradlew.bat test` passes (JUnit 4 tests under `app/src/test/java/cr/ac/ucr/mijornada/`).
- Javadoc on every public class and method; comments explain *why*, not *what*.
- No unused imports, no `import android.R`, no magic numbers.
- Commit message format: `type(scope): summary` (e.g. `feat(model): add StudyLog collection`).

## Phases

### Phase 0 — Repository hygiene
Verify the project is the Empty Views Activity template (Java + `res/layout/activity_main.xml`).
Create/complete `.gitignore` (exclude `build/`, `.gradle/`, `local.properties`, `.idea/`, `*.apk` except none, `captures/`).
`git init` if needed and make an initial commit. Report minSdk, compileSdk, targetSdk, AGP and Gradle versions found.

### Phase 1 — Model and rules (pure Java + unit tests)
Create `ActividadEstudio`, `StudyLog`, `ActivityValidator`.
JUnit tests: validator (empty, spaces-only, empty minutes, non-numeric, 0, 481, 1, 480),
StudyLog (three adds → count 3 and correct total; removeLast; removeLast on empty returns false).

### Phase 2 — Resources and layout
Move all UI text to `strings.xml` (include formatted strings with placeholders, e.g. list item `%1$d. %2$s — %3$d min`).
Update `activity_main.xml`: keep `ScrollView` with id `main` and single child; labels with `labelFor`;
inputs (≥48dp); buttons "Registrar actividad" and "Eliminar último"; list TextView (multiline);
summary TextView with `accessibilityLiveRegion="polite"`; short author line ("Autor: Cristopher …" — leave surname placeholder).
Text in `sp`, dimensions in `dp`. Ensure usable with keyboard open on a small screen.

### Phase 3 — Register flow
Wire `MainActivity`: keep EdgeToEdge + insets listener from the guide, validate → add → render → clear inputs → focus name.

### Phase 4 — Delete last
Delete button calls `StudyLog.removeLast()`; re-render; Toast if the list was empty.

### Phase 5 — Rotation state
Implement save/restore with the two parallel ArrayLists. Verify the Manifest has no `configChanges`.

### Phase 6 — Documentation and packaging
- `README.md` (English): author, requirements and exact versions (from Phase 0), how to run, file structure,
  architecture explanation (XML ↔ Activity ↔ events ↔ model), what survives rotation vs. closing the app,
  difficulties & solutions (placeholder), **reflection placeholder (300–500 words, written by the author)**,
  video link placeholder, sources, AI-usage disclosure placeholder.
- `TESTING.md`: table with columns Input/Action | Expected | Observed | Evidence, pre-filled with the 8 required
  cases (empty name, empty minutes, 0, 481, three valid with verifiable total, delete last, delete on empty, rotation)
  plus limits 1 and 480. Leave Observed/Evidence empty.
- `package.ps1`: builds `Apellido_Nombre_IF0009_AndroidNativo.zip` excluding `build/`, `.gradle/`, `local.properties`, `.idea/`.
