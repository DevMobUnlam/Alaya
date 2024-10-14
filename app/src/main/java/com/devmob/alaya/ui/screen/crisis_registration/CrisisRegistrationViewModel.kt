package com.devmob.alaya.ui.screen.crisis_registration

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.devmob.alaya.domain.model.CrisisBodySensation
import com.devmob.alaya.domain.model.CrisisEmotion
import com.devmob.alaya.domain.model.CrisisPlace
import com.devmob.alaya.domain.model.CrisisTimeDetails
import com.devmob.alaya.domain.model.CrisisTool
import com.devmob.alaya.domain.model.Intensity
import java.util.Date

class CrisisRegistrationViewModel(): ViewModel() {

    @RequiresApi(Build.VERSION_CODES.O)
    private val _screenState = MutableLiveData(CrisisRegistrationScreenState())
    @RequiresApi(Build.VERSION_CODES.O)
    val screenState: LiveData<CrisisRegistrationScreenState> = _screenState

    var shouldShowExitModal by mutableStateOf(false)


    @RequiresApi(Build.VERSION_CODES.O)
    fun cleanState(){
        _screenState.value = CrisisRegistrationScreenState()
    }


    @RequiresApi(Build.VERSION_CODES.O)
    fun goOneStepForward(){
        _screenState.value = _screenState.value?.copy(currentStep = this._screenState.value?.currentStep!!.plus(1))
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun goOneStepBack(){
        _screenState.value = _screenState.value?.copy(currentStep = this._screenState.value?.currentStep!!.minus(1))
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun addCrisisPlace(place: CrisisPlace){
        _screenState.value?.crisisDetails?.placeList?.add(place)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun addCrisisBodySensation(bodySensation: CrisisBodySensation) {
        _screenState.value?.crisisDetails?.bodySensationList?.add(bodySensation)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun onBodySensationIntensityChange(intensity: Intensity, index: Int,bodySensation: CrisisBodySensation){
        val updatedBodySensation = bodySensation.copy(intensity = intensity)
        _screenState.value?.crisisDetails?.bodySensationList?.set(index,updatedBodySensation)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun onEmotionIntensityChange(intensity: Intensity, index: Int, emotion: CrisisEmotion){
        val updatedEmotion = emotion.copy(intensity = intensity)
        _screenState.value?.crisisDetails?.emotionList?.set(index,updatedEmotion)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun addCrisisTool(crisisTool: CrisisTool) {
        _screenState.value?.crisisDetails?.toolList?.add(crisisTool)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun addCrisisEmotion(crisisEmotion: CrisisEmotion) {
        _screenState.value?.crisisDetails?.emotionList?.add(crisisEmotion)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun updateNotes(text: String){
        _screenState.value = _screenState?.value?.copy(
            crisisDetails = _screenState?.value?.crisisDetails?.copy(
                notes = text
            )!!
        )
    }


    @RequiresApi(Build.VERSION_CODES.O)
    fun updatePlaceStatus(place: CrisisPlace, index: Int, isActive: Boolean){
        val updatedElement = place.copy(isActive = isActive)
        _screenState.value?.crisisDetails?.placeList?.set(index = index, updatedElement)
    }


    fun dismissExitModal() {
    shouldShowExitModal = false
    }

    fun showExitModal() {
        shouldShowExitModal = true
    }

    @RequiresApi(Build.VERSION_CODES.O)
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

    @RequiresApi(Build.VERSION_CODES.O)
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

    @RequiresApi(Build.VERSION_CODES.O)
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

    @RequiresApi(Build.VERSION_CODES.O)
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


}
