package com.devmob.alaya.ui.screen.professionalCrisisTreatment

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devmob.alaya.domain.SaveCrisisTreatmentUseCase
import com.devmob.alaya.domain.model.FirebaseResult
import com.devmob.alaya.domain.model.OptionTreatment
import kotlinx.coroutines.launch

class ConfigTreatmentViewModel(
    private val saveCrisisUseCase: SaveCrisisTreatmentUseCase
) : ViewModel() {

    var patientEmail = mutableStateOf("")

    private val _showError = mutableStateOf(false)
    val showError: MutableState<Boolean>
        get() = _showError

    private val _navigate = mutableStateOf(false)
    val navigate: MutableState<Boolean>
        get() = _navigate

    var firstSelectOption = mutableStateOf<OptionTreatment?>(null)
    var secondSelectOption = mutableStateOf<OptionTreatment?>(null)
    var thirdSelectOption = mutableStateOf<OptionTreatment?>(null)

    private var _treatmentOptions = mutableStateListOf(
        OptionTreatment(
            title = "Controlar la respiración",
            description = "Poner una mano en el pecho y otra en el estómago para tomar aire y soltarlo lentamente"
        ),
        OptionTreatment(
            title = "Imaginación guiada",
            description = "Cerrar los ojos y pensar en un lugar tranquilo, prestando atención a todos los sentidos del ambiente que te rodea"
        ),
        OptionTreatment(
            title = "Autoafirmaciones",
            description = """
                Repetir frases:
                “Soy fuerte y esto pasará”
                “Tengo el control de mi mente y mi cuerpo”
                “Me merezco tener alegría y plenitud”
            """.trimIndent()
        )
    )
    val treatmentOptions: SnapshotStateList<OptionTreatment> = _treatmentOptions

    fun addCustomActivity(activity: OptionTreatment) {
        _treatmentOptions.add(activity)
        Log.d("leandro", "treatment list vm ${_treatmentOptions.size}")
    }

    fun saveCrisisTreatment(patientEmail: String, listOfTreatment: List<OptionTreatment> ) {
        viewModelScope.launch {
            Log.d("leandro","treatment list que va a firebase $listOfTreatment")
            when (saveCrisisUseCase(patientEmail, listOfTreatment)) {
                is FirebaseResult.Error -> {
                    Log.d("saveCrisisTreatment", "No se pudo guardar el tratamiento de crisis")
                    Log.d("leandro", "No se pudo guardar el tratamiento de crisis.")
                    _showError.value = true
                }

                FirebaseResult.Success -> {
                    Log.d("leandro", "Se guardó el registro de crisis")
                    _navigate.value = true
                }
            }
        }
    }
}