# 🧪 Android Studio Test — MVVM & Testing

Aplicación Android desarrollada en **Kotlin** con **Jetpack Compose** siguiendo el patrón de arquitectura **MVVM**, con estado reactivo mediante **LiveData**. El proyecto sirve como base práctica para **Unit Testing** sobre el ViewModel y **Instrumental UI Testing** sobre los composables de la pantalla principal.

## 📱 Descripción

Este repositorio contiene una app de ejemplo con interfaz Compose (`MainView`) que permite interactuar con distintos controles: interruptor Wi‑Fi, opciones de menú (checkboxes), TriState checkbox, radios de selección, control de volumen (slider), menú desplegable, campo de búsqueda con feedback y un botón de estado. La lógica y el estado mutable residen en `MainViewModel`, observados desde la vista.

> **Nota:** Parte de las etiquetas de la interfaz siguen en **catalán** (según el diseño original de la base del repositorio); este README y gran parte de los comentarios del código están en **castellano**.

## ✨ Características de la aplicación

- ⚡ **Switch** para activar/desactivar estado Wi‑Fi
- ✅ **Opciones de menú** carnívoro / vegetariano / vegano con checkboxes (incluye estados deshabilitados según el diseño original; textos en catalán en pantalla)
- ☑️ **TriState checkbox** para **ciclar** entre Off, Indeterminate y On
- 🔘 **Radio buttons** (Pilota d'Or / jugadores; textos según la app)
- 🎚️ **Slider de volumen** con porcentaje en pantalla
- 📋 **Dropdown** con Opción A/B/C (en catalán en la app: «Opció»)
- 🔎 **OutlinedTextField** de búsqueda y botón **Buscar** con mensaje de confirmación (`showSnackbar` / texto “Acció completada!”)
- 🔄 **Botón** de estado Activado / Desactivado (Activat / Desactivat en la interfaz) según color de contenedor
- ✅ **Instrumentos de testing** en `MainView` mediante `Modifier.testTag(...)` para localizar nodos desde tests Compose

## 🏗️ Arquitectura

El proyecto sigue el patrón **MVVM (Model-View-ViewModel)** con **LiveData** para la gestión reactiva:

```
├── view/
│   └── MainView.kt              # UI Compose: observa LiveData del ViewModel (observeAsState)
├── viewmodel/
│   └── MainViewModel.kt         # Estado MutableLiveData + funciones públicas de negocio
├── ui/theme/
│   ├── Color.kt
│   ├── Theme.kt
│   └── Type.kt
└── MainActivity.kt              # setContent → MainView(viewModel)

app/src/test/…
└── MainViewModelUnitTest.kt     # Unit tests (JVM): ViewModel + InstantTaskExecutorRule

app/src/androidTest/…
├── ViewInstrumentedUITest.kt   # Instrumented UI: createAndroidComposeRule<MainActivity>
└── ExampleInstrumentedTest.kt
```

## 🚀 Tecnologías utilizadas

| Área | Tecnología |
|------|-------------|
| Lenguaje | Kotlin |
| UI | Jetpack Compose, Material 3 |
| Arquitectura | MVVM |
| Estado | `MutableLiveData` / `LiveData`, suscripción desde Compose con `observeAsState` |
| Lifecycle | `lifecycle-runtime-ktx`, `Activity` + `viewModels()` |
| Unit tests | JUnit 4, `androidx.arch.core:core-testing` (`InstantTaskExecutorRule`) |
| Instrumented UI tests | Compose UI Test (`ui-test-junit4`), Espresso sincronización, `AndroidJUnitRunner` |

## 🧾 Objetivos de práctica cumplidos

- Implementación **MVVM** con datos reactivos vía **LiveData**
- **Unit Testing** sobre los métodos públicos del `MainViewModel` (28 tests JVM)
- **Instrumental UI Testing** sobre elementos de `MainView` con `@get:Rule composeTestRule = createAndroidComposeRule<MainActivity>()` y `onNodeWithTag(...)` (8 tests)
- Dependencias de test declaradas en **`app/build.gradle.kts`**

## 🎥 Demostración del testing en vídeo

- [Demostración de los tests en YouTube](https://youtu.be/5u2amIsBQWk)

## 🖥️ Cómo ejecutar los tests

### Qué se declaró en el proyecto para que los tests funcionen correctamente

Para que **pasen en local** tanto los unitarios como los instrumentados se hizo lo siguiente (resumen de lo que llevas en el repo):

| Ámbito | Qué se añadió / configuró |
|--------|---------------------------|
| **`app/build.gradle.kts`** | `testImplementation` de **JUnit** y **`androidx.arch.core:core-testing`** (necesario para `InstantTaskExecutorRule` y LiveData en JVM). |
| | `androidTestImplementation` de **JUnit AndroidX**, **Espresso Core**, **Compose UI Test** (`ui-test-junit4`) y **BOM** de Compose también en `androidTest`. |
| | `debugImplementation` de **`ui-tooling`** y **`ui-test-manifest`** (Compose / tests de UI en debug). |
| | `implementation` de **`runtime-livedata`** para usar `observeAsState` con LiveData desde Compose. |
| | `defaultConfig { testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner" }` para ejecutar tests en dispositivo/emulador. |
| **`MainViewModelUnitTest`** | Regla `@get:Rule val instantTaskExecutorRule = InstantTaskExecutorRule()` e **import** `androidx.arch.core.executor.testing.InstantTaskExecutorRule` (sin esto, LiveData no se comporta bien en tests JVM). |
| **`ViewInstrumentedUITest`** | `@get:Rule val composeTestRule = createAndroidComposeRule<MainActivity>()`, `@RunWith(AndroidJUnit4::class)`, tests con `onNodeWithTag` / acciones Compose. |
| **`MainView`** | `Modifier.testTag("...")` en los composables que hay que localizar desde los tests instrumentados. |
| **Entorno de ejecución UI** | Tests instrumentados ejecutados en emulador **`API 34`** (p. ej. *Medium Phone API 34*) para evitar el fallo conocido de Espresso con `InputManager.getInstance` en **API 35/36** preview. |

Los **unit tests** no requieren emulador. Los **instrumentados** sí: debes tener un AVD o móvil y seleccionarlo antes de pulsar Run.

---

### Unit tests (`MainViewModelUnitTest`)

1. Abre el proyecto en **Android Studio** (Ladybug recomendado en el enunciado).
2. Sincroniza Gradle (**File → Sync Project with Gradle Files**).
3. En el archivo `MainViewModelUnitTest.kt`, usa la **flecha verde** junto a la clase o a cada `@Test` → **Run**  
   También puedes ejecutar `./gradlew :app:testDebugUnitTest` desde terminal.
4. **No necesitas emulador** (corren en la JVM del ordenador).

### Instrumented UI tests (`ViewInstrumentedUITest`)

1. Crea y arranca un **Android Virtual Device (AVD)**.  
   Para evitar errores de Espresso con APIs muy nuevas (p. ej. `NoSuchMethodException: InputManager.getInstance`), se recomienda **API 34** (p. ej. *Medium Phone API 34*).
2. Elige ese dispositivo en el selector de ejecución de la barra superior.
3. Abre `ViewInstrumentedUITest.kt` → **Run** sobre la clase o los tests individuales.  
   O Gradle: `./gradlew :app:connectedDebugAndroidTest`.

**Nota:** En emuladores **API 35/36 preview** algunas versiones de Espresso pueden fallar al inicializar (`InputManager`); usando **API 34** los 8 tests de UI ejecutan sin ese problema.

## 🛠️ Instalación

1. Clona el repositorio y abre la carpeta raíz en **Android Studio**.
2. Ejecuta **Sync Project with Gradle Files**.
3. **Unit tests**: Run como se indica arriba (solo JVM).
4. **Tests instrumentados**: levanta un AVD (**API 34** recomendado) y ejecuta los tests de `androidTest`.

## 📦 Requisitos

- Android Studio (Ladybug o compatible)
- JDK 11 (según `compileOptions` del módulo `app`)
- Android SDK configurado (**minSdk 24**, **compileSdk/targetSdk 35** en el proyecto)
- Para instrumentados: **emulador** o **dispositivo físico** con depuración USB

## 📚 Dependencias principales (referencia — `app/build.gradle.kts`)

```kotlin
// App
implementation(platform(libs.androidx.compose.bom))
implementation(libs.androidx.material3)
implementation(libs.androidx.runtime.livedata)
implementation(libs.androidx.activity.compose)

// Unit testing
testImplementation("androidx.arch.core:core-testing:2.2.0")
testImplementation(libs.junit)

// Instrumented UI testing
androidTestImplementation(libs.androidx.junit)
androidTestImplementation(libs.androidx.espresso.core)
androidTestImplementation(platform(libs.androidx.compose.bom))
androidTestImplementation(libs.androidx.ui.test.junit4)

// Debug — Compose previews / manifest de tests UI
debugImplementation(libs.androidx.ui.tooling)
debugImplementation(libs.androidx.ui.test.manifest)
```

## ✅ Características del código verificadas

- Patrón **MVVM** con separación vista / `MainViewModel`
- **LiveData** para exponer estado a la Compose UI
- **testTag** en composables clave para pruebas de UI
- **InstantTaskExecutorRule** en tests unitarios para ejecutar trabajo de LiveData en el mismo hilo
- **`testInstrumentationRunner`**: `androidx.test.runner.AndroidJUnitRunner`

## 👨‍💻 Autor

Alex Jiménez Quiñonero
<br>
Ejercicio académico individual — **M07 - Android Studio** (La Salle).

## 📄 Licencia

Este proyecto forma parte de un ejercicio académico para la asignatura **M07 - Android Studio**.
