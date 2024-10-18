package com.devmob.alaya.ui.screen.crisis_registration

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class CrisisRegistrationSummaryViewModel: ViewModel() {

    private val _screenState = MutableLiveData(CrisisRegistrationScreenState())

    val screenState: LiveData<CrisisRegistrationScreenState> = _screenState

    var shouldShowModal by mutableStateOf(false)

    fun dismissExitModal() {
        shouldShowModal = false
    }

    fun showExitModal() {
        shouldShowModal = true
    }
}