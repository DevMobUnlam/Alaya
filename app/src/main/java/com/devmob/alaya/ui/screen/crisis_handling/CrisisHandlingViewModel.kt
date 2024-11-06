package com.devmob.alaya.ui.screen.crisis_handling

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devmob.alaya.data.FirebaseClient
import com.devmob.alaya.domain.GetCrisisTreatmentUseCase
import com.devmob.alaya.domain.SaveCrisisTreatmentUseCase
import com.devmob.alaya.domain.model.OptionTreatment
import com.devmob.alaya.domain.model.StepCrisis
import kotlinx.coroutines.launch

class CrisisHandlingViewModel(private val getCrisisTreatmentUseCase: GetCrisisTreatmentUseCase) :
    ViewModel() {

    val currentUser = FirebaseClient().auth.currentUser
    var steps by mutableStateOf<List<StepCrisis>>(emptyList())
    var optionTreatmentsList by mutableStateOf<List<OptionTreatment>?>(null)
    var currentStepIndex by mutableIntStateOf(0)
    var shouldShowModal by mutableStateOf(false)
    var shouldShowExitModal by mutableStateOf(false)

    private val _loading = mutableStateOf(true)
    val loading: MutableState<Boolean>
        get() = _loading

    val currentStep: StepCrisis?
        get() = if (steps.isNotEmpty()) {
            steps[currentStepIndex]
        } else null


    init {
        fetchCrisisSteps()
    }

    fun fetchCrisisSteps() {
        var stepCrisisList: List<StepCrisis>
        viewModelScope.launch {
            _loading.value = true
            try {
                Log.d("leandro", "current user ${currentUser?.email}")
                optionTreatmentsList = currentUser?.email?.let { getCrisisTreatmentUseCase(it) }

                Log.d("leandro", "la corutina del optiontreatment list : $optionTreatmentsList")
                if (!optionTreatmentsList.isNullOrEmpty()) {
                    Log.d("leandro", "el option treatment no es null : $optionTreatmentsList")
                    stepCrisisList = optionTreatmentsList!!.map { option ->
                        StepCrisis(
                            title = option.title,
                            description = option.description,
                            image = option.imageUri
                        )
                    }
                    steps = stepCrisisList
                    _loading.value = false
                }

            } catch (e: Exception) {
                _loading.value = false
                Log.d("leandro", "catch de fetchcrisis $e")
            }
        }
    }

    fun nextStep() {
        if (currentStepIndex < steps.size - 1) {
            currentStepIndex++
        } else {
            shouldShowModal = true
        }
    }

    fun showModal() {
        shouldShowModal = true
    }

    fun dismissModal() {
        shouldShowModal = false
    }

    fun showExitModal() {
        shouldShowExitModal = true
    }

    fun dismissExitModal() {
        shouldShowExitModal = false
    }
}