package com.devmob.alaya.domain

import com.devmob.alaya.data.GetUserRepositoryImpl
import com.devmob.alaya.data.NotificationRepositoryImpl
import com.devmob.alaya.domain.model.Invitation
import com.devmob.alaya.domain.model.InvitationStatus
import com.devmob.alaya.domain.model.Patient
import com.devmob.alaya.domain.model.Professional
import retrofit2.Response

class GetInvitationUseCase {
    private val getUserUseCase = GetUserRepositoryImpl()
    private val notificationRepository = NotificationRepositoryImpl()

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

    suspend fun sendInvitation(email: String, professionalEmail: String): Result<Unit> {
        return getUserUseCase.sendInvitation(email, professionalEmail)
    }

    suspend fun updateProfessionalInvitationList(
        professionalEmail: String,
        patientEmail: String,
        status: InvitationStatus
    ) {
        return getUserUseCase.updateProfessionalInvitationList(professionalEmail,patientEmail, status)
    }

    suspend fun sendNotification(patientEmail: String, professionalEmail: String): Response<Unit> {
        return notificationRepository.sendNotificationInvitation(patientEmail,professionalEmail)
    }
}
