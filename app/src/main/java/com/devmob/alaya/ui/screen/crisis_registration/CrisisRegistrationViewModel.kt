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
import com.devmob.alaya.domain.model.CrisisTool
import com.devmob.alaya.domain.model.FirebaseResult
import com.devmob.alaya.domain.model.Intensity
import com.devmob.alaya.domain.model.util.toDB
import com.devmob.alaya.utils.toCalendar
import com.devmob.alaya.utils.toDate
import kotlinx.coroutines.launch
import java.util.Calendar
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
    }

    var shouldShowExitModal by mutableStateOf(false)

    fun cleanState() {
        _screenState.value = CrisisRegistrationScreenState()
        shouldGoToBack = true
        shouldGoToSummary = false
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
        val oldDate = _screenState.value?.crisisDetails?.crisisTimeDetails?.startTime
        val newDate = updateDate(date, oldDate)
        val updatedCrisisTimeDetails = _screenState.value?.crisisDetails?.crisisTimeDetails?.copy(
            startTime = newDate
        )
        _screenState.value = _screenState.value?.copy(
            crisisDetails = _screenState.value?.crisisDetails?.copy(
                crisisTimeDetails = updatedCrisisTimeDetails!!
            )!!
        )

    }

    fun updateStartTime(hour: Date) {
        val oldDate = _screenState.value?.crisisDetails?.crisisTimeDetails?.startTime
        val newDate = updateHour(hour, oldDate)
        val updatedCrisisTimeDetails = _screenState.value?.crisisDetails?.crisisTimeDetails?.copy(
            startTime = newDate
        )
        _screenState.value = _screenState.value?.copy(
            crisisDetails = _screenState.value?.crisisDetails?.copy(
                crisisTimeDetails = updatedCrisisTimeDetails!!
            )!!
        )
    }

    fun updateEndDate(date: Date) {
        val oldTime = _screenState.value?.crisisDetails?.crisisTimeDetails?.endTime
        val newDate = updateDate(date, oldTime)
        val updatedCrisisTimeDetails = _screenState.value?.crisisDetails?.crisisTimeDetails?.copy(
            endTime = newDate
        )
        _screenState.value = _screenState.value?.copy(
            crisisDetails = _screenState.value?.crisisDetails?.copy(
                crisisTimeDetails = updatedCrisisTimeDetails!!
            )!!
        )
    }

    fun updateEndTime(hour: Date) {
        val oldTime = _screenState.value?.crisisDetails?.crisisTimeDetails?.endTime
        val newDate = updateHour(hour, oldTime)
        val updatedCrisisTimeDetails = _screenState.value?.crisisDetails?.crisisTimeDetails?.copy(
            endTime = newDate
        )
        _screenState.value = _screenState.value?.copy(
            crisisDetails = _screenState.value?.crisisDetails?.copy(
                crisisTimeDetails = updatedCrisisTimeDetails!!
            )!!
        )
    }

    private fun updateDate(newDate: Date, oldTime: Date?): Date {
        val newDateCalendar = newDate.toCalendar()
        val oldTimeCalendar = oldTime.toCalendar()
        oldTimeCalendar.set(Calendar.DAY_OF_MONTH, newDateCalendar.get(Calendar.DAY_OF_MONTH))
        oldTimeCalendar.set(Calendar.MONTH, newDateCalendar.get(Calendar.MONTH))
        oldTimeCalendar.set(Calendar.YEAR, newDateCalendar.get(Calendar.YEAR))
        return oldTimeCalendar.toDate()
    }

    private fun updateHour(newDate: Date, oldTime: Date?): Date {
        val newDateCalendar = newDate.toCalendar()
        val oldTimeCalendar = oldTime.toCalendar()
        oldTimeCalendar.set(Calendar.HOUR_OF_DAY, newDateCalendar.get(Calendar.HOUR_OF_DAY))
        oldTimeCalendar.set(Calendar.MINUTE, newDateCalendar.get(Calendar.MINUTE))
        return oldTimeCalendar.toDate()
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
        viewModelScope.launch {
            val crisis = _screenState.value?.crisisDetails?.toDB()
            val response = crisis?.let { saveCrisisRegistrationUseCase(it) }
            when (response) {
                is FirebaseResult.Success -> {
                    Log.d("CrisisRegistrationViewModel", "saveRegister: Success")
                }

                is FirebaseResult.Error -> {
                    Log.d("CrisisRegistrationViewModel", "saveRegister: Error")
                }

                null -> Log.d("CrisisRegistrationViewModel", "saveRegister: null ")
            }
        }
    }
}
