# MiJornada — Mi jornada de estudio (versión personal)

Native Android app for the IF0009 assignment at UCR Sede del Sur. The user registers the study activities of the
day (name and minutes), sees them as a numbered list with a running summary, can delete the last one, and keeps
the list when the screen rotates.

**Author:** Cristopher <!-- AUTHOR: add your surname and student ID (carné). -->
**Course:** IF0009 — Universidad de Costa Rica, Sede del Sur

**Video:** <!-- AUTHOR: paste the video link here. -->

---

## Requirements and versions

| Item | Version |
|---|---|
| Language / UI | Java + XML Views |
| Android Gradle Plugin (AGP) | 9.3.3 |
| Gradle (wrapper) | 9.5.0 |
| Gradle daemon JDK (toolchain) | 25 |
| Java source/target compatibility | 11 |
| `compileSdk` | 37 |
| `targetSdk` | 37 |
| `minSdk` | 24 (Android 7.0) |
| JUnit | 4.13.2 |
| Android Studio | <!-- AUTHOR: Help > About, e.g. "Android Studio X 2026.x.x". --> |

No dependencies were added beyond those of the *Empty Views Activity* template.

## How to run

**Android Studio**
1. *File > Open* and select the project folder.
2. Wait for the Gradle sync to finish.
3. Start an emulator (or connect a phone) with Android 7.0 (API 24) or newer and press *Run 'app'*.

**Command line (PowerShell, from the project folder)**
```powershell
.\gradlew.bat assembleDebug   # build the debug APK
.\gradlew.bat test            # run the JUnit unit tests
.\gradlew.bat installDebug    # install on the running emulator / connected device
```

The unit test report is written to `app\build\reports\tests\testDebugUnitTest\index.html`.

## File structure

```
app/src/main/
├── AndroidManifest.xml                 Declares MainActivity as the launcher (no configChanges)
├── java/cr/ac/ucr/mijornada/
│   ├── MainActivity.java               UI only: reads the form, calls the model, renders, saves/restores state
│   └── model/                          Pure Java, no Android imports
│       ├── ActividadEstudio.java       Immutable activity: nombre + minutos
│       ├── StudyLog.java               The ArrayList of activities; count and total computed from it
│       └── ActivityValidator.java      Validation rules and the 1–480 minute limits
└── res/
    ├── layout/activity_main.xml        ScrollView > LinearLayout with the form, buttons, summary and list
    ├── values/strings.xml              Every user-facing text (Spanish), including formatted strings and plurals
    └── values/dimens.xml               Spacing (dp), text sizes (sp) and the 48dp touch target
app/src/test/java/cr/ac/ucr/mijornada/model/
├── ActivityValidatorTest.java          Empty / spaces-only name, empty and non-numeric minutes, 0, 481, 1, 480
└── StudyLogTest.java                   Three adds, removeLast, removeLast on empty, read-only view
TESTING.md                              Manual test table
package.ps1                             Builds the delivery ZIP
```

## Architecture

The app separates **the rules of the problem** from **the Android screen**:

```
activity_main.xml  ──inflated by──▶  MainActivity  ──asks──▶  ActivityValidator
   (views + ids)                    (events, render)  ──updates──▶  StudyLog ──holds──▶ ActividadEstudio
```

1. **XML ↔ Activity.** `setContentView(R.layout.activity_main)` creates the views described in the layout, and
   `findViewById(R.id.…)` gives `MainActivity` a reference to each one. All texts come from `strings.xml`.
2. **Events.** The two buttons get click listeners (`setOnClickListener`). Android calls
   `registerActivity()` or `deleteLastActivity()` when the user taps them.
3. **Model.** `MainActivity` never decides whether input is valid: it passes the raw text to
   `ActivityValidator.validate(...)`, which returns `OK`, `EMPTY_NAME`, `INVALID_MINUTES` or `OUT_OF_RANGE`.
   Only on `OK` is a new `ActividadEstudio` added to the `StudyLog`; every error returns before the collection is
   touched. Errors are shown with `EditText.setError(...)` and the cursor moves to the wrong field.
4. **Rendering.** A single `render()` method rebuilds the numbered list and the summary by iterating the
   collection every time it changes, so the screen can never drift from the data.
5. **Testing.** Because the model has no Android imports, its rules run as plain JUnit tests on the computer, without
   an emulator.

Edge-to-edge is enabled as in the course guide. The window-insets listener pads the `ScrollView` for the system bars
**and the keyboard** (`systemBars() | ime()`), so the form can scroll above the keyboard on small screens.

## What survives and what does not

| Situation | Registered activities | Text being typed |
|---|---|---|
| Screen rotation (or other configuration change) | **Kept** — saved in `onSaveInstanceState` as two parallel lists (names and minutes) and rebuilt in `onCreate` | Kept — Android saves the state of views that have an id |
| App sent to the background and later killed by the system to free memory | **Kept** — Android hands the same saved `Bundle` back when the user returns | Kept |
| User closes the app (swipes it away from *Recents*) or the device restarts | **Lost** — the Bundle is only for temporary UI state; nothing is written to storage | Lost |

Rotation recreates the Activity on purpose: the Manifest has no `android:configChanges`, so the save/restore code
is what keeps the data.

## Difficulties and solutions

<!-- AUTHOR: describe the difficulties you found and how you solved them. -->

## Reflection

<!-- AUTHOR: personal reflection, 300–500 words, written by you. -->

## Sources

- Android Developers — Activity lifecycle and saving UI state:
  <https://developer.android.com/guide/components/activities/activity-lifecycle>,
  <https://developer.android.com/topic/libraries/architecture/saving-states>
- Android Developers — Display content edge-to-edge and window insets:
  <https://developer.android.com/develop/ui/views/layout/edge-to-edge>
- Android Developers — String resources, formatting and plurals:
  <https://developer.android.com/guide/topics/resources/string-resource>
- Android Developers — Make apps more accessible (labels, touch targets, live regions):
  <https://developer.android.com/guide/topics/ui/accessibility/apps>
- Android Developers — Build local unit tests: <https://developer.android.com/training/testing/local-tests>
- JUnit 4: <https://junit.org/junit4/>
<!-- AUTHOR: add the course guide and any other material you used. -->

## Use of AI

<!-- AUTHOR: AI-usage disclosure, written by you. -->
