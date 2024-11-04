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
    var isLoading by mutableStateOf(false)

    fun getPatientData(email: String) {
        viewModelScope.launch {
            isLoading = true
            patientData = getEmailUseCase.getUser(email)
            isLoading = false
        }
    }
}