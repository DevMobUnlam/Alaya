package com.devmob.alaya.ui.screen

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.devmob.alaya.domain.model.CrisisBodySensation
import com.devmob.alaya.domain.model.CrisisEmotion
import com.devmob.alaya.domain.model.CrisisPlace
import com.devmob.alaya.domain.model.CrisisTool

class CrisisRegistrationViewModel(): ViewModel() {

    @RequiresApi(Build.VERSION_CODES.O)
    private val _screenState = MutableLiveData(CrisisRegistrationScreenState(totalSteps = 6))
    @RequiresApi(Build.VERSION_CODES.O)
    val screenState: LiveData<CrisisRegistrationScreenState> = _screenState

    @RequiresApi(Build.VERSION_CODES.O)
    fun cleanState(){
        _screenState.value = CrisisRegistrationScreenState()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun addCrisisPlace(place: CrisisPlace){
        _screenState.value = _screenState.value!!.copy(
            placeList = this._screenState.value!!.placeList + place
        )
    }

}