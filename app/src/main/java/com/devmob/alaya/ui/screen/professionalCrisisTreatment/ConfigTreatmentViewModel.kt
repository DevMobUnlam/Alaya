package com.devmob.alaya.ui.screen.professionalCrisisTreatment

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devmob.alaya.data.FirebaseClient
import com.devmob.alaya.domain.SaveCrisisTreatmentUseCase
import com.devmob.alaya.domain.model.FirebaseResult
import com.devmob.alaya.domain.model.OptionTreatment
import kotlinx.coroutines.launch

class ConfigTreatmentViewModel(
    private val saveCrisisUseCase: SaveCrisisTreatmentUseCase
) : ViewModel() {

    var patientEmail = mutableStateOf("")
    val professionalEmail = FirebaseClient().auth.currentUser?.email

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
            description = "Poner una mano en el pecho y otra en el estómago para tomar aire y soltarlo lentamente",
            imageUri = "https://firebasestorage.googleapis.com/v0/b/alaya-db7b8.appspot.com/o/customOptionTreatment%2FSTEP%201%20RESPIRACION.png?alt=media&token=b8168202-57ca-4d8f-b47a-3c53f45c6b35"
        ),
        OptionTreatment(
            title = "Imaginación guiada",
            description = "Cerrar los ojos y pensar en un lugar tranquilo, prestando atención a todos los sentidos del ambiente que te rodea",
            imageUri = "https://firebasestorage.googleapis.com/v0/b/alaya-db7b8.appspot.com/o/customOptionTreatment%2FSTEP%202%20cerrar%20los%20ojos.png?alt=media&token=958eb6a6-ef06-4af2-ac7b-5c1ec26ba9fc"

        ),
        OptionTreatment(
            title = "Autoafirmaciones",
            description = """
                Repetir frases:
                “Soy fuerte y esto pasará”
                “Tengo el control de mi mente y mi cuerpo”
                “Me merezco tener alegría y plenitud”
            """.trimIndent(),
            imageUri = "https://firebasestorage.googleapis.com/v0/b/alaya-db7b8.appspot.com/o/customOptionTreatment%2FSTEP%203afirmaciones.png?alt=media&token=b7b51703-7454-4ae3-a531-32205ff310d3"
        )
    )
    val treatmentOptions: SnapshotStateList<OptionTreatment> = _treatmentOptions

    fun addCustomActivity(activity: OptionTreatment) {
        _treatmentOptions.add(activity)
    }

    fun saveCrisisTreatment(patientEmail: String, listOfTreatment: List<OptionTreatment>) {
        viewModelScope.launch {
            when (saveCrisisUseCase(patientEmail, listOfTreatment)) {
                is FirebaseResult.Error -> {
                    Log.d("saveCrisisTreatment", "Cannot save the CrisisTreatment")
                    _showError.value = true
                }

                FirebaseResult.Success -> {
                    _navigate.value = true
                }
            }
        }
    }

    fun sendNotification(patientEmail: String, professionalEmail: String) {
        viewModelScope.launch {
            val notification =
                saveCrisisUseCase.sendNotification(patientEmail, professionalEmail)
            if (notification.isSuccessful) {
                Log.i(
                    "ConfigTreatmentViewModel",
                    "Notificacion enviada satisfactoriamente a: $patientEmail"
                )
            } else {
                Log.w(
                    "ConfigTreatmentViewModel",
                    "No se pudo enviar la notificación  a: $patientEmail"
                )
            }
        }
    }
}

