package com.devmob.alaya.ui.screen.crisis_handling

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

    var currentStepIndex by mutableIntStateOf(0)
    var shouldShowModal by mutableStateOf(false)
    var shouldShowExitModal by mutableStateOf(false)

    private val _loading = mutableStateOf(false)
    val loading: MutableState<Boolean>
        get() = _loading

    val currentStep: StepCrisis
        get() = steps[currentStepIndex]

    init {
        fetchCrisisSteps()
    }

    fun fetchCrisisSteps() {
        //TODO consultar al repositorio para obtener los pasos de la crisis
        _loading.value = true
        var stepCrisisList: List<StepCrisis>
        viewModelScope.launch {
            try {
                val optionTreatmentsList = currentUser?.email?.let { getCrisisTreatmentUseCase(it) }
                stepCrisisList = optionTreatmentsList?.map { option ->
                    StepCrisis(
                        title = option.title,
                        description = option.description,
                        image = option.imageUri.toString() // Convierte Uri a String
                    )
                } ?: emptyList()
                steps = stepCrisisList
                _loading.value = false
            } catch (e: Exception) {
                _loading.value = true
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