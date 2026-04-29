package com.example.android_studio_test_exercice

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.compose.ui.state.ToggleableState
import com.example.android_studio_test_exercice.viewmodel.MainViewModel
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * Tests unitarios (JUnit) del [MainViewModel].
 *
 * Corre en la JVM (`src/test`): no necesita Android ni emulador.
 * [InstantTaskExecutorRule] fuerza ejecución síncrona del hilado donde LiveData ejecuta callbacks,
 * de modo que el valor observable de LiveData se puede afirmar en el mismo hilo del test.
 *
 * Agrupa las pruebas por método público del ViewModel y por valores iniciales.
 */
 
class MainViewModelUnitTest {

    /** Necesario con LiveData en tests JVM: las tareas de arch se ejecutan en el mismo hilo que el test. */
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: MainViewModel

    /** Crea un [MainViewModel] nuevo antes de cada método de prueba; evita fugas de estado entre tests. */
    @Before
    fun setUp() {
        viewModel = MainViewModel()
    }

    // ─── Estado inicial (tras el constructor) ───────────────────

    /** Comprueba los valores por defecto de cada estado expuesto como LiveData. */
    @Test
    fun checkInitialValues() {
        Assert.assertEquals(true, viewModel.estatSwitch.value)
        Assert.assertEquals(false, viewModel.esVegetaria.value)
        Assert.assertEquals(false, viewModel.esVega.value)
        Assert.assertEquals(true, viewModel.esCarnivor.value)
        Assert.assertEquals(ToggleableState.Off, viewModel.triStateStatus.value)
        Assert.assertEquals("Messi", viewModel.selectedOption.value)
        Assert.assertEquals(0f, viewModel.sliderValue.value)
        Assert.assertEquals(false, viewModel.expanded.value)
        Assert.assertEquals("Opció A", viewModel.selectedItem.value)
        Assert.assertEquals("", viewModel.searchText.value)
        Assert.assertEquals(false, viewModel.showSnackbar.value)
        Assert.assertEquals(false, viewModel.toggleState.value)
    }

    // ─── toggleEstatSwitch (interruptor Wi‑Fi) ─────────────────

    /** De `true` inicial a `false` con una sola llamada. */
    @Test
    fun toggleEstatSwitch_fromTrue_returnsFalse() {
        Assert.assertEquals(true, viewModel.estatSwitch.value)
        viewModel.toggleEstatSwitch()
        Assert.assertEquals(false, viewModel.estatSwitch.value)
    }

    /** Dos toggles consecutivos restauran el valor inicial. */
    @Test
    fun toggleEstatSwitch_twiceRestoresValue() {
        viewModel.toggleEstatSwitch()
        viewModel.toggleEstatSwitch()
        Assert.assertEquals(true, viewModel.estatSwitch.value)
    }

    // ─── toggleEsCarnivor ──────────────────────────────────────

    /** De `true` inicial a `false` con una sola llamada. */
    @Test
    fun toggleEsCarnivor_fromTrue_returnsFalse() {
        Assert.assertEquals(true, viewModel.esCarnivor.value)
        viewModel.toggleEsCarnivor()
        Assert.assertEquals(false, viewModel.esCarnivor.value)
    }

    /** Dos toggles consecutivos restauran el valor inicial. */
    @Test
    fun toggleEsCarnivor_twiceRestoresValue() {
        viewModel.toggleEsCarnivor()
        viewModel.toggleEsCarnivor()
        Assert.assertEquals(true, viewModel.esCarnivor.value)
    }

    // ─── toggleEsVegetaria ─────────────────────────────────────

    /** De `false` inicial a `true` con una sola llamada. */
    @Test
    fun toggleEsVegetaria_fromFalse_returnsTrue() {
        Assert.assertEquals(false, viewModel.esVegetaria.value)
        viewModel.toggleEsVegetaria()
        Assert.assertEquals(true, viewModel.esVegetaria.value)
    }

    /** Dos toggles consecutivos restauran el valor inicial. */
    @Test
    fun toggleEsVegetaria_twiceRestoresValue() {
        viewModel.toggleEsVegetaria()
        viewModel.toggleEsVegetaria()
        Assert.assertEquals(false, viewModel.esVegetaria.value)
    }

    // ─── toggleEsVega ──────────────────────────────────────────

    /** De `false` inicial a `true` con una sola llamada. */
    @Test
    fun toggleEsVega_fromFalse_returnsTrue() {
        Assert.assertEquals(false, viewModel.esVega.value)
        viewModel.toggleEsVega()
        Assert.assertEquals(true, viewModel.esVega.value)
    }

    /** Dos toggles consecutivos restauran el valor inicial. */
    @Test
    fun toggleEsVega_twiceRestoresValue() {
        viewModel.toggleEsVega()
        viewModel.toggleEsVega()
        Assert.assertEquals(false, viewModel.esVega.value)
    }

    // ─── setSelectedOption (jugador Pilota d'Or) ───────────────

    /** Asigna el jugador seleccionado y queda reflejado en [MainViewModel.selectedOption]. */
    @Test
    fun setSelectedOption_setsCorrectValue() {
        viewModel.setSelectedOption("Lamine Yamal")
        Assert.assertEquals("Lamine Yamal", viewModel.selectedOption.value)
    }

    /** La segunda llamada sobrescribe el valor anterior. */
    @Test
    fun setSelectedOption_overwritesPreviousValue() {
        viewModel.setSelectedOption("Raphina")
        viewModel.setSelectedOption("Lamine Yamal")
        Assert.assertEquals("Lamine Yamal", viewModel.selectedOption.value)
    }

    // ─── toggleTriStateStatus ──────────────────────────────────

    /** Primer toggle: Off → Indeterminate (alineado con el `when` del ViewModel). */
    @Test
    fun toggleTriStateStatus_offToIndeterminate() {
        Assert.assertEquals(ToggleableState.Off, viewModel.triStateStatus.value)
        viewModel.toggleTriStateStatus()
        Assert.assertEquals(ToggleableState.Indeterminate, viewModel.triStateStatus.value)
    }

    /** Segundo toggle: Indeterminate → On. */
    @Test
    fun toggleTriStateStatus_indeterminateToOn() {
        viewModel.toggleTriStateStatus() // Off → Indeterminate
        viewModel.toggleTriStateStatus() // Indeterminate → On
        Assert.assertEquals(ToggleableState.On, viewModel.triStateStatus.value)
    }

    /** Tercer toggle: On → Off. */
    @Test
    fun toggleTriStateStatus_onToOff() {
        viewModel.toggleTriStateStatus() // Off → Indeterminate
        viewModel.toggleTriStateStatus() // Indeterminate → On
        viewModel.toggleTriStateStatus() // On → Off
        Assert.assertEquals(ToggleableState.Off, viewModel.triStateStatus.value)
    }

    /** Tres toggles seguidos vuelven a dejar el ciclo en Off. */
    @Test
    fun toggleTriStateStatus_fullCycleRestoresOff() {
        repeat(3) { viewModel.toggleTriStateStatus() }
        Assert.assertEquals(ToggleableState.Off, viewModel.triStateStatus.value)
    }

    // ─── setSliderValue (volumen 0–100) ────────────────────────

    /** Actualiza el porcentaje de volumen a un valor intermedio. */
    @Test
    fun setSliderValue_setsCorrectValue() {
        viewModel.setSliderValue(75f)
        Assert.assertEquals(75f, viewModel.sliderValue.value)
    }

    /** Fija volumen explícitamente en 0. */
    @Test
    fun setSliderValue_zero() {
        viewModel.setSliderValue(0f)
        Assert.assertEquals(0f, viewModel.sliderValue.value)
    }

    /** Permite volúmen máximo según UI (0–100). */
    @Test
    fun setSliderValue_maxValue() {
        viewModel.setSliderValue(100f)
        Assert.assertEquals(100f, viewModel.sliderValue.value)
    }

    // ─── setExpanded (menú desplegable) ───────────────────────

    /** Marca desplegable visible u oculto (true/false). */
    @Test
    fun setExpanded_true() {
        viewModel.setExpanded(true)
        Assert.assertEquals(true, viewModel.expanded.value)
    }

    /** Primero abierto y luego cerrado debe quedar en false. */
    @Test
    fun setExpanded_false() {
        viewModel.setExpanded(true)
        viewModel.setExpanded(false)
        Assert.assertEquals(false, viewModel.expanded.value)
    }

    // ─── setSelectedItem (opciones A/B/C) ──────────────────────

    /** Actualiza la opción mostrada como texto del desplegable. */
    @Test
    fun setSelectedItem_setsCorrectValue() {
        viewModel.setSelectedItem("Opció B")
        Assert.assertEquals("Opció B", viewModel.selectedItem.value)
    }

    /** Sobrescribe el ítem cuando se elige otro. */
    @Test
    fun setSelectedItem_overwritesPreviousValue() {
        viewModel.setSelectedItem("Opció B")
        viewModel.setSelectedItem("Opció C")
        Assert.assertEquals("Opció C", viewModel.selectedItem.value)
    }

    // ─── setSearchText ─────────────────────────────────────────

    /** El texto de búsqueda queda almacenado en LiveData. */
    @Test
    fun setSearchText_setsCorrectValue() {
        viewModel.setSearchText("Android")
        Assert.assertEquals("Android", viewModel.searchText.value)
    }

    /** Cadena vacía borra el contenido previo. */
    @Test
    fun setSearchText_emptyString() {
        viewModel.setSearchText("Android")
        viewModel.setSearchText("")
        Assert.assertEquals("", viewModel.searchText.value)
    }

    // ─── performSearch (dispara feedback tipo snackbar) ───────

    /** Tras buscar, el flag de mensaje pasa a true. */
    @Test
    fun performSearch_setsShowSnackbarTrue() {
        Assert.assertEquals(false, viewModel.showSnackbar.value)
        viewModel.performSearch()
        Assert.assertEquals(true, viewModel.showSnackbar.value)
    }

    /** Llamadas repetidas mantienen el flag en true. */
    @Test
    fun performSearch_calledTwiceStaysTrue() {
        viewModel.performSearch()
        viewModel.performSearch()
        Assert.assertEquals(true, viewModel.showSnackbar.value)
    }

    // ─── toggle (botón Activado / Desactivado) ────────────────

    /** De false inicial a true con un solo toggle. */
    @Test
    fun toggle_fromFalse_returnsTrue() {
        Assert.assertEquals(false, viewModel.toggleState.value)
        viewModel.toggle()
        Assert.assertEquals(true, viewModel.toggleState.value)
    }

    /** Dos toggles vuelven al estado inicial. */
    @Test
    fun toggle_twiceRestoresValue() {
        viewModel.toggle()
        viewModel.toggle()
        Assert.assertEquals(false, viewModel.toggleState.value)
    }
}
