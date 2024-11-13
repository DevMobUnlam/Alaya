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

    suspend fun getInvitations(email: String): List<Invitation>? {
        return getUserUseCase.getUser(email)?.invitations
    }

    suspend fun updateInvitation(email: String, fieldName: String, status: InvitationStatus) {
        getUserUseCase.updateUserField(email, fieldName, status.name)
    }

    suspend fun addProfessional(userId: String, professional: Professional) {
        getUserUseCase.addNewField(userId, "professional", professional)
    }

    suspend fun addPatient(userId: String, patient: Patient) {
        val document = getUserUseCase.getUser(userId)
        val currentList = document?.patients ?: emptyList()
        val updatedList = currentList + patient
        getUserUseCase.updateUserField(userId, "patients", updatedList)
    }

    suspend fun sendInvitation(email: String, professionalEmail: String): Result<Unit> {
        val invitationForPatient = Invitation(professionalEmail, InvitationStatus.PENDING)
        val invitationForProfessional = Invitation(email, InvitationStatus.PENDING)
        return getUserUseCase.sendInvitation(invitationForPatient, invitationForProfessional)
    }

    suspend fun updateProfessionalInvitationList(
        professionalEmail: String,
        patientEmail: String,
        status: InvitationStatus
    ) {
        val professional = getUserUseCase.getUser(professionalEmail) ?: return
        val updatedInvitations = professional.invitations.map { invitation ->
            if (invitation.email == patientEmail) {
                invitation.copy(status = status)
            } else invitation
        }
        return getUserUseCase.updateUserField(professionalEmail,"invitations", updatedInvitations)
    }

    suspend fun sendNotification(patientEmail: String, professionalEmail: String): Response<Unit> {
        return notificationRepository.sendNotificationInvitation(patientEmail,professionalEmail)
    }
}
