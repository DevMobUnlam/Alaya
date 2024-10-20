package com.devmob.alaya.ui.screen.crisis_registration

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.devmob.alaya.domain.model.CrisisBodySensation
import com.devmob.alaya.domain.model.CrisisEmotion
import com.devmob.alaya.domain.model.CrisisPlace
import com.devmob.alaya.domain.model.CrisisRegisterData
import com.devmob.alaya.domain.model.CrisisTimeDetails
import com.devmob.alaya.domain.model.CrisisTool
import com.devmob.alaya.domain.model.Intensity
import java.time.LocalDate
import java.time.LocalTime
import java.util.Date

class CrisisRegistrationViewModel() : ViewModel() {

    var registrationData = mutableStateOf(CrisisRegisterData())
        private set

    fun updateRegistrationData(data: CrisisRegisterData) {
        registrationData.value = data
    }

    private val _screenState = MutableLiveData(CrisisRegistrationScreenState())
    val screenState: LiveData<CrisisRegistrationScreenState> = _screenState
    var startDate by mutableStateOf<LocalDate?>(null)
    var startTime by mutableStateOf<LocalTime?>(null)
    var endDate by mutableStateOf<LocalDate?>(null)
    var endTime by mutableStateOf<LocalTime?>(null)
    private val _places = MutableLiveData<List<CrisisPlace>>()
    val places: LiveData<List<CrisisPlace>> get() = _places
    private val _tools = MutableLiveData<List<CrisisTool>>()
    val tools: LiveData<List<CrisisTool>> get() = _tools
    private val _bodySensations = MutableLiveData<List<CrisisBodySensation>>()
    val bodySensations: LiveData<List<CrisisBodySensation>> get() = _bodySensations
    private val _emotions = MutableLiveData<List<CrisisEmotion>>()
    val emotions: LiveData<List<CrisisEmotion>> get() = _emotions


    init {
        loadPlaces()
        loadTools()
        loadBodySensations()
        loadEmotions()
    }

    var shouldShowExitModal by mutableStateOf(false)

    fun cleanState() {
        _screenState.value = CrisisRegistrationScreenState()
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
    }

    fun addCrisisPlace(place: CrisisPlace) {
        val currentPlaces = _places.value?.toMutableList() ?: mutableListOf()
        if (!currentPlaces.any { it.name == place.name }) {
            currentPlaces.add(place)
            _places.value = currentPlaces
            _screenState.value = _screenState.value?.copy(
                crisisDetails = _screenState.value!!.crisisDetails.copy(
                    placeList = currentPlaces
                )
            )
        }
    }

    fun addCrisisBodySensation(bodySensation: CrisisBodySensation) {
        val currentSensations = _bodySensations.value?.toMutableList() ?: mutableListOf()
        if (!currentSensations.any { it.name == bodySensation.name }) {
            currentSensations.add(bodySensation)
            _bodySensations.value = currentSensations
            _screenState.value = _screenState.value?.copy(
                crisisDetails = _screenState.value!!.crisisDetails.copy(
                    bodySensationList = currentSensations
                )
            )
        }
    }

    fun updateCrisisBodySensation(bodySensation: CrisisBodySensation) {
        val currentState = _screenState.value ?: return
        val updatedBodySensationList =
            currentState.crisisDetails.bodySensationList.toMutableList().apply {
                if (any { it.name == bodySensation.name }) {
                    removeIf { it.name == bodySensation.name }
                } else {
                    add(bodySensation)
                }
            }

        _screenState.value = currentState.copy(
            crisisDetails = currentState.crisisDetails.copy(
                bodySensationList = updatedBodySensationList
            )
        )
    }

    fun updateCrisisEmotion(emotion: CrisisEmotion) {
        val currentState = _screenState.value ?: return
        val updatedEmotionList = currentState.crisisDetails.emotionList.toMutableList().apply {
            if (any { it.name == emotion.name }) {
                removeIf { it.name == emotion.name }
            } else {
                add(emotion)
            }
        }

        _screenState.value = currentState.copy(
            crisisDetails = currentState.crisisDetails.copy(
                emotionList = updatedEmotionList
            )
        )
    }

    fun updateCrisisTool(tool: CrisisTool) {
        val currentState = _screenState.value ?: return
        val updatedToolList = currentState.crisisDetails.toolList.toMutableList().apply {
            if (any { it.name == tool.name }) {
                removeIf { it.name == tool.name }
            } else {
                add(tool)
            }
        }

        _screenState.value = currentState.copy(
            crisisDetails = currentState.crisisDetails.copy(
                toolList = updatedToolList
            )
        )
    }

    fun onBodySensationIntensityChange(
        intensity: Intensity,
        index: Int,
        bodySensation: CrisisBodySensation
    ) {
        val updatedBodySensation = bodySensation.copy(intensity = intensity)
        _screenState.value?.crisisDetails?.bodySensationList?.set(index, updatedBodySensation)
    }

    fun onEmotionIntensityChange(intensity: Intensity, index: Int, emotion: CrisisEmotion) {
        val updatedEmotion = emotion.copy(intensity = intensity)
        _screenState.value?.crisisDetails?.emotionList?.set(index, updatedEmotion)
    }

    fun addCrisisTool(crisisTool: CrisisTool) {
        val currentTools = _tools.value?.toMutableList() ?: mutableListOf()
        if (!currentTools.any { it.name == crisisTool.name }) {
            currentTools.add(crisisTool)
            _tools.value = currentTools
            _screenState.value = _screenState.value?.copy(
                crisisDetails = _screenState.value!!.crisisDetails.copy(
                    toolList = currentTools
                )
            )
        }
    }

    fun addCrisisEmotion(crisisEmotion: CrisisEmotion) {
        val currentEmotions = _emotions.value?.toMutableList() ?: mutableListOf()
        if (!currentEmotions.any { it.name == crisisEmotion.name }) {
            currentEmotions.add(crisisEmotion)
            _emotions.value = currentEmotions
            _screenState.value = _screenState.value?.copy(
                crisisDetails = _screenState.value!!.crisisDetails.copy(
                    emotionList = currentEmotions
                )
            )
        }
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

    fun updatePlaceStatus(place: CrisisPlace, index: Int) {
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

    fun dismissExitModal() {
        shouldShowExitModal = false
    }

    fun showExitModal() {
        shouldShowExitModal = true
    }

    private fun updateCrisisTimeDetails(updatedDetails: CrisisTimeDetails) {
        _screenState.value = _screenState.value?.crisisDetails?.copy(
            crisisTimeDetails = updatedDetails
        )?.let {
            _screenState.value?.copy(
                crisisDetails = it
            )
        }
    }

    fun updateStartDate(date: Date) {
        val updatedCrisisTimeDetails = _screenState.value?.crisisDetails?.crisisTimeDetails?.copy(
            startingDate = date
        )

        _screenState.value = _screenState.value?.copy(
            crisisDetails = _screenState.value?.crisisDetails?.copy(
                crisisTimeDetails = updatedCrisisTimeDetails!!
            )!!
        )
    }

    fun updateStartTime(time: Date) {
        val updatedCrisisTimeDetails = _screenState.value?.crisisDetails?.crisisTimeDetails?.copy(
            startTIme = time
        )

        _screenState.value = _screenState.value?.copy(
            crisisDetails = _screenState.value?.crisisDetails?.copy(
                crisisTimeDetails = updatedCrisisTimeDetails!!
            )!!
        )
    }

    fun updateEndDate(date: Date) {
        val updatedCrisisTimeDetails = _screenState.value?.crisisDetails?.crisisTimeDetails?.copy(
            endDate = date
        )

        _screenState.value = _screenState.value?.copy(
            crisisDetails = _screenState.value?.crisisDetails?.copy(
                crisisTimeDetails = updatedCrisisTimeDetails!!
            )!!
        )
    }

    fun updateEndTime(time: Date) {
        val updatedCrisisTimeDetails = _screenState.value?.crisisDetails?.crisisTimeDetails?.copy(
            endTime = time
        )

        _screenState.value = _screenState.value?.copy(
            crisisDetails = _screenState.value?.crisisDetails?.copy(
                crisisTimeDetails = updatedCrisisTimeDetails!!
            )!!
        )
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
}
