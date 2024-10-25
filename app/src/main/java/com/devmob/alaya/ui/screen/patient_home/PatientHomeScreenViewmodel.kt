package com.devmob.alaya.ui.screen.patient_home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import java.util.Calendar

class PatientHomeScreenViewmodel : ViewModel() {
    var nameProfessional by mutableStateOf("")
    var namePatient by mutableStateOf("")
    var greetingMessage by mutableStateOf("")
    var isProfessionalInvitation by mutableStateOf(false)

    init {
        fetchPatient()
        updateGreetingMessage()
        checkProfessionalInvitation()
    }

    private fun fetchPatient() {
        namePatient = "Flor"
    }

    private fun fetchProfessional() {
        nameProfessional = "Patricia Bertero"
    }

    private fun checkProfessionalInvitation() {
        fetchProfessional()
        isProfessionalInvitation = true
    }

    fun dismissModal() {
        isProfessionalInvitation = false
    }

    private fun updateGreetingMessage() {
        val calendar = Calendar.getInstance()
        val hourOfDay = calendar.get(Calendar.HOUR_OF_DAY)
        greetingMessage = when (hourOfDay) {
            in 5..11 -> "Buenos días"
            in 12..19 -> "Buenas tardes"
            else -> "Buenas noches"
        }
    }
}