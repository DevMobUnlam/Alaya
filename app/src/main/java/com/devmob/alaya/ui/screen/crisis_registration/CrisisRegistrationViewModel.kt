package com.devmob.alaya.ui.screen.crisis_registration

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devmob.alaya.domain.SaveCrisisRegistrationUseCase
import com.devmob.alaya.domain.model.CrisisBodySensation
import com.devmob.alaya.domain.model.CrisisDetailsDB
import com.devmob.alaya.domain.model.CrisisEmotion
import com.devmob.alaya.domain.model.CrisisPlace
import com.devmob.alaya.domain.model.CrisisTimeDetails
import com.devmob.alaya.domain.model.CrisisTool
import com.devmob.alaya.domain.model.FirebaseResult
import com.devmob.alaya.domain.model.Intensity
import com.devmob.alaya.domain.model.util.toDB
import com.devmob.alaya.ui.screen.crisis_registration.GridElementsRepository.returnAvailableTools
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
    private val _crisisTimeDetails = MutableLiveData(CrisisTimeDetails())
    val crisisTimeDetails: LiveData<CrisisTimeDetails> get() = _crisisTimeDetails

    var selectedTools = mutableStateListOf<String>()

    init {
        loadPlaces()
        loadTools()
        loadBodySensations()
        loadEmotions()
        loadLastCrisisDetails()
    }


    var shouldShowExitModal by mutableStateOf(false)

    private val _crisisDetails = MutableLiveData<CrisisDetailsDB?>()
    val crisisDetails: LiveData<CrisisDetailsDB?> get() = _crisisDetails

    fun loadLastCrisisDetails() {
        viewModelScope.launch {
            val result = saveCrisisRegistrationUseCase.getLastCrisisDetails()
            _crisisDetails.value = result

            if (result != null) {
                if (result.completed == false) { //opciones precargadas cuando el registro esta incompleto
                    val startTime = result.start
                    val endTime = result.end

                    if (startTime != null && endTime != null) {
                        val crisisTimeDetails = CrisisTimeDetails(
                            startTime = startTime,
                            endTime = endTime
                        )
                        _crisisTimeDetails.value = crisisTimeDetails
                    }
                    selectedTools.clear()
                    val availableTools = returnAvailableTools()
                    for (tool in availableTools) {
                        if (result.tools.contains(tool.id)) {
                            selectedTools.add(tool.id)
                        }
                    }
                } else {
                    _crisisTimeDetails.value = CrisisTimeDetails()
                    selectedTools.clear()
                }

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

            // Si existe un registro incompleto, lo actualizo
            if (_crisisDetails.value != null && _crisisDetails.value?.completed == false) {
                val updatedCrisis = crisis?.copy(completed = true)
                val response = updatedCrisis?.let { saveCrisisRegistrationUseCase.updateCrisisDetails(it) }

                when (response) {
                    is FirebaseResult.Success -> {
                        Log.d("CrisisRegistrationViewModel", "Registro actualizado exitosamente")
                    }
                    is FirebaseResult.Error -> {
                        Log.d("CrisisRegistrationViewModel", "Error al actualizar el registro")
                    }
                    null -> Log.d("CrisisRegistrationViewModel", "Respuesta null")
                }
            } else {
                val response = crisis?.let { saveCrisisRegistrationUseCase.invoke(it) }

                when (response) {
                    is FirebaseResult.Success -> {
                        Log.d("CrisisRegistrationViewModel", "Nuevo registro guardado exitosamente")
                    }
                    is FirebaseResult.Error -> {
                        Log.d("CrisisRegistrationViewModel", "Error al guardar el nuevo registro")
                    }
                    null -> Log.d("CrisisRegistrationViewModel", "Respuesta null")
                }
            }
        }
    }
}
