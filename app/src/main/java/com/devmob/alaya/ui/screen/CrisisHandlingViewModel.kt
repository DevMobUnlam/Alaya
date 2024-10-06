package com.devmob.alaya.ui.screen

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.devmob.alaya.domain.model.StepCrisis

class CrisisHandlingViewModel : ViewModel() {

    private var steps by mutableStateOf<List<StepCrisis>>(emptyList())

    private var currentStepIndex by mutableIntStateOf(0)

    val currentStep: StepCrisis
        get() = steps[currentStepIndex]

    init {
        fetchCrisisSteps()
    }

    private fun fetchCrisisSteps() {
        /*viewModelScope.launch {
            consultar al repositorio para obtener los pasos de la crisis
        }*/
        steps = listOf(
            StepCrisis("Controlar la respiración", "Poner una mano en el pecho y la otra en el estómago para tomar aire y soltarlo lentamente", "image_step_1"),
            StepCrisis("Imaginación guiada", "Cerrar los ojos y pensar en un lugar tranquilo, prestando atención a todos los sentidos del ambiente que te rodea", "image_step_2"),
            StepCrisis("Autoafirmaciones", "Repetir frases:\n" +
                    "“Soy fuerte y esto pasará”\n" +
                    "“Tengo el control de mi mente y mi cuerpo” \n" +
                    "“Me merezco tener alegría y plenitud”", "image_step_3")
        )
    }

    fun nextStep() {
        if (currentStepIndex < steps.size - 1) {
            currentStepIndex++
        }
    }
}