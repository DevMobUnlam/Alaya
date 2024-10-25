package com.devmob.alaya.ui.screen.patient_home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.devmob.alaya.domain.GetInvitationUseCase
import com.devmob.alaya.domain.GetUserNameUseCase
import com.devmob.alaya.domain.GetUserSurnameUseCase
import com.devmob.alaya.domain.UpdateInvitationUseCase

class PatientHomeScreenViewModelFactory(
    private val getUserName: GetUserNameUseCase,
    private val getUserSurnameUseCase: GetUserSurnameUseCase,
    private val getInvitationUseCase: GetInvitationUseCase,
    private val updateInvitationUseCase: UpdateInvitationUseCase
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PatientHomeScreenViewmodel::class.java)) {
            return PatientHomeScreenViewmodel(
                getUserName,
                getUserSurnameUseCase,
                getInvitationUseCase,
                updateInvitationUseCase
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}