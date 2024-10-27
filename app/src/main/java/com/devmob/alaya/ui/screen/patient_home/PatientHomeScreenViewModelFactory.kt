package com.devmob.alaya.ui.screen.patient_home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.devmob.alaya.data.FirebaseClient
import com.devmob.alaya.domain.GetInvitationUseCase
import com.devmob.alaya.domain.GetUserDataUseCase

class PatientHomeScreenViewModelFactory(
    private val getUserData: GetUserDataUseCase,
    private val getInvitationUseCase: GetInvitationUseCase,
    private val firebaseClient: FirebaseClient
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PatientHomeScreenViewmodel::class.java)) {
            return PatientHomeScreenViewmodel(
                getUserData,
                getInvitationUseCase,
                firebaseClient
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}