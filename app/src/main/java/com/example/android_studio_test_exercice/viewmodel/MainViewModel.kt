package com.example.android_studio_test_exercice.viewmodel

import androidx.compose.ui.state.ToggleableState
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

/**
 * Capa ViewModel (MVVM): estado reactivo con [MutableLiveData] expuesto como [LiveData] de solo lectura para la vista.
 *
 * Respecto al repositorio base se añadió:
 * - Propiedades y valores iniciales para volumen (slider), menú desplegable (`expanded`, `selectedItem`),
 *   buscador (`searchText`), feedback al buscar (`showSnackbar`) y botón Activado/Desactivado (`toggleState`).
 * - Métodos públicos adicionales (`toggleEsVegetaria`, `toggleEsVega`, `setSelectedOption` y setters/toggles
 *   asociados), comprobables con los tests unitarios (`MainViewModelUnitTest`).
 */
class MainViewModel : ViewModel {
    // Atributos
    private val _estatSwitch: MutableLiveData<Boolean>
    public val estatSwitch: LiveData<Boolean>

    private val _esVegetaria: MutableLiveData<Boolean>
    public val esVegetaria: LiveData<Boolean>

    private val _esVega: MutableLiveData<Boolean>
    public val esVega: LiveData<Boolean>

    private val _esCarnivor: MutableLiveData<Boolean>
    public val esCarnivor: LiveData<Boolean>

    private val _triStateStatus: MutableLiveData<ToggleableState>
    public val triStateStatus: LiveData<ToggleableState>

    private val _selectedOption: MutableLiveData<String>
    public val selectedOption: LiveData<String>

    // Añadido para completar la práctica (volumen, menú desplegable, búsqueda, snackbar, toggle)
    private val _sliderValue: MutableLiveData<Float>
    public val sliderValue: LiveData<Float>

    private val _expanded: MutableLiveData<Boolean>
    public val expanded: LiveData<Boolean>

    private val _selectedItem: MutableLiveData<String>
    public val selectedItem: LiveData<String>

    private val _searchText: MutableLiveData<String>
    public val searchText: LiveData<String>

    private val _showSnackbar: MutableLiveData<Boolean>
    public val showSnackbar: LiveData<Boolean>

    private val _toggleState: MutableLiveData<Boolean>
    public val toggleState: LiveData<Boolean>

    /**
     * Inicializa todos los [MutableLiveData] con valores por defecto coherentes con [MainView] (observeAsState).
     */
    constructor() : super() {
        this._estatSwitch = MutableLiveData<Boolean>(true)
        this.estatSwitch = this._estatSwitch

        this._esVegetaria = MutableLiveData<Boolean>(false)
        this.esVegetaria = this._esVegetaria

        this._esVega = MutableLiveData<Boolean>(false)
        this.esVega = this._esVega

        this._esCarnivor = MutableLiveData<Boolean>(true)
        this.esCarnivor = this._esCarnivor

        this._triStateStatus = MutableLiveData<ToggleableState>(ToggleableState.Off)
        this.triStateStatus = this._triStateStatus

        this._selectedOption = MutableLiveData<String>("Messi")
        this.selectedOption = this._selectedOption

        this._sliderValue = MutableLiveData<Float>(0f)
        this.sliderValue = this._sliderValue

        this._expanded = MutableLiveData<Boolean>(false)
        this.expanded = this._expanded

        this._selectedItem = MutableLiveData<String>("Opció A")
        this.selectedItem = this._selectedItem

        this._searchText = MutableLiveData<String>("")
        this.searchText = this._searchText

        this._showSnackbar = MutableLiveData<Boolean>(false)
        this.showSnackbar = this._showSnackbar

        this._toggleState = MutableLiveData<Boolean>(false)
        this.toggleState = this._toggleState
    }

    // --- Wi‑Fi y opciones de menú (checkboxes) ---

    fun toggleEstatSwitch(){
        this._estatSwitch.value = !(this._estatSwitch.value)!!
    }

    fun toggleEsCarnivor(){
        this._esCarnivor.value = !(this._esCarnivor.value)!!
    }

    fun toggleEsVegetaria(){
        this._esVegetaria.value = !(this._esVegetaria.value)!!
    }
    fun toggleEsVega() {
        this._esVega.value = !(this._esVega.value)!!
    }

    fun setSelectedOption(option: String) {
        this._selectedOption.value = option
    }

    fun toggleTriStateStatus(){
        when(this._triStateStatus.value){
            ToggleableState.On -> setTriStateStatus(ToggleableState.Off)
            ToggleableState.Off -> setTriStateStatus(ToggleableState.Indeterminate)
            ToggleableState.Indeterminate -> setTriStateStatus(ToggleableState.On)
            null -> setTriStateStatus(ToggleableState.On)
        }
    }

    private fun setTriStateStatus(triState: ToggleableState){
        this._triStateStatus.value = triState
    }

    // --- Volumen, dropdown, buscador y botón de estado (completados en la práctica) ---

    fun setSliderValue(value: Float) {
        this._sliderValue.value = value
    }

    fun setExpanded(value: Boolean) {
        this._expanded.value = value
    }

    fun setSelectedItem(value: String) {
        this._selectedItem.value = value
    }

    fun setSearchText(value: String) {
        this._searchText.value = value
    }

    fun performSearch() {
        this._showSnackbar.value = true
    }

    fun toggle() {
        this._toggleState.value = !(this._toggleState.value)!!
    }
}