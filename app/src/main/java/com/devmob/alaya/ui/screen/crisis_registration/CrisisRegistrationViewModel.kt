package com.devmob.alaya.ui.screen.crisis_registration

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devmob.alaya.domain.SaveCrisisRegistrationUseCase
import com.devmob.alaya.domain.model.CrisisBodySensation
import com.devmob.alaya.domain.model.CrisisEmotion
import com.devmob.alaya.domain.model.CrisisPlace
import com.devmob.alaya.domain.model.CrisisTimeDetails
import com.devmob.alaya.domain.model.CrisisTool
import com.devmob.alaya.domain.model.FirebaseResult
import com.devmob.alaya.domain.model.Intensity
import com.devmob.alaya.domain.model.util.toDB
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Date

class CrisisRegistrationViewModel(
    private val saveCrisisRegistrationUseCase: SaveCrisisRegistrationUseCase
) : ViewModel() {

    private val _screenState = MutableLiveData(CrisisRegistrationScreenState())
    val screenState: LiveData<CrisisRegistrationScreenState> = _screenState
    private val _places = MutableLiveData<List<CrisisPlace>>()
    val places: LiveData<List<CrisisPlace>> get() = _places
    private val _tools = MutableLiveData<List<CrisisTool>>()
    val tools: LiveData<List<CrisisTool>> get() = _tools
    private val _bodySensations = MutableLiveData<List<CrisisBodySensation>>()
    val bodySensations: LiveData<List<CrisisBodySensation>> get() = _bodySensations
    private val _emotions = MutableLiveData<List<CrisisEmotion>>()
    val emotions: LiveData<List<CrisisEmotion>> get() = _emotions
    var shouldGoToBack by mutableStateOf(true)
    var shouldGoToSummary by mutableStateOf(false)

    init {
        loadPlaces()
        loadTools()
        loadBodySensations()
        loadEmotions()
        loadLastCrisisDetails()
    }


    var shouldShowExitModal by mutableStateOf(false)

    private fun loadLastCrisisDetails() {
        viewModelScope.launch(Dispatchers.Main) {
            val result = saveCrisisRegistrationUseCase.getLastCrisisDetails()

            if (result?.completed == false) {
                val startTime = result.start
                val endTime = result.end
                if (startTime != null && endTime != null) {
                    val crisisTimeDetails = CrisisTimeDetails(
                        startTime = startTime,
                        endTime = endTime
                    )
                    _screenState.value = _screenState.value?.copy(
                        crisisDetails = _screenState.value!!.crisisDetails.copy(
                            crisisTimeDetails = crisisTimeDetails
                        )
                    )
                }
                val availableTools = GridElementsRepository.returnAvailableTools()

                val selectedCrisisTools = result.tools.mapNotNull { toolId ->
                    availableTools.find { it.id == toolId }
                }
                _tools.value = availableTools
                _screenState.value = _screenState.value?.copy(
                    crisisDetails = _screenState.value!!.crisisDetails.copy(
                        toolList = selectedCrisisTools
                    )
                )
            } else {
                _screenState.value = _screenState.value?.copy(
                    crisisDetails = _screenState.value!!.crisisDetails.copy(
                        crisisTimeDetails = CrisisTimeDetails()
                    )
                )
                _screenState.value = _screenState.value?.copy(
                    crisisDetails = _screenState.value!!.crisisDetails.copy(
                        toolList = emptyList()
                    )
                )
            }
        }
    }

    fun cleanState() {
        _screenState.value = CrisisRegistrationScreenState()
        shouldGoToBack = true
        shouldGoToSummary = false
        shouldShowExitModal = false
    }

    fun goOneStepForward() {
        _screenState.value =
            _screenState.value?.copy(currentStep = this._screenState.value?.currentStep!!.plus(1))
    }

    fun goOneStepBack() {
        _screenState.value =
            _screenState.value?.copy(currentStep = this._screenState.value?.currentStep!!.minus(1))
    }

    fun updateStep(step: Int) {
        _screenState.value = _screenState.value?.copy(currentStep = step)
        hideBackButton()
    }

    fun addCrisisPlace(place: CrisisPlace) {
        val currentPlaces = _places.value?.toMutableList() ?: mutableListOf()
        if (!currentPlaces.any { it.name == place.name }) {
            currentPlaces.add(place)
            _places.value = currentPlaces
        }
        updatePlace(place,0)
    }

    fun addCrisisBodySensation(bodySensation: CrisisBodySensation) {
        val currentSensations = _bodySensations.value?.toMutableList() ?: mutableListOf()
        if (!currentSensations.any { it.name == bodySensation.name }) {
            currentSensations.add(bodySensation)
            _bodySensations.value = currentSensations
        }
        selectCrisisBodySensation(bodySensation)
    }

    fun unselectCrisisBodySensation(bodySensation: CrisisBodySensation) {
        val currentState = _screenState.value ?: return
        val updatedBodySensationList =
            currentState.crisisDetails.bodySensationList.toMutableList().apply {
                if (any { it.name == bodySensation.name }) {
                    removeIf { it.name == bodySensation.name }
                }
            }
        updateStateBodySensationList(currentState, updatedBodySensationList)
    }

    fun selectCrisisBodySensation(bodySensation: CrisisBodySensation) {
        val currentState = _screenState.value ?: return
        val updatedBodySensationList =
            currentState.crisisDetails.bodySensationList.toMutableList().apply {
                if (!any { it.name == bodySensation.name }) {
                    add(bodySensation)
                }
            }
        updateStateBodySensationList(currentState, updatedBodySensationList)
    }

    fun updateIntensityBodySensation(bodySensation: CrisisBodySensation, intensity: Intensity) {
        val currentState = _screenState.value ?: return
        val updatedBodySensationList =
            currentState.crisisDetails.bodySensationList.toMutableList().apply {
                if (any { it.name == bodySensation.name }) {
                    removeIf { it.name == bodySensation.name }
                }
                add(bodySensation.copy(intensity = intensity))
            }
        updateStateBodySensationList(currentState, updatedBodySensationList)
    }

    private fun updateStateBodySensationList(
        currentState: CrisisRegistrationScreenState,
        updatedBodySensationList: MutableList<CrisisBodySensation>
    ) {
        _screenState.value = currentState.copy(
            crisisDetails = currentState.crisisDetails.copy(
                bodySensationList = updatedBodySensationList
            )
        )
    }

    fun unselectCrisisEmotion(emotion: CrisisEmotion) {
        val currentState = _screenState.value ?: return
        val updatedEmotionList =
            currentState.crisisDetails.emotionList.toMutableList().apply {
                if (any { it.name == emotion.name }) {
                    removeIf { it.name == emotion.name }
                }
            }
        updateStateEmotionList(currentState, updatedEmotionList)
    }

    fun selectCrisisEmotion(emotion: CrisisEmotion) {
        val currentState = _screenState.value ?: return
        val updatedEmotionList =
            currentState.crisisDetails.emotionList.toMutableList().apply {
                if (!any { it.name == emotion.name }) {
                    add(emotion)
                }
            }
        updateStateEmotionList(currentState, updatedEmotionList)
    }

    fun selectCrisisTool(tool: CrisisTool) {
        val currentState = _screenState.value ?: return
        val updatedToolList =
            currentState.crisisDetails.toolList.toMutableList().apply {
                if (!any { it.name == tool.name }) {
                    add(tool)
                }
            }
        updateStateToolList(currentState, updatedToolList)
    }

    fun updateIntensityEmotion(crisisEmotion: CrisisEmotion, intensity: Intensity) {
        val currentState = _screenState.value ?: return
        val updatedEmotionList =
            currentState.crisisDetails.emotionList.toMutableList().apply {
                if (any { it.name == crisisEmotion.name }) {
                    removeIf { it.name == crisisEmotion.name }
                }
                add(crisisEmotion.copy(intensity = intensity))
            }
        updateStateEmotionList(currentState, updatedEmotionList)
    }

    private fun updateStateEmotionList(
        currentState: CrisisRegistrationScreenState,
        updatedEmotionList: MutableList<CrisisEmotion>
    ) {
        _screenState.value = currentState.copy(
            crisisDetails = currentState.crisisDetails.copy(
                emotionList = updatedEmotionList
            )
        )
    }

    private fun updateStateToolList(
        currentState: CrisisRegistrationScreenState,
        updatedToolList: MutableList<CrisisTool>
    ) {
        _screenState.value = currentState.copy(
            crisisDetails = currentState.crisisDetails.copy(
                toolList = updatedToolList
            )
        )
    }
    fun unselectCrisisTool(tool: CrisisTool) {
        val currentState = _screenState.value ?: return
        val updatedToolList =
            currentState.crisisDetails.toolList.toMutableList().apply {
                if (any { it.name == tool.name }) {
                    removeIf { it.name == tool.name }
                }
            }
        updateStateToolList(currentState, updatedToolList)
    }

    fun addCrisisTool(crisisTool: CrisisTool) {
        val currentTools = _tools.value?.toMutableList() ?: mutableListOf()
        if (!currentTools.any { it.name == crisisTool.name }) {
            currentTools.add(crisisTool)
            _tools.value = currentTools
        }
        selectCrisisTool(crisisTool)
    }

    fun addCrisisEmotion(crisisEmotion: CrisisEmotion) {
        val currentEmotions = _emotions.value?.toMutableList() ?: mutableListOf()
        if (!currentEmotions.any { it.name == crisisEmotion.name }) {
            currentEmotions.add(crisisEmotion)
            _emotions.value = currentEmotions
        }
        selectCrisisEmotion(crisisEmotion)
    }

    fun updateNotes(text: String) {
        _screenState.value = _screenState.value?.crisisDetails?.copy(
            notes = text
        )?.let {
            _screenState.value?.copy(
                crisisDetails = it
            )
        }
    }

    fun updatePlace(place: CrisisPlace, index: Int) {
        val newPlaceList = mutableListOf<CrisisPlace>().apply {
            add(index, place)
        }

        _screenState.value = _screenState.value?.crisisDetails?.copy(
            placeList = newPlaceList
        )?.let {
            _screenState.value?.copy(
                crisisDetails = it
            )
        }
    }

    fun clearPlaceSelection() {
        if (_screenState.value?.crisisDetails?.placeList.isNullOrEmpty()) return
        _screenState.value = _screenState.value?.crisisDetails?.copy(
            placeList = mutableListOf()
        )?.let {
            _screenState.value?.copy(
                crisisDetails = it
            )
        }
    }

    fun dismissExitModal() {
        shouldShowExitModal = false
    }

    fun showExitModal() {
        shouldShowExitModal = true
    }

    fun updateStartDate(date: Date) {
        val updatedCrisisTimeDetails = _screenState.value?.crisisDetails?.crisisTimeDetails?.copy(
            startTime = date
        )
        updateScreenStateCrisisDetails(updatedCrisisTimeDetails)
    }

    fun updateEndDate(date: Date) {
        val updatedCrisisTimeDetails = _screenState.value?.crisisDetails?.crisisTimeDetails?.copy(
            endTime = date
        )
        updateScreenStateCrisisDetails(updatedCrisisTimeDetails)
    }

    private fun updateScreenStateCrisisDetails(updatedCrisisTimeDetails: CrisisTimeDetails?) {
        _screenState.value = updatedCrisisTimeDetails?.let { crisisTimeDetails ->
            _screenState.value?.crisisDetails?.copy(
                crisisTimeDetails = crisisTimeDetails
            )?.let { crisisDetails ->
                _screenState.value?.copy(
                    crisisDetails = crisisDetails
                )
            }
        }
    }

    fun hideBackButton() {
        shouldGoToBack = false
        shouldGoToSummary = true
    }

    private fun loadPlaces() {
        _places.value = GridElementsRepository.returnAvailablePlaces()
    }

    private fun loadTools() {
        _tools.value = GridElementsRepository.returnAvailableTools()
    }

    private fun loadBodySensations() {
        _bodySensations.value = GridElementsRepository.returnAvailableBodySensations()
    }

    private fun loadEmotions() {
        _emotions.value = GridElementsRepository.returnAvailableEmotions()
    }

    fun saveRegister() {
        val crisis = _screenState.value?.crisisDetails?.toDB()
        viewModelScope.launch {
            val lastCrisis = saveCrisisRegistrationUseCase.getLastCrisisDetails()
            if (lastCrisis != null && !lastCrisis.completed) {
                val updatedCrisis = crisis?.copy(completed = true)
                val response = updatedCrisis?.let {
                    saveCrisisRegistrationUseCase.updateCrisisDetails(
                        it
                    )
                }

                when (response) {
                    is FirebaseResult.Success -> {
                        Log.d(
                            "CrisisRegistrationViewModel",
                            "Registro actualizado exitosamente"
                        )
                    }

                    is FirebaseResult.Error -> {
                        Log.d("CrisisRegistrationViewModel", "Error al actualizar el registro")
                    }

                    null -> {
                        Log.d("CrisisRegistrationViewModel", "Respuesta null al actualizar")
                    }
                }
            } else {
                // Si no se encontró crisis o la última está completa, creo nuevo registro
                val crisisToSave = crisis?.copy(completed = true)
                val response = crisisToSave?.let { saveCrisisRegistrationUseCase.invoke(it) }

                when (response) {
                    is FirebaseResult.Success -> {
                        Log.d("CrisisRegistrationViewModel", "Nuevo registro guardado exitosamente")
                    }

                    is FirebaseResult.Error -> {
                        Log.d("CrisisRegistrationViewModel", "Error al guardar el nuevo registro")
                    }

                    null -> {
                        Log.d(
                            "CrisisRegistrationViewModel",
                            "Respuesta null al guardar el nuevo registro"
                        )
                    }
                }
            }
        }
    }
}
