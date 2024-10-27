package com.devmob.alaya.domain

import com.devmob.alaya.data.GetUserRepositoryImpl
import com.devmob.alaya.domain.model.Invitation
import com.devmob.alaya.domain.model.InvitationStatus
import com.devmob.alaya.domain.model.Patient
import com.devmob.alaya.domain.model.Professional

class GetInvitationUseCase {
    private val getUserUseCase = GetUserRepositoryImpl()

    suspend fun getInvitationProfessional(email: String): Invitation? {
        return getUserUseCase.getUser(email)?.invitation
    }

    suspend fun updateInvitation(email: String, fieldName: String, status: InvitationStatus) {
        getUserUseCase.updateUserField(email, fieldName, status.name)
    }

    suspend fun addProfessional(userId: String, professional: Professional) {
        getUserUseCase.addNewField(userId, "professional", professional)
    }

    suspend fun addPatient(userId: String, patient: Patient) {
        getUserUseCase.addNewFieldToList(userId, "patients", patient)
    }
}
