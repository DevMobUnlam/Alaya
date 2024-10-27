package com.devmob.alaya.ui.screen.patient_home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devmob.alaya.data.FirebaseClient
import com.devmob.alaya.domain.GetInvitationUseCase
import com.devmob.alaya.domain.GetUserDataUseCase
import com.devmob.alaya.domain.model.InvitationStatus
import com.devmob.alaya.domain.model.Patient
import com.devmob.alaya.domain.model.Professional
import kotlinx.coroutines.launch
import java.util.Calendar

class PatientHomeScreenViewmodel(
    private val getUserData: GetUserDataUseCase,
    private val getInvitationUseCase: GetInvitationUseCase,
    private val firebaseClient: FirebaseClient = FirebaseClient()
) : ViewModel() {

    var nameProfessional by mutableStateOf("")
    var namePatient by mutableStateOf("")
    var greetingMessage by mutableStateOf("")
    var shouldShowInvitation by mutableStateOf(false)
    private var emailPatient by mutableStateOf("")
    private var emailProfessional by mutableStateOf("")

    fun fetchPatient() {
        getEmailPatient()
        viewModelScope.launch {
            namePatient = getUserData.getName(emailPatient) ?: ""
        }
    }

    private fun getEmailPatient() {
        emailPatient = firebaseClient.auth.currentUser?.email.toString()
    }

    fun updateGreetingMessage() {
        val calendar = Calendar.getInstance()
        val hourOfDay = calendar.get(Calendar.HOUR_OF_DAY)
        greetingMessage = when (hourOfDay) {
            in 5..11 -> "Buenos días"
            in 12..19 -> "Buenas tardes"
            else -> "Buenas noches"
        }
    }

    private fun fetchProfessional(professionalEmail: String) {
        viewModelScope.launch {
            val name = getUserData.getName(professionalEmail)
            val surname = getUserData.getSurname(professionalEmail)
            nameProfessional = "$name $surname"
            shouldShowInvitation = true
        }
    }

    fun checkProfessionalInvitation() {
        viewModelScope.launch {
            getInvitationUseCase.getInvitationProfessional(emailPatient)?.let { invitation ->
                when (invitation.status) {
                    InvitationStatus.PENDING -> {
                        fetchProfessional(invitation.professionalEmail)
                        emailProfessional = invitation.professionalEmail
                    }

                    InvitationStatus.ACCEPTED, InvitationStatus.REJECTED, InvitationStatus.NONE -> {
                        shouldShowInvitation = false
                    }
                }
            }
        }
    }

    fun acceptInvitation() {
        updateInvitationStatus(InvitationStatus.ACCEPTED)
        addProfessionalToPatient()
        addPatientToProfessional()
        shouldShowInvitation = false
    }

    fun rejectInvitation() {
        updateInvitationStatus(InvitationStatus.REJECTED)
        shouldShowInvitation = false
    }

    private fun updateInvitationStatus(status: InvitationStatus) {
        emailPatient.let {
            viewModelScope.launch {
                getInvitationUseCase.updateInvitation(it, "invitation.status", status)
            }
        }
    }

    private fun addProfessionalToPatient() {
        viewModelScope.launch {
            val professionalData = getUserData.getUser(emailProfessional)
            val professional = Professional(
                emailProfessional,
                professionalData?.name ?: "",
                professionalData?.surname ?: "",
                professionalData?.phone ?: ""
            )
            getInvitationUseCase.addProfessional(emailPatient, professional)
        }
    }

    private fun addPatientToProfessional() {
        viewModelScope.launch {
            val patientData = getUserData.getUser(emailPatient)
            val patient = Patient(
                emailPatient,
                patientData?.name ?: "",
                patientData?.surname ?: "",
                patientData?.phone ?: ""
            )
            getInvitationUseCase.addPatient(emailProfessional, patient)
        }
    }
}