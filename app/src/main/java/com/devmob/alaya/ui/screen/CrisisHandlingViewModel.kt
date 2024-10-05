package com.devmob.alaya.ui.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devmob.alaya.domain.model.StepCrisis
import kotlinx.coroutines.launch

class CrisisHandlingViewModel : ViewModel() {

    fun getCrisisSteps(): List<StepCrisis> {
        /*viewModelScope.launch {
            consultar al repositorio para obtener los pasos de la crisis
        }*/
        return listOf(
            StepCrisis("Paso 1", "Esta descripcion es del paso 1", ""),
            StepCrisis("Paso 2", "Esta descripcion es del paso 2", ""),
            StepCrisis("Paso 3", "Esta descripcion es del paso 3", "")
        )
    }
}