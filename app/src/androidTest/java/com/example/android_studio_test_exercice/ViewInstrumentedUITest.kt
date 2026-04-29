package com.example.android_studio_test_exercice

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Pruebas instrumentadas de la interfaz Compose de [MainActivity]:
 * localiza cada nodo con `onNodeWithTag` usando los mismos `testTag`
 * definidos en el composable `MainView`.
 *
 * Requiere emulador o dispositivo físico (no se ejecutan dentro de la JVM de los tests unitarios).
 * Se recomienda API 34 (o similar estable) por compatibilidad con Espresso / Compose UI Test.
 *
 * Cobertura orientativa: estado inicial · Wi‑Fi · checkboxes · TriState · radios ·
 * buscador + mensaje · botón Activado/Desactivado · menú desplegable.
 */
@LargeTest
@RunWith(AndroidJUnit4::class)
class ViewInstrumentedUITest {

    /** Arranca [MainActivity], sincroniza la composición y permite aserciones y acciones de Compose. */
    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    /** Comprueba textos y estados de los toggles iniciales antes de ninguna interacción del usuario. */
    @Test
    fun checkInitialComposableValues() {
        composeTestRule.onNodeWithTag("wifi_switch_id").assertIsOn()
        composeTestRule.onNodeWithTag("checkbox_carnivor_id").assertIsOn()
        composeTestRule.onNodeWithTag("checkbox_carnivor_id").assertIsNotEnabled()
        composeTestRule.onNodeWithTag("checkbox_vegetaria_id").assertIsOff()
        composeTestRule.onNodeWithTag("checkbox_vega_id").assertIsOff()
        composeTestRule.onNodeWithTag("tristate_checkbox_id").assertIsOff()
        composeTestRule.onNodeWithTag("volume_label_id").assertTextContains("Volum: 0%")
        composeTestRule.onNodeWithTag("dropdown_anchor_id").assertTextContains("Opció A")
        composeTestRule.onNodeWithTag("snackbar_message_id").assertDoesNotExist()
        composeTestRule.onNodeWithTag("toggle_state_button_id").assertTextContains("Desactivat")
    }

    /** Simula pulsaciones en el interruptor Wi‑Fi: activado → desactivado → activado. */
    @Test
    fun checkWifiSwitch() {
        composeTestRule.onNodeWithTag("wifi_switch_id").assertIsOn()
        composeTestRule.onNodeWithTag("wifi_switch_id").performClick()
        composeTestRule.onNodeWithTag("wifi_switch_id").assertIsOff()
        composeTestRule.onNodeWithTag("wifi_switch_id").performClick()
        composeTestRule.onNodeWithTag("wifi_switch_id").assertIsOn()
    }

    /** Activa y desactiva los checkboxes de vegetariano y vegano; comprueba on/off tras los clics. */
    @Test
    fun checkCheckboxVegetariaAndVega() {
        composeTestRule.onNodeWithTag("checkbox_vegetaria_id").assertIsOff()
        composeTestRule.onNodeWithTag("checkbox_vegetaria_id").performClick()
        composeTestRule.onNodeWithTag("checkbox_vegetaria_id").assertIsOn()

        composeTestRule.onNodeWithTag("checkbox_vega_id").assertIsOff()
        composeTestRule.onNodeWithTag("checkbox_vega_id").performClick()
        composeTestRule.onNodeWithTag("checkbox_vega_id").assertIsOn()

        composeTestRule.onNodeWithTag("checkbox_vegetaria_id").performClick()
        composeTestRule.onNodeWithTag("checkbox_vegetaria_id").assertIsOff()
    }

    /**
     * Verifica el ciclo del TriState checkbox según el ViewModel:
     * Off → Indeterminate → On → Off.
     */
    @Test
    fun checkTriStateCheckbox() {
        // Off -> Indeterminate -> On -> Off
        composeTestRule.onNodeWithTag("tristate_checkbox_id").assertIsOff()
        composeTestRule.onNodeWithTag("tristate_checkbox_id").performClick()
        // Estado intermedio Indeterminate (no se usa assertIsOn/assertIsOff; se comprueba que el nodo existe)
        composeTestRule.onNodeWithTag("tristate_checkbox_id").assertExists()
        composeTestRule.onNodeWithTag("tristate_checkbox_id").performClick()
        composeTestRule.onNodeWithTag("tristate_checkbox_id").assertIsOn()
        composeTestRule.onNodeWithTag("tristate_checkbox_id").performClick()
        composeTestRule.onNodeWithTag("tristate_checkbox_id").assertIsOff()
    }

    /** El radio de Vinicius permanece deshabilitado; selecciona otros jugadores y comprueba selección / no selección. */
    @Test
    fun checkRadioButtons() {
        composeTestRule.onNodeWithTag("radio_Vinicius_id").assertIsNotEnabled()
        composeTestRule.onNodeWithTag("radio_Lamine_Yamal_id").performClick()
        composeTestRule.onNodeWithTag("radio_Lamine_Yamal_id").assertIsSelected()
        composeTestRule.onNodeWithTag("radio_Raphina_id").performClick()
        composeTestRule.onNodeWithTag("radio_Raphina_id").assertIsSelected()
        composeTestRule.onNodeWithTag("radio_Lamine_Yamal_id").assertIsNotSelected()
    }

    /** Introduce texto en el buscador, pulsa Buscar y aparece el mensaje «Acció completada!». */
    @Test
    fun checkSearchAndSnackbar() {
        composeTestRule.onNodeWithTag("snackbar_message_id").assertDoesNotExist()
        composeTestRule.onNodeWithTag("search_field_id").performTextInput("Android")
        composeTestRule.onNodeWithTag("snackbar_message_id").assertDoesNotExist()
        composeTestRule.onNodeWithTag("search_button_id").performClick()
        composeTestRule.onNodeWithTag("snackbar_message_id").assertIsDisplayed()
        composeTestRule.onNodeWithTag("snackbar_message_id").assertTextContains("Acció completada!")
    }

    /** Alterna el botón de estado (texto por semántica): Desactivat ⇄ Activat. */
    @Test
    fun checkToggleButton() {
        composeTestRule.onNodeWithTag("toggle_state_button_id").assertTextContains("Desactivat")
        composeTestRule.onNodeWithTag("toggle_state_button_id").performClick()
        composeTestRule.onNodeWithTag("toggle_state_button_id").assertTextContains("Activat")
        composeTestRule.onNodeWithTag("toggle_state_button_id").performClick()
        composeTestRule.onNodeWithTag("toggle_state_button_id").assertTextContains("Desactivat")
    }

    /** Abre el desplegable desde el ancla, elige Opción B y C; comprueba el texto mostrado. */
    @Test
    fun checkDropdownMenu() {
        composeTestRule.onNodeWithTag("dropdown_anchor_id").assertTextContains("Opció A")
        composeTestRule.onNodeWithTag("dropdown_anchor_id").performClick()
        composeTestRule.onNodeWithTag("dropdown_option_Opció_B_id").performClick()
        composeTestRule.onNodeWithTag("dropdown_anchor_id").assertTextContains("Opció B")
        composeTestRule.onNodeWithTag("dropdown_anchor_id").performClick()
        composeTestRule.onNodeWithTag("dropdown_option_Opció_C_id").performClick()
        composeTestRule.onNodeWithTag("dropdown_anchor_id").assertTextContains("Opció C")
    }
}
