package com.devmob.alaya.ui.screen.patient_home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devmob.alaya.data.FirebaseClient
import com.devmob.alaya.domain.GetInvitationUseCase
import com.devmob.alaya.domain.GetUserDataUseCase
import com.devmob.alaya.domain.UpdateInvitationUseCase
import com.devmob.alaya.domain.model.InvitationStatus
import kotlinx.coroutines.launch
import java.util.Calendar

class PatientHomeScreenViewmodel(
    private val getUserData: GetUserDataUseCase,
    private val getInvitationUseCase: GetInvitationUseCase,
    private val updateInvitationUseCase: UpdateInvitationUseCase
) : ViewModel() {

    var nameProfessional by mutableStateOf("")
    var namePatient by mutableStateOf("")
    var greetingMessage by mutableStateOf("")
    var shouldShowInvitation by mutableStateOf(false)
    private var emailPatient by mutableStateOf("")

    fun fetchPatient() {
        getEmailPatient()
        viewModelScope.launch {
            namePatient = getUserData.getName(emailPatient) ?: ""
        }
    }

    private fun getEmailPatient() {
        emailPatient = FirebaseClient().auth.currentUser?.email.toString()
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
            getInvitationUseCase.invoke(emailPatient)?.let { invitation ->
                when (invitation.status) {
                    InvitationStatus.PENDING -> {
                        fetchProfessional(invitation.professionalEmail)
                    }

                    InvitationStatus.ACCEPTED, InvitationStatus.REJECTED, InvitationStatus.NONE -> {
                        shouldShowInvitation = false
                    }
                }
            }
        }
    }

    fun acceptInvitation() {
        updateInvitationStatus(InvitationStatus.ACCEPTED.name)
        shouldShowInvitation = false
    }

    fun rejectInvitation() {
        updateInvitationStatus(InvitationStatus.REJECTED.name)
        shouldShowInvitation = false
    }

    private fun updateInvitationStatus(status: String) {
        emailPatient.let {
            viewModelScope.launch {
                updateInvitationUseCase(it, status)
            }
        }
    }

    private fun addProfessionalToPatient(professionalEmail: String) {

    }
}