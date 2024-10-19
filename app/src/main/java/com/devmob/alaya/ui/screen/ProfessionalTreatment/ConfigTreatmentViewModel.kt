package com.devmob.alaya.ui.screen.ProfessionalTreatment

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.devmob.alaya.domain.model.OptionTreatment

class ConfigTreatmentViewModel : ViewModel() {

    var firstSelectOption = mutableStateOf<OptionTreatment?>(null)
    var secondSelectOption = mutableStateOf<OptionTreatment?>(null)
    var thirdSelectOption = mutableStateOf<OptionTreatment?>(null)

    private val _treatmentOptions = mutableStateListOf(
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
    val treatmentOptions: List<OptionTreatment> get() = _treatmentOptions

    // Agregar una actividad personalizada
    fun addCustomActivity(activity: OptionTreatment) {
        _treatmentOptions.add(activity)
        println("Actividad agregada: ${activity.title}")
    }
}