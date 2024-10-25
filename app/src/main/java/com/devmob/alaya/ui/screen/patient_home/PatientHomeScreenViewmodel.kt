package com.devmob.alaya.ui.screen.patient_home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devmob.alaya.data.FirebaseClient
import com.devmob.alaya.domain.GetInvitationUseCase
import com.devmob.alaya.domain.GetUserNameUseCase
import com.devmob.alaya.domain.GetUserSurnameUseCase
import com.devmob.alaya.domain.model.InvitationStatus
import kotlinx.coroutines.launch
import java.util.Calendar

class PatientHomeScreenViewmodel(
    private val getUserName: GetUserNameUseCase,
    private val getUserSurnameUseCase: GetUserSurnameUseCase,
    private val getInvitationUseCase: GetInvitationUseCase
) : ViewModel() {

    private val emailPatient = FirebaseClient().auth.currentUser?.email

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
        if (emailPatient.isNullOrEmpty()) return
        viewModelScope.launch {
            namePatient = getUserName(emailPatient) ?: ""
        }
    }

    private fun fetchProfessional(professionalEmail: String) {
        viewModelScope.launch {
            val name = getUserName(professionalEmail)
            val surname = getUserSurnameUseCase(professionalEmail)
            nameProfessional = "$name $surname"
        }
    }

    private fun checkProfessionalInvitation() {
        if (emailPatient.isNullOrEmpty()) return
        viewModelScope.launch {
            getInvitationUseCase.invoke(emailPatient)?.let { invitation ->
                when (invitation.status) {
                    InvitationStatus.PENDING -> {
                        isProfessionalInvitation = true
                        fetchProfessional(invitation.professionalEmail)
                    }

                    InvitationStatus.ACCEPTED, InvitationStatus.REJECTED, InvitationStatus.NONE -> {
                        isProfessionalInvitation = false
                    }
                }
            }
        }
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