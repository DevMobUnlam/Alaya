package com.devmob.alaya.ui.screen.patient_profile

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devmob.alaya.domain.GetUserDataUseCase
import com.devmob.alaya.domain.model.User
import kotlinx.coroutines.launch

class PatientProfileViewModel(private val getEmailUseCase: GetUserDataUseCase) :
    ViewModel() {

    var patientData by mutableStateOf<User?>(null)

    fun getPatientData(email: String) {
        viewModelScope.launch {
            patientData = getEmailUseCase.getUser(email)
        }
    }
}