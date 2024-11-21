package com.devmob.alaya.domain

import com.devmob.alaya.domain.model.Invitation
import com.devmob.alaya.domain.model.InvitationStatus
import com.devmob.alaya.domain.model.Patient
import com.devmob.alaya.domain.model.Professional
import retrofit2.Response

class GetInvitationUseCase(
    private val getUserRepository: GetUserRepository,
    private val notificationRepository: NotificationRepository
) {

    suspend fun getInvitationProfessional(email: String): Invitation? {
        return getUserRepository.getUser(email)?.invitation
    }

    suspend fun updateInvitation(email: String, fieldName: String, status: InvitationStatus) {
        getUserRepository.updateUserField(email, fieldName, status.name)
    }

    suspend fun addProfessional(userId: String, professional: Professional) {
        getUserRepository.addNewField(userId, "professional", professional)
    }

    suspend fun addPatient(userId: String, patient: Patient) {
        val document = getUserRepository.getUser(userId)
        val currentList = document?.patients ?: emptyList()
        val updatedList = currentList + patient
        getUserRepository.updateUserField(userId, "patients", updatedList)
    }

    suspend fun sendInvitation(email: String, professionalEmail: String): Result<Unit> {
        val invitationForPatient = Invitation(professionalEmail, InvitationStatus.PENDING)
        val invitationForProfessional = Invitation(email, InvitationStatus.PENDING)
        return getUserRepository.sendInvitation(invitationForPatient, invitationForProfessional)
    }

    suspend fun updateProfessionalInvitationList(
        professionalEmail: String,
        patientEmail: String,
        status: InvitationStatus
    ) {
        val professional = getUserRepository.getUser(professionalEmail) ?: return
        val updatedInvitations = professional.invitations.map { invitation ->
            if (invitation.email == patientEmail) {
                invitation.copy(status = status)
            } else invitation
        }
        return getUserRepository.updateUserField(professionalEmail, "invitations", updatedInvitations)
    }

    suspend fun sendNotification(patientEmail: String): Response<Unit> {
        return notificationRepository.sendNotificationInvitation(patientEmail)
    }
}
