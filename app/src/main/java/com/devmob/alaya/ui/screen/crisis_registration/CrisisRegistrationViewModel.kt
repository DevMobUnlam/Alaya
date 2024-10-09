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
import com.devmob.alaya.domain.model.CrisisTool
import com.devmob.alaya.domain.model.Intensity

class CrisisRegistrationViewModel(): ViewModel() {

    @RequiresApi(Build.VERSION_CODES.O)
    private val _screenState = MutableLiveData(CrisisRegistrationScreenState(totalSteps = 6))
    @RequiresApi(Build.VERSION_CODES.O)
    val screenState: LiveData<CrisisRegistrationScreenState> = _screenState

    var shouldShowExitModal by mutableStateOf(false)

    var listprueba = listOf("Mauro","Jose")

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
        _screenState.value?.placeList?.add(place)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun addCrisisBodySensation(bodySensation: CrisisBodySensation) {
        _screenState.value?.bodySensationList?.add(bodySensation)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun onBodySensationIntensityChange(intensity: Intensity, index: Int,bodySensation: CrisisBodySensation){
        val updatedBodySensation = bodySensation.copy(intensity = intensity)
        _screenState.value?.bodySensationList?.set(index,updatedBodySensation)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun onEmotionIntensityChange(intensity: Intensity, index: Int, emotion: CrisisEmotion){
        val updatedEmotion = emotion.copy(intensity = intensity)
        _screenState.value?.emotionList?.set(index,updatedEmotion)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun addCrisisTool(crisisTool: CrisisTool) {
        _screenState.value?.toolList?.add(crisisTool)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun addCrisisEmotion(crisisEmotion: CrisisEmotion) {
        _screenState.value?.emotionList?.add(crisisEmotion)
    }

    fun dismissExitModal() {
        shouldShowExitModal = false
    }

    fun showExitModal() {
        shouldShowExitModal = true
        println("showExitModal updated, new value is $shouldShowExitModal")
    }


}